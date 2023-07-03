package fr.danglos.arduino;

public enum IntegrationEnum {

    TCS34725_INTEGRATIONTIME_2_4MS("2.4ms", 22),
    TCS34725_INTEGRATIONTIME_24MS("24ms", 23),
    TCS34725_INTEGRATIONTIME_50MS("50ms", 24),
    TCS34725_INTEGRATIONTIME_60MS("60ms", 25),
    TCS34725_INTEGRATIONTIME_101MS("101ms", 26),
    TCS34725_INTEGRATIONTIME_120MS("120ms", 27),
    TCS34725_INTEGRATIONTIME_154MS("154ms", 28),
    TCS34725_INTEGRATIONTIME_180MS("180ms", 29),
    TCS34725_INTEGRATIONTIME_199MS("199ms", 32),
    TCS34725_INTEGRATIONTIME_240MS("240ms", 33),
    TCS34725_INTEGRATIONTIME_300MS("300ms", 34),
    TCS34725_INTEGRATIONTIME_360MS("360ms", 35),
    TCS34725_INTEGRATIONTIME_401MS("401ms", 36),
    TCS34725_INTEGRATIONTIME_420MS("420ms", 37),
    TCS34725_INTEGRATIONTIME_480MS("480ms", 38),
    TCS34725_INTEGRATIONTIME_499MS("499ms", 39),
    TCS34725_INTEGRATIONTIME_540MS("540ms", 42),
    TCS34725_INTEGRATIONTIME_600MS("600ms", 44),
    TCS34725_INTEGRATIONTIME_614MS("614ms", 46);

    private String libelle;
    private int code;

    IntegrationEnum(String libelle, int code) {
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
