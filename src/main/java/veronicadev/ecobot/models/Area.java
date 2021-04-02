package veronicadev.ecobot.models;

import java.util.List;

public class Area {
    private String name;
    private String streets;
    private String addressedTo;

    private List<AreaCalendar> areaCalendar;

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

    public List<AreaCalendar> getWeekCalendar() {
        return areaCalendar;
    }

    public void setWeekCalendar(List<AreaCalendar> areaCalendar) {
        this.areaCalendar = areaCalendar;
    }
}
