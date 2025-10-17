module book.tracker.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires okhttp3;

    opens booktracker to javafx.fxml;
    exports booktracker;
}
