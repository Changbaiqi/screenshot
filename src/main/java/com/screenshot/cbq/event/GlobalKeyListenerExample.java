package com.screenshot.cbq.event;
//import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
//import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListenerExample implements NativeKeyListener {

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
//        if(nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F5){
//            System.out.println("截图");
//
//            GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//            int width = defaultScreenDevice.getDisplayMode().getWidth();
//            int height = defaultScreenDevice.getDisplayMode().getHeight();
//
//            Platform.runLater(()->{
//                ImageModel png = ScreenShotUtils.fullCameraScreen("E:", "CBQ" + (long) (Math.random() * 99999999999L), "png");
//                ImageFactoryView.init(png);
//
//            });
//
//        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
