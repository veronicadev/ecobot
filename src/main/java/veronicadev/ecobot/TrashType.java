package veronicadev.ecobot;

public enum TrashType {
    ORGANICO("ORGANICO"),
    INDIFFERENZIATO("INDIFFERENZIATO"),
    PLASTICA_LATTINE("PLASTICA_LATTINE"),
    CARTA("CARTA"),
    VERDE("VERDE");

    private String type;
    TrashType(String type) { this.type = type; }

}
