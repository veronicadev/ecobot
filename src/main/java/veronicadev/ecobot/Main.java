package veronicadev.ecobot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static String municipalityName;
    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            System.out.println("** EcoBot init **");
            api.registerBot(new EcoBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    private static ArrayList<Area> getAreas() {
        try{
            JSONObject jsonFile = FileManager.readJSON("data.json");
            municipalityName = jsonFile.getString("municipalityName");
            ArrayList<Area> areas = getAreas(jsonFile);
            System.out.println(areas);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Area>();
    }

    private static ArrayList<Area> getAreas(JSONObject jsonFile){
        ArrayList<Area> areas = new ArrayList<>();
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

    private static String getAreaAddressedTo(){
        String addressedTo = "";
        return addressedTo;
    }

    private static ArrayList<TrashContainer> getWeekCalendar(JSONObject areaJson){
        ArrayList<TrashContainer> week = new ArrayList<>();
        if(areaJson.has("weekCalendar")){
            JSONArray weekCalendarJson = areaJson.getJSONArray("weekCalendar");
            for (Object w: weekCalendarJson) {
                JSONObject weekJson = (JSONObject) w;
                TrashContainer trashContainer = new TrashContainer();
                trashContainer.setDay(weekJson.getString("type"));
                trashContainer.setType(TrashType.valueOf(weekJson.getString("type")));
                week.add(trashContainer);
            }
        }
        return week;
    }
}
