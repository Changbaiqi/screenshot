package com.screenshot.cbq;

//import com.github.kwhat.jnativehook.GlobalScreen;
//import com.github.kwhat.jnativehook.NativeHookException;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.screenshot.cbq.controller.ImageFactoryView;
import com.screenshot.cbq.event.GlobalKeyListenerExample;
import com.screenshot.cbq.model.ImageModel;
import com.screenshot.cbq.utils.ScreenShotUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.logging.LogManager;

public class MainApplication extends Application {

    public static final int FIRST_SHORTCUT = 1; //开启监听
    public static final int SECOND_SHORTCUT = 2; //关闭监听
    public static HotkeyListener hotkeyListener = null;



    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("controller/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 500);
        stage.setResizable(false);
        stage.setTitle("ScreenShot");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e->{ System.exit(0);});
        while(true){
            JIntellitype.getInstance().registerHotKey(FIRST_SHORTCUT, 0, 116);
            hotkeyListener = code -> {
                System.out.println("测试：" + code);
                switch (code) {
                    case 1 -> {
                        System.out.println("测试成功");

                        Platform.runLater(() -> {
                            ImageModel png = ScreenShotUtils.fullCameraScreen("E:", "CBQ" + (long) (Math.random() * 99999999999L), "png");
                            ImageFactoryView.init(png);
                        });
                    }
                    default -> System.out.println("未知按键：" + code);
                }
            };
            JIntellitype.getInstance().addHotKeyListener(hotkeyListener);
        }
//
//        while(true){
//
//
//        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
