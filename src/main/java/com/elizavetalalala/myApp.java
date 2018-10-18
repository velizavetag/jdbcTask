package com.elizavetalalala;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.*;

import static com.elizavetalalala.config.AppProperties.getProperties;

public class myApp {

    public static void main(String[] args) {
//        StatementsStaff.select_photo_where_id(1);
//        StatementsStaff.query(1);
//        StatementsStaff.query_where_date_between(Date.valueOf("2018-04-18"), Date.valueOf("2018-09-28"));

//        StatementsStaff.insertion();
//        StatementsStaff.update_where_city("Dresden");
//         StatementsStaff.delete_where_id(7);
//        StatementsStaff.query_employees_departments();
        StatementsStaff.procedure(2);
    }



}
