module org.example.javafxapv {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens org.example.javafxapv to javafx.fxml;
    exports org.example.javafxapv;
}