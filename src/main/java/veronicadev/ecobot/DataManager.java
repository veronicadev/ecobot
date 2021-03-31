package veronicadev.ecobot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager dataManager = null;
    private static String municipalityName;
    private static RecyclingDepot recyclingDepot;
    private static List<Area> areas = new ArrayList<>();
    private DataManager(){}
    public static DataManager getInstance(){
        if(dataManager ==null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    @SuppressWarnings("unchecked")
    public JSONObject readJSON(String path){
        System.out.println("** Retrieving data init **");
        JSONObject obj = null;
        InputStream input = DataManager.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            //Read JSON file
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            obj = new JSONObject(sb.toString());
            areas = getAreasFromJSON(obj);
            System.out.println(areas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("** Retrieving data end **");
        return obj;
    }

    public static ArrayList<Area> getAreasFromJSON(JSONObject jsonFile) throws Exception{
        ArrayList<Area> areas = new ArrayList<>();
        municipalityName = jsonFile.getString("municipalityName");
        recyclingDepot = getRecyclingDepotFromJSON(jsonFile);
        if(jsonFile.has("areas")){
            JSONArray areasJson = jsonFile.getJSONArray("areas");
            for (Object a: areasJson) {
                Area area = new Area();
                JSONObject areaJson = (JSONObject) a;
                area.setName(areaJson.getString("name"));
                area.setStreets(areaJson.getString("streets"));
                if(areaJson.has("addressedTo")){
                    area.setAddressedTo(areaJson.getString("addressedTo"));
                }
                area.setWeekCalendar(getWeekCalendar(areaJson));
                areas.add(area);
            }
        }else{
            throw new Exception("Expected field 'areas' in Data.json");
        }
        return areas;
    }

    private static RecyclingDepot getRecyclingDepotFromJSON(JSONObject jsonFile){
        RecyclingDepot recyclingDepot = new RecyclingDepot();
        if(jsonFile.has("recyclingDepot")){
            JSONObject rd = jsonFile.getJSONObject("recyclingDepot");
            recyclingDepot.setAddress(rd.getString("address"));
            recyclingDepot.setTime(rd.getString("time"));
            recyclingDepot.setTelephone(rd.getString("telephone"));
        }
        return recyclingDepot;
    }

    private static ArrayList<TrashContainer> getWeekCalendar(JSONObject areaJson){
        ArrayList<TrashContainer> week = new ArrayList<>();
        if(areaJson.has("weekCalendar")){
            JSONArray weekCalendarJson = areaJson.getJSONArray("weekCalendar");
            for (Object w: weekCalendarJson) {
                JSONObject weekJson = (JSONObject) w;
                TrashContainer trashContainer = new TrashContainer();
                trashContainer.setDay(weekJson.getString("day"));
                trashContainer.setType(weekJson.getString("type"));
                if(weekJson.has("hoursRange")){
                    trashContainer.setHoursRange(weekJson.getString("hoursRange"));
                }
                week.add(trashContainer);
            }
        }
        return week;
    }

    public TrashContainer findContainer(String day, Area area){
        for (TrashContainer trashContainer: area.getWeekCalendar()) {
            if(trashContainer.getDay().equals(day)){
                return trashContainer;
            }
        }
        return null;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public RecyclingDepot getRecyclingDepot() {
        return recyclingDepot;
    }
}
