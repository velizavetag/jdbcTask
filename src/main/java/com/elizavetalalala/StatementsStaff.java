package com.elizavetalalala;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

import static com.elizavetalalala.config.AppProperties.getProperties;

public class StatementsStaff {

    private static final String URL = getProperties().getProperty("url");
    private static final String USERNAME = getProperties().getProperty("username");
    private static final String PASSWORD = getProperties().getProperty("password");
    private static final String USERNAME1 = getProperties().getProperty("username1");
    private static final String PASSWORD1= getProperties().getProperty("password1");
    private static final String SELECT_ALL = "SELECT id, Country, city, date_start, date_end, date_time_inserted FROM TRAVELLINGS WHERE id = ? ";
    private static final String QUERY_WHERE_DATE = "SELECT id, Country, city, date_start, date_end, date_time_inserted FROM TRAVELLINGS WHERE DATE_START BETWEEN ? AND ? ";
    private static final String UPDATE_WHERE_CITY = "UPDATE TRAVELLINGS SET DATE_START = ? where City = ? ";
    private static final String INSERT_VALUES = "insert into TRAVELLINGS (id, country, city, date_start, date_end, photo, date_time_inserted) " +
            " values(?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_WHERE_ID = "DELETE FROM TRAVELLINGS WHERE id = ?";
    private static final String SELECT_PHOTO = "SELECT photo FROM TRAVELLINGS WHERE id = ? ";
    private static final String QUERY_EMPLOYEES_DEPARTMENTS = "select first_name, last_name, department_id, department_name\n" +
            "from employees \n" +
            "left join departments using(department_id)";
    private static final String EXECUTE_PROCEDURE = "{call proc_duplicate(?,?)}";

    public static void query(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL)) {

            stmt.setInt(1, id);

            for (ResultSet rs = stmt.executeQuery(); rs.next(); ) {
                String result = "id: " + rs.getInt("id") +
                        "\tCountry: " + rs.getString("Country") +
                        "\tCity: " + rs.getString("City") + "\n" +
                        "The travel period was FROM\t " + rs.getDate("date_start") +
                        "\tTILL: \t " + rs.getDate("date_end")
                        + "\nthe picture was taken : " + rs.getTimestamp("date_time_taken");
                System.out.println(result);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void select_photo_where_id(int id) {

        ImageIcon image;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(SELECT_PHOTO)) {
            stmt.setInt(1, id);
            for (ResultSet rs = stmt.executeQuery(); rs.next(); ) {

                Blob blob = rs.getBlob("photo");
                InputStream in = blob.getBinaryStream();
                try {
                    image = new ImageIcon(ImageIO.read(in));
                    Draw_photo.draw_picture(image);

                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void query_where_date_between(Date date1, Date date2) {

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(QUERY_WHERE_DATE)) {

            stmt.setDate(1, date1);
            stmt.setDate(2, date2);

            for (ResultSet rs = stmt.executeQuery(); rs.next(); ) {
                String result = "id: " + rs.getInt("id") +
                        "\tCountry: " + rs.getString("Country") +
                        "\tCity: " + rs.getString("City") + "\n" +
                        "The travel period was FROM\t " + rs.getDate("date_start") +
                        "\tTILL: \t " + rs.getDate("date_end")
                        + "\nthe picture was taken : " + rs.getTimestamp("date_time_taken");
                System.out.println(result);
                System.out.println("=====================================================");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void insertion() {
        Scanner scanner = new Scanner(System.in);

        //  Reading the user input
        System.out.println("id: ");
        int id = scanner.nextInt();
        System.out.println("country: ");
        String country = scanner.next();
        System.out.println("city: ");
        String city = scanner.next();
        System.out.println("start date: ");
        String string_date_start = scanner.next();
        Date date_start = Date.valueOf(string_date_start);
        System.out.println("end date: ");
        Date date_end = Date.valueOf(scanner.next());
        Timestamp date_time_taken = new Timestamp(System.currentTimeMillis());
        FileInputStream inputStream = null;
        System.out.println("provide the path to the image: ");
        String path = scanner.next();

        //setting up the parameters for the prepared statement
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(INSERT_VALUES)) {
            conn.setAutoCommit(false);
            // trying to load a file from the internet
            if (path.startsWith("http")) {
                BufferedInputStream in = new BufferedInputStream(new URL(path).openStream());
                stmt.setBinaryStream(6, in);
            } else {
                // reading locally from PC
                stmt.setBytes(6, readFile(path));
            }
//
            stmt.setInt(1, id);
            stmt.setString(2, country);
            stmt.setString(3, city);
            stmt.setDate(4, date_start);
            stmt.setDate(5, date_end);
            stmt.setTimestamp(7, date_time_taken);
            conn.commit();
            stmt.executeUpdate();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reading the file from pc
    private static byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1; ) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public static void update_where_city(String city) {

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(UPDATE_WHERE_CITY)) {
            System.out.println("What date would you like to set up?");
            Scanner scanner = new Scanner(System.in);
            Date date = Date.valueOf(scanner.next());
            conn.setAutoCommit(false);
            stmt.setDate(1, date);
            stmt.setString(2, city);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void delete_where_id(int id){
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(DELETE_WHERE_ID)) {

            conn.setAutoCommit(false);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void query_employees_departments () {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME1, PASSWORD1); PreparedStatement stmt = conn.prepareStatement(QUERY_EMPLOYEES_DEPARTMENTS)) {

            for (ResultSet rs = stmt.executeQuery(); rs.next(); ) {
                String result = "first name: " + rs.getString("first_name") +
                        "\tlast name: " + rs.getString("last_name") +
                        "\tdepartment id: " + rs.getInt("department_id") + "\n" +
                        "\tdepartment name: \t " + rs.getString("department_name");
                System.out.println(result);
                System.out.println("===============================================================");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void procedure (int first_number){
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); CallableStatement stmt = conn.prepareCall(EXECUTE_PROCEDURE)) {

            stmt.setInt(1, first_number);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();

            System.out.println(first_number + " * 2 = " + stmt.getInt(2));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


}
