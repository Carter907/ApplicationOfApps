module org.cartapp {
    requires javafx.controls;
    requires java.base;
    requires java.sql;

    exports org.cartapp to javafx.controls;
    opens org.cartapp;
}