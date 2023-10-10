package com.screenshot.cbq.event;

public interface NativeKeyListener {
    void nativeKeyTyped(NativeKeyEvent nativeKeyEvent);

    void nativeKeyPressed(NativeKeyEvent nativeKeyEvent);

    void nativeKeyReleased(NativeKeyEvent nativeKeyEvent);
}
