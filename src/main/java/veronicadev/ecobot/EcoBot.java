package veronicadev.ecobot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EcoBot {
    private static String BOT_TOKEN;
    private static ArrayList<Area> areas;
    private static String municipalityName;
    private static TelegramBot bot;
    public static void main(String[] args) throws IOException {

        /*GET AREAS DATA*/
        areas = getAreas();

        BOT_TOKEN = getVar("BOT_TOKEN");
        bot = new TelegramBot(BOT_TOKEN);
        System.out.println("** EcoBot init **");

    }

    private static String getVar(String var){
        return System.getenv(var);
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
