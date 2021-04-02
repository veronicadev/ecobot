package veronicadev.ecobot.models;

import java.util.List;

public class AreaCalendar {
    private String day;
    private List<TrashContainer> containers;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<TrashContainer> getContainers() {
        return containers;
    }

    public void setContainers(List<TrashContainer> containers) {
        this.containers = containers;
    }
}
