package com.elizavetalalala;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.*;
import javax.swing.*;

import static com.elizavetalalala.config.AppProperties.getProperties;

public class Draw_photo {


    public static void draw_picture(final ImageIcon imageIcon) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageFrame frame = new ImageFrame(imageIcon);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}


    class ImageFrame extends JFrame {
        public int default_width;
        public int default_height;
        static ImageIcon icon;

        public ImageFrame(ImageIcon icon) {
            this.icon = icon;
            ImageComponent component = new ImageComponent(icon);
            if (component.getImage().getIconHeight() > component.getImage().getIconWidth()) {
                default_width = 500;
                default_height = 800;
            } else {
                default_width = 800;
                default_height = 500;
            }

            setTitle("ImageTest");
            setSize(default_width, default_height);

            add(component);
        }
    }


    class ImageComponent extends JComponent {
       static  ImageIcon image;

        public ImageComponent(ImageIcon image) {
            this.image = image;
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
//        g2d.rotate(Math.toRadians(270),image.getIconWidth(),image.getIconHeight()/3);
            image.paintIcon(this, g2d, 0, -0);
        }

        public static ImageIcon getImage() {
            return image;
        }
    }

