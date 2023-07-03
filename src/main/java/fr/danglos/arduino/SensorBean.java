package fr.danglos.arduino;


import java.io.Serializable;

public class SensorBean implements Serializable {
    /**
     * UID of class
     */
    private static final long serialVersionUID = -5067790505985875022L;
    /**
     * Singleton instance
     */
    private static SensorBean instance;
    /**
     * Temperature Value
     */
    private String temperature;
    /**
     * Lux Value
     */
    private String lux;
    private String red;
    private String green;
    private String blue;
    private String clear;
    private String html;


    /**
     * Model for temperature values
     */
    public SensorBean() {
        lux = "0";
        temperature = "0";
        red = "0";
        green = "0";
        blue = "0";
        clear = "0";
        html = "#ffffff";
    }

    public static SensorBean getInstance() {
        if (instance == null) {
            instance = new SensorBean();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "SensorBean{" +

                ", red='" + red + '\'' +
                ", green='" + green + '\'' +
                ", blue='" + blue + '\'' +
                ", html='" + html + '\'' +
                '}';
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLux() {
        return lux;
    }

    public void setLux(String lux) {
        this.lux = lux;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getGreen() {
        return green;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public String getClear() {
        return clear;
    }

    public void setClear(String clear) {
        this.clear = clear;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
