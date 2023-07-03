package fr.danglos;

import fr.danglos.arduino.SerialComm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SecondForm extends JFrame {
    private JPanel colorPanel;
    private SerialComm serialComm;

    public SecondForm() {
        setTitle("Second Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuration du layout
        setLayout(new BorderLayout());
        colorPanel = new JPanel();
        add(colorPanel, BorderLayout.CENTER);

        // Gestion de l'événement de pression de touche pour quitter le plein écran
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(serialComm != null && serialComm.getSerialPort() != null && serialComm.getSerialPort().isOpen()){
                    serialComm.getSerialPort().closePort();
                    serialComm.stopThread();
                }
                dispose(); // Fermer la fenêtre
            }
        });

        // Affichage de la fenêtre en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void setColor(Color color) {
        colorPanel.setBackground(color);
    }

    public void showFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setVisible(true);
        }

        // Changement de couleur du panneau
        colorPanel.setBackground(Color.GREEN);
    }
    
    public void setSerialComm(SerialComm serialComm){
        this.serialComm = serialComm;
    }
}