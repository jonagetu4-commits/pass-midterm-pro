module com.example.java3_mid_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.java3_mid_project to javafx.fxml;
    exports com.example.java3_mid_project;
}