package com.example.java3_mid_project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Statement stmt;
    @FXML
    private ComboBox<String> user_pick_cb;

    private final String[] box_items = {"Order total", "Order details", "Names and cities", "Employee look up"};

    @FXML
    private Label welcomeText;

    @FXML
    private TextField user_input_tf;

    @FXML
    protected void onHelloButtonClick() throws SQLException {
        String passStr = user_input_tf.getText();
        if (user_pick_cb.getValue().equals("Order total")) {
            welcomeText.setText("Your total: $" + getOderTotal(passStr));
        }
        else if (user_pick_cb.getValue().equals("Order details")) {
            welcomeText.setText(getOrderDetails(passStr));
        }
        else if (user_pick_cb.getValue().equals("Names and cities")) {
            welcomeText.setText(getNamesAndCity(passStr));
        }
        else if (user_pick_cb.getValue().equals("Employee look up")) {
            welcomeText.setText(getEmployeeLookUp(passStr));
        }
    }

    public String getOderTotal(String passStr) throws SQLException { //done
        String order_total = "";
        double total = 0;
        ResultSet rs = stmt.executeQuery("SELECT UnitPrice * Quantity + ((UnitPrice * Quantity) * Discount)\n" +
                "FROM `order details`\n" +
                "WHERE OrderID = " + passStr);
        while (rs.next()) {
            total += rs.getDouble(1);
                    //rs.getDouble(1) * rs.getDouble(2) +((rs.getDouble(1) * rs.getDouble(2)) *rs.getDouble(3));
        }

        order_total = String.format("%.2f", total);
        return order_total;
    }

    public String getOrderDetails(String passStr) throws SQLException { //to be worked on
        String order_details = "";
        ResultSet rs = null;
        //problem here
        rs = stmt.executeQuery("SELECT OrderDate, Freight FROM `orders` WHERE OrderID =" + "'" + passStr + "'");
        System.out.println("query executed");
        while (rs.next()) {
            order_details = "Order date: " + rs.getString(1) + " Freight charge: " + rs.getString(2) + "\n";
        }
        System.out.println("info passed to string");


        //this works :)
        rs = stmt.executeQuery("SELECT ProductID, UnitPrice, Quantity, Discount\n" +
                "FROM `order details`\n" +
                        "WHERE OrderID = " + passStr);

        while (rs.next()) {
            order_details = order_details + "ProductID: " +rs.getString(1) + " Price: "+rs.getString(2) +
                    " Amount: " + rs.getString(3) + " Discount: " + rs.getString(4) +"\n";
        }

        return order_details;
    }

    public String getNamesAndCity(String passStr) throws SQLException {
        String AC = "";
        ResultSet rs = null;

        rs = stmt.executeQuery("SELECT ContactName, City\n" +
                "FROM `customers`\n" +
                "WHERE Country = 'USA' AND Region = " + "'" + passStr+"'");
        while (rs.next()) {
            AC = AC + "Name: " + rs.getString(1) + " City: " + rs.getString(2) + "\n";
        }

        return AC;
    }

    public String getEmployeeLookUp(String passStr) throws SQLException {
        String employeeLookUp = "";
        ResultSet rs = null;

        rs = stmt.executeQuery("SELECT LastName, FirstName\n" +
                "FROM `employees`\n" +
                "WHERE BirthDate LIKE '"+ passStr +"%'\n" +
                "ORDER BY LastName ASC");
        while (rs.next()) {
            employeeLookUp = employeeLookUp + "LastName: " + rs.getString(1) + " First Name: "+ rs.getString(2) + "\n";
        }


        return  employeeLookUp;
    }


    private void initializeDB() {
        try {
            // Load the JDBC driver
            //Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            // Establish a connection
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/northwind", "root", "password");
            System.out.println("Database connected");

            // Create a statement
            stmt = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user_pick_cb.getItems().addAll(box_items);
        initializeDB();
    }
}