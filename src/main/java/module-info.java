module com.example.littleproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.littleproject to javafx.fxml;
    exports com.example.littleproject;
}