package veronicadev.ecobot;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class EcoBot {
    private static String URL;
    private static String BOT_TOKEN;
    private static ArrayList<Area> areas;
    public static void main(String[] args) throws IOException {
        URL = getVar("URL");
        BOT_TOKEN = getVar("BOT_TOKEN");
        areas = getAreas();
    }

    private static String getVar(String var){
        return System.getenv(var);
    }

    private static ArrayList<Area> getAreas() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        Elements areasToElaborate = doc.select(".fusion-one-full:not(:nth-last-of-type(2))");
        for (Element a : areasToElaborate) {
            Elements titleNodes = a.select("p:first-of-type strong");
            if(titleNodes!=null){
                Area area = new Area();
                area.setName(titleNodes.text());
                area.setStreets(getAreaStreets(a));
                area.setAddressedTo(getAreaAddressedTo(a));
                area.setWeekCalendar(getWeekCalendar(a));
                System.out.println(area.getName());
                System.out.println(area.getStreets());
                System.out.println(area.getAddressedTo());
            }
        }
        return new ArrayList<Area>();
    }

    private static String getAreaStreets(Element element){
        String streets = "";
        Elements streetsNodes = element.select("p:nth-of-type(2)");
        if(streetsNodes!=null){
            streets = streetsNodes.text();
        }
        return streets;
    }

    private static String getAreaAddressedTo(Element element){
        String addressedTo = "";
        Elements streetsNodes = element.select("p:nth-of-type(3) strong");
        if(streetsNodes!=null){
            addressedTo = streetsNodes.text();
        }
        return addressedTo;
    }

    private static ArrayList<TrashContainer> getWeekCalendar(Element element){
        ArrayList<TrashContainer> week = new ArrayList<>();
        return week;
    }
}
