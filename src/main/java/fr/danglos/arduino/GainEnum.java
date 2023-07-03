package fr.danglos.arduino;

public enum GainEnum {

    TCS34725_GAIN_1X("1x", 52),
    TCS34725_GAIN_4X("4x", 53),
    TCS34725_GAIN_16X("16x", 54),
    TCS34725_GAIN_60X("60x", 55);

    private String libelle;
    private int code;

    GainEnum(String libelle, int code) {
        this.libelle = libelle;
        this.code = code;
    }


    @Override
    public String toString() {
        return libelle;
    }


    public int getCode() {
        return code;
    }
}
