module com.screenshot.cbq {
    
    requires javafx.controls;
    requires javafx.fxml;

//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires com.almasb.fxgl.all;
//    requires lombok;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.desktop;
//    requires com.github.kwhat.jnativehook;
    requires java.logging;
    requires jintellitype;
//    requires jnativehook;
//    requires java.desktop;

    opens com.screenshot.cbq to javafx.fxml;
    exports com.screenshot.cbq;
    exports com.screenshot.cbq.controller;
    opens com.screenshot.cbq.controller to javafx.fxml;
}