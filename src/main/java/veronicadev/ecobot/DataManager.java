package veronicadev.ecobot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager dataManager = null;
    private static String municipalityName;
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
        }
        System.out.println("** Retrieving data end **");
        return obj;
    }

    public static ArrayList<Area> getAreasFromJSON(JSONObject jsonFile){
        ArrayList<Area> areas = new ArrayList<>();
        municipalityName = jsonFile.getString("municipalityName");
        if(jsonFile.has("areas")){
            JSONArray areasJson = jsonFile.getJSONArray("areas");
            for (Object a: areasJson) {
                Area area = new Area();
                JSONObject areaJson = (JSONObject) a;
                area.setName(areaJson.getString("name"));
                area.setStreets(areaJson.getString("streets"));
                area.setAddressedTo(areaJson.getString("addressedTo"));
                area.setWeekCalendar(getWeekCalendar(areaJson));
                areas.add(area);
            }
        }
        return areas;
    }

    private static ArrayList<TrashContainer> getWeekCalendar(JSONObject areaJson){
        ArrayList<TrashContainer> week = new ArrayList<>();
        if(areaJson.has("weekCalendar")){
            JSONArray weekCalendarJson = areaJson.getJSONArray("weekCalendar");
            for (Object w: weekCalendarJson) {
                JSONObject weekJson = (JSONObject) w;
                TrashContainer trashContainer = new TrashContainer();
                trashContainer.setDay(weekJson.getString("day"));
                trashContainer.setType(TrashType.valueOf(weekJson.getString("type")));
                week.add(trashContainer);
            }
        }
        return week;
    }

    public String findByDay(String day, Area area){
        String type = "Niente";
        for (TrashContainer trashContainer: area.getWeekCalendar()) {
            if(trashContainer.getDay().equals(day)){
                type = trashContainer.getType().getName();
            }
        }
        return type;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public List<Area> getAreas() {
        return areas;
    }
}