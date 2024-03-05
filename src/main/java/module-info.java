module com.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires qrgen;
    requires itextpdf;
    requires java.desktop;
    requires java.mail;
    requires org.apache.poi.poi;
    requires javafx.media;
    requires javafx.web;
    requires twilio;

    opens com.example.demo4 to javafx.fxml;
    opens com.example.demo4.entities to javafx.base;


    exports com.example.demo4;
}