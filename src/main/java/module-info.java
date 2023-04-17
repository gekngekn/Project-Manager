module com.gekn.productivityapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens com.gekn.productivityapp to javafx.fxml;
    exports com.gekn.productivityapp;
    exports com.gekn.productivityapp.models;
    exports com.gekn.productivityapp.interfaces;
}