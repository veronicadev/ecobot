package veronicadev.ecobot.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import veronicadev.ecobot.Application;
import veronicadev.ecobot.models.Area;
import veronicadev.ecobot.models.AreaCalendar;
import veronicadev.ecobot.models.RecyclingDepot;
import veronicadev.ecobot.models.TrashContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static Logger logger = LoggerFactory.getLogger(DataManager.class);
    private static DataManager dataManager = null;
    private static String municipalityName;
    private static RecyclingDepot recyclingDepot;
    private static List<Area> areas = new ArrayList<>();
    private DataManager(){}
    public static DataManager getInstance(){
        logger.info("** DataManager getting instance **");
        if(dataManager ==null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    @SuppressWarnings("unchecked")
    public JSONObject readJSON(String path){
        logger.info("** DataManager retrieving data init **");
        JSONObject obj = null;
        InputStream input = DataManager.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            obj = new JSONObject(sb.toString());
            areas = getAreasFromJSON(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("** Catched FileNotFoundException in DataManager.readJSON **", e);
        }  catch (IOException e) {
            e.printStackTrace();
            logger.error("** Catched IOException in DataManager.readJSON **", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("** Catched Exception in DataManager.readJSON **", e);
        }
        logger.info("** DataManager retrieving data end **");
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
            throw new Exception("Expected field 'areas' in data.json");
        }
        return areas;
    }

    private static RecyclingDepot getRecyclingDepotFromJSON(JSONObject jsonFile) throws Exception{
        RecyclingDepot recyclingDepot = new RecyclingDepot();
        if(jsonFile.has("recyclingDepot")){
            JSONObject rd = jsonFile.getJSONObject("recyclingDepot");
            recyclingDepot.setAddress(rd.getString("address"));
            recyclingDepot.setTime(rd.getString("time"));
            recyclingDepot.setTelephone(rd.getString("telephone"));
        }else{
            throw new Exception("Expected field 'recyclingDepot' in data.json");
        }
        return recyclingDepot;
    }

    private static List<AreaCalendar> getWeekCalendar(JSONObject areaJson) throws Exception {
        List<AreaCalendar> list = new ArrayList<>();
        if(areaJson.has("weekCalendar")){
            JSONArray weekCalendarJSON = areaJson.getJSONArray("weekCalendar");
            for (Object w:weekCalendarJSON ) {
                JSONObject weekJSON = (JSONObject) w;
                JSONArray containersJSON = weekJSON.getJSONArray("containers");
                ArrayList<TrashContainer> containers = new ArrayList<>();
                for (Object c: containersJSON) {
                    JSONObject cJSON = (JSONObject) c;
                    TrashContainer trashContainer = new TrashContainer();
                    trashContainer.setType(cJSON.getString("type"));
                    if(cJSON.has("hoursRange")){
                        trashContainer.setHoursRange(cJSON.getString("hoursRange"));
                    }
                    containers.add(trashContainer);
                }
                AreaCalendar areaCalendar = new AreaCalendar();
                areaCalendar.setContainers(containers);
                areaCalendar.setDay(weekJSON.getString("day"));
                list.add(areaCalendar);
            }
        }else{
            throw new Exception("Expected field 'weekCalendar' in data.json");
        }
        return list;
    }

    public List<TrashContainer> findContainers(String day, Area area){
        for (AreaCalendar areaCalendar : area.getWeekCalendar()) {
            if(areaCalendar.getDay().equals(day)){
                return areaCalendar.getContainers();
            }
        }
        return new ArrayList<>();
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
