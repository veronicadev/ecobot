package veronicadev.ecobot.models;

public class TrashContainer {
    private String type;
    private String hoursRange;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHoursRange() {
        if(hoursRange==null){
            return "";
        }
        return hoursRange;
    }

    public void setHoursRange(String hoursRange) {
        this.hoursRange = hoursRange;
    }
}
