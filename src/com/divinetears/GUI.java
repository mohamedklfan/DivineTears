package com.divinetears;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {


    public static int objectToCollectFrom;
    public static boolean isRunning = true;
    public static String interact;
    public static boolean fish = false;
    public static boolean nearestDeposit = false;

    public GUI() {
        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("DivineTears");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 300, 300, 240);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        String[] objectSelection = {"Mine Tears", "Chop Tears", "Fish Tears"};
        final JComboBox comboBox = new JComboBox<>(objectSelection);
        comboBox.setBounds(10, 50, 120, 27);
        contentPane.add(comboBox);
        comboBox.setVisible(true);
        final JComboBox godName = new JComboBox<>(Global.Obelisk.values());
        contentPane.add(godName).setBounds(170, 50, 120, 27);
        godName.setVisible(true);
        godName.setEnabled(false);
        final JLabel godLabel = new JLabel("Choose your god:");
        contentPane.add(godLabel).setBounds(170, 23, 120, 27);
        godLabel.setVisible(true);
        final JLabel label1 = new JLabel("Choose how to collect:");
        label1.setBounds(10, 23, 140, 27);
        contentPane.add(label1);
        label1.setVisible(true);
        final JCheckBox nearest = new JCheckBox("Get nearest deposit?");
        contentPane.add(nearest).setBounds(47, 110, 150, 27);
        nearest.setVisible(true);
        JButton btnStart = new JButton("Start");
        btnStart.setBounds(170, 180, 120, 27);
        contentPane.add(btnStart);


        nearest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nearest.isSelected()) {
                    comboBox.setEnabled(false);
                } else {
                    comboBox.setEnabled(true);
                }
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.isEnabled()) {
                    if (comboBox.getSelectedItem().equals("Mine Tears")) {
                        objectToCollectFrom = Global.ROCK;
                        interact = "Mine";
                    }
                    if (comboBox.getSelectedItem().equals("Chop Tears")) {
                        objectToCollectFrom = Global.FORMATION;
                        interact = "Chop down";
                    }
                    if (comboBox.getSelectedItem().equals("Fish Tears")) {
                        fish = true;
                    }
                }
                if (nearest.isSelected()) {
                    nearestDeposit = true;
                }

                dispose();
                isRunning = false;
            }
        });


    }
}
