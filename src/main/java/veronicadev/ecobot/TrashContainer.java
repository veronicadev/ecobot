package veronicadev.ecobot;

public class TrashContainer {
    private String day;
    private TrashType type;
    private String hoursRange;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public TrashType getType() {
        return type;
    }

    public void setType(TrashType type) {
        this.type = type;
    }

    public String getHoursRange() {
        return hoursRange;
    }

    public void setHoursRange(String hoursRange) {
        this.hoursRange = hoursRange;
    }
}
