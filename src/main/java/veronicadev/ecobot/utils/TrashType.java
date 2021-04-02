package veronicadev.ecobot.utils;

public enum TrashType {
    ORGANICO("ORGANICO", "Organico"),
    INDIFFERENZIATO("INDIFFERENZIATO", "Indifferenziato"),
    PLASTICA_LATTINE("PLASTICA_LATTINE", "Plastica/Lattine"),
    CARTA("CARTA", "Carta"),
    VERDE("VERDE", "Verde"),
    VETRO("VETRO", "Vetro");

    private String type;
    private String name;
    TrashType(String type, String name) { this.type = type; this.name = name; }

    public String getName() {
        return name;
    }
}
