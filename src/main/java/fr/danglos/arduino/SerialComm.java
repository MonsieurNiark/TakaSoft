package fr.danglos.arduino;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import fr.danglos.SecondForm;

import javax.swing.*;
import java.awt.*;

public class SerialComm {

    private String couleurOld = "#ffffff";
    private String couleurNew;

    /** Port data is coming from */
    private SerialPort commPort;
    private Thread serialThread;

    private long lastTimeDataReceived;

    private boolean dataRecieved = false;

    /** model of the view */
    private volatile  SensorBean bean;

    /** String buffer for serial data */
    private final StringBuilder data;

    private final SecondForm secondForm;

    private Thread transitionThread;

    Timer colortimer;

    /**
     * Start looking for serial data
     *
     * @param tempBean   - Model of the view
     * @param secondForm
     */
    public SerialComm(SensorBean tempBean, SecondForm secondForm)
    {
        /* Initialize attributes */
        bean = tempBean;
        data = new StringBuilder();
        this.secondForm = secondForm;
        //serialThread = getSerialThread();
        //serialThread.start();

    }

    public void run()
    {
        serialThread = getSerialThread();
        serialThread.start();
        transitionThread = getTransitionThread();
        transitionThread.start();
        System.out.println("afte run");
    }


    private Thread getTransitionThread() {
        return  new Thread(() -> {
            while (true){

                couleurNew = bean.getHtml();

                int steps = 15;
                int stepCount = 0;
                while (stepCount / steps < 1){
                    stepCount++;
                    System.out.println(stepCount);
                    showColor(blendColors(
                            Color.decode(couleurOld),
                            Color.decode(couleurNew),
                            (float) stepCount / steps
                    ));

                    if(stepCount / steps >= 1){
                        couleurOld = couleurNew;
                    }

                    try{
                        Thread.sleep(8);
                    }catch (Exception e){

                    }
                }
            }
        });
    }


    private static Color blendColors(Color color1, Color color2, float ratio) {
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        System.out.println(ratio);
        return new Color(r, g, b);
    }

    private void showColor(Color color) {
        secondForm.setColor(color);
    }

    private Thread getSerialThread()
    {
        return new Thread(() ->
        {
            System.out.println("run");
            findCorrectSerialPort(commPort.getSystemPortName());
            openSerialPort();

            /* Add listener to port */
            commPort.addDataListener(new SerialPortDataListener()
            {
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

                public void serialEvent(SerialPortEvent event)
                {
                    dataRecieved = true;
                    /* Data not available return */
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    {
                        return;
                    }

                    /* Read serial bytes from communication port */
                    byte[] newData = new byte[16];
                    commPort.readBytes(newData, newData.length);

                    for (byte newDatum : newData)
                    {
                        lastTimeDataReceived = System.currentTimeMillis();

                        /* convert byte to char */
                        Character serialInput = (char) newDatum;

                        if(Character.isLetterOrDigit(serialInput))
                        {
                            data.append(serialInput);
                        }

                        /* Indicates the end of a value */
                        if (serialInput.equals('E'))
                        {
                            try {
                                parseData(data.toString());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            data.setLength(0);
                            break;
                        }
                    }
                }
            });

        });
    }

    /**
     * @param serialData - String sent by controller
     */
    private void parseData(String serialData) throws InterruptedException {
        //System.out.println("parse " + serialData);
        if (serialData.contains("Temp")) {
            bean.setTemperature(serialData.replaceAll("[^0-9]", ""));
        } else if (serialData.contains("Lux")) {
            bean.setLux(serialData.replaceAll("[^0-9]", ""));
        } else if (serialData.contains("Red")) {
            bean.setRed(serialData.replaceAll("[^0-9]", ""));
        } else if (serialData.contains("Green")) {
            bean.setGreen(serialData.replaceAll("[^0-9]", ""));
        } else if (serialData.contains("Blue")) {
            bean.setBlue(serialData.replaceAll("[^0-9]", ""));
        } else if (serialData.contains("Clear")) {
            bean.setClear(serialData.replaceAll("[^0-9]", ""));

            String hexRed = Integer.toHexString(Integer.parseInt((bean.getRed())));
            String hexGreen = Integer.toHexString(Integer.parseInt((bean.getGreen())));
            String hexBlue = Integer.toHexString(Integer.parseInt((bean.getBlue())));

            if (hexRed.length() == 1) hexRed = hexRed.concat("0");
            if (hexGreen.length() == 1) hexGreen = hexGreen.concat("0");
            if (hexBlue.length() == 1) hexBlue = hexBlue.concat("0");

            if (hexRed.length() > 2) hexRed = hexRed.substring(0,2);
            if (hexGreen.length() > 2) hexGreen = hexGreen.substring(0,2);
            if (hexBlue.length() > 2) hexBlue = hexBlue.substring(0,2);
            bean.setHtml("#" +
                    hexRed +
                    hexGreen +
                    hexBlue);
        }


    }



    /**
     * Continually try to connect to port till it is available
     *
     * @param portDescription string that is the port description
     */
    public void findCorrectSerialPort(String portDescription)
    {
        boolean found = false;
        long failures = 0;

        while(!found)
        {
            SerialPort[] ports = SerialPort.getCommPorts();
            for (SerialPort port : ports)
            {
                if (port.getSystemPortName().equals(portDescription))
                {
                    commPort = port;
                    found = true;
                }
            }

            if(!found)
            {
                failures += 1;

                if(failures % 100 == 0)
                {
                    System.out.println("search attempt for  \"" + portDescription + "\"" + " has failed " + failures + " times");
                }
            }
        }
    }

    /**
     * Will continue to try and open serial port until it is open
     * If any other device is talking to the device listed then this function will not exit
     */
    public void openSerialPort()
    {
        long failures = 0;
        while(!commPort.openPort())
        {
            failures +=1;

            if(failures % 100 == 0)
            {
                System.out.println("Open port failed " + failures + " times on " + commPort.getSystemPortName());
            }
        }
        System.out.println("port open");
        lastTimeDataReceived = System.currentTimeMillis();
        commPort.setBaudRate(9600);
    }

    public SerialPort getSerialPort() {
        return commPort;
    }

    public void setPortCom(SerialPort selectedItem) {
        this.commPort = selectedItem;
    }

    public void stopThread() {
        transitionThread.interrupt();
    }
}
