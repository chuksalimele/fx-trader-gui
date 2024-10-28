/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 *
 * @author user
 */
public class Toast {
    
    public static void show(String message, int durationMillis, Component parent) {
        JWindow toastWindow = new JWindow();
        toastWindow.setLayout(new BorderLayout());

        // Wrapping the message in HTML tags
        JLabel label = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        toastWindow.add(label, BorderLayout.CENTER);

        // Pack the window to adjust size based on label's preferred size
        toastWindow.pack();

        // Position the toast at the bottom right corner of the parent component
        Point parentLocation = parent.getLocationOnScreen();
        Dimension parentSize = parent.getSize();
        int x = parentLocation.x + parentSize.width - toastWindow.getWidth() - 20;
        int y = parentLocation.y + parentSize.height - toastWindow.getHeight() - 20;
        toastWindow.setLocation(x, y);

        // Make the window transparent
        toastWindow.setBackground(new Color(0, 0, 0, 0));
        toastWindow.setOpacity(0.85f);

        // Show the toast
        toastWindow.setVisible(true);

        // Hide the toast after the specified duration
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toastWindow.setVisible(false);
                toastWindow.dispose();
            }
        }, durationMillis);
    }
    
}
