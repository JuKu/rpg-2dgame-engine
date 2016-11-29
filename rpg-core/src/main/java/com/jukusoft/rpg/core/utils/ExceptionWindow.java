package com.jukusoft.rpg.core.utils;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Justin on 18.09.2016.
 */
public class ExceptionWindow extends JFrame {

    public ExceptionWindow(String title, String content) {
        this.setTitle(title);
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //focus error window
        this.setAlwaysOnTop(true);

        //create new panel and label with error text
        JPanel panel = new JPanel();
        panel.add(new JLabel(content));
        this.setContentPane(panel);

        //add window listener to allow close operation
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public static ExceptionWindow createAndWait (String title, String content) {
        ExceptionWindow window = new ExceptionWindow(title, content);

        //block this thread
        try {
            Thread.currentThread().wait();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return window;
    }

}
