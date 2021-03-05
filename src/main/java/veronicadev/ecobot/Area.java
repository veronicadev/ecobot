package veronicadev.ecobot;

import java.util.ArrayList;

public class Area {
    private String name;
    private String streets;
    private String addressedTo;
    private ArrayList<TrashContainer> weekCalendar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreets() {
        return streets;
    }

    public void setStreets(String streets) {
        this.streets = streets;
    }

    public String getAddressedTo() {
        return addressedTo;
    }

    public void setAddressedTo(String addressedTo) {
        this.addressedTo = addressedTo;
    }

    public ArrayList<TrashContainer> getWeekCalendar() {
        return weekCalendar;
    }

    public void setWeekCalendar(ArrayList<TrashContainer> weekCalendar) {
        this.weekCalendar = weekCalendar;
    }
}
