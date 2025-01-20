module org.clinique {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires java.sql;

    opens org.clinique to javafx.fxml,javafx.base;
    exports org.clinique;
    exports org.clinique.dao;
    opens org.clinique.dao to javafx.base, javafx.fxml;
    exports org.clinique.metier;
    opens org.clinique.metier to javafx.base, javafx.fxml;
    exports org.clinique.controller;
    opens org.clinique.controller to javafx.base, javafx.fxml;
    exports org.clinique.database;
    opens org.clinique.database to javafx.base, javafx.fxml;
}
