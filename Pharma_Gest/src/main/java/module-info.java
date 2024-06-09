module com.example.pharma_gest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;

    opens com.example.pharma_gest to javafx.fxml;
    exports com.example.pharma_gest;
}

