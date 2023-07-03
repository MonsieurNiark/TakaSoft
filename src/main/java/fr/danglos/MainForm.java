package fr.danglos;
import com.fazecast.jSerialComm.SerialPort;
import fr.danglos.arduino.GainEnum;
import fr.danglos.arduino.IntegrationEnum;
import fr.danglos.arduino.SensorBean;
import fr.danglos.arduino.SerialComm;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainForm extends JFrame {
    private JComboBox<GainEnum> gainCombobox;
    private JComboBox<IntegrationEnum> integrationCombobox;
    private JComboBox<SerialPort> portsCom;
    private JButton startButton;

    public MainForm() {
        setTitle("Main Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création des composants
        gainCombobox = new JComboBox<>(GainEnum.values());
        integrationCombobox = new JComboBox<>(IntegrationEnum.values());
        portsCom = new JComboBox<>(SerialPort.getCommPorts());
        portsCom.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(Objects.toString(value.getSystemPortName(), ""));
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            return label;
        });
        startButton = new JButton("Start");

        // Configuration du layout
        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(new JLabel("Gain:"));
        formPanel.add(gainCombobox);
        formPanel.add(new JLabel("Integration:"));
        formPanel.add(integrationCombobox);
        formPanel.add(new JLabel("COM:"));
        formPanel.add(portsCom);
        formPanel.add(new JLabel(""));
        formPanel.add(startButton);

        // Ajout des composants à la fenêtre
        add(formPanel, BorderLayout.CENTER);

        // Personnalisation des composants
        Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        gainCombobox.setFont(labelFont);
        integrationCombobox.setFont(labelFont);
        portsCom.setFont(labelFont);
        startButton.setFont(buttonFont);

        // Gestion des événements
        startButton.addActionListener(e -> {
            SensorBean sensorBean = SensorBean.getInstance();

           // dispose(); // Fermer la fenêtre principale
            SecondForm secondForm = new SecondForm(); // Créer la deuxième fenêtre
            SerialComm serialComm =  new SerialComm(sensorBean, secondForm);
            serialComm.setPortCom((SerialPort)portsCom.getSelectedItem());


            secondForm.setSerialComm(serialComm);
            secondForm.showFullScreen(); // Afficher la deuxième fenêtre en plein écran


            serialComm.run();
        });

        // Affichage de la fenêtre principale
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm();
            }
        });
    }
}


