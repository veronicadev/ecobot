package veronicadev.ecobot;

public enum TrashType {
    ORGANICO("organico"),
    INDIFFERENZIATO("indifferenziato"),
    PLASTICA_LATTINE("plastica lattine"),
    CARTA("carta"),
    VERDE("verde");

    private String type;
    TrashType(String type) { this.type = type; }

}
