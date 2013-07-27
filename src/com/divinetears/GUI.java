package com.divinetears;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {


    public static String objectToCollectFrom;
    public static boolean isRunning = true;
    public static String interact;
    public static boolean fish = false;

    public GUI() {
        setResizable(true);
        setAlwaysOnTop(true);
        setTitle("DivineTears");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 300, 300, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        String[] objectSelection = {"Mine Tears", "Chop Tears", "Fish Tears"};
        final JComboBox comboBox = new JComboBox<>(objectSelection);
        comboBox.setBounds(10, 50, 120, 27);
        contentPane.add(comboBox);
        comboBox.setVisible(true);
        JButton btnStart = new JButton("Start");
        btnStart.setBounds(150, 225, 120, 27);
        contentPane.add(btnStart);


        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().equals("Mine Tears")) {
                    objectToCollectFrom = "Divine Tear Rocks";
                    interact = "Mine";
                }
                if (comboBox.getSelectedItem().equals("Chop Tears")) {
                    objectToCollectFrom = "Divine Tears Formation";
                    interact = "Chop down";
                }
                if (comboBox.getSelectedItem().equals("Fish Tears")) {
                    fish = true;
                }

                dispose();
                isRunning = false;
            }
        });


    }
}
