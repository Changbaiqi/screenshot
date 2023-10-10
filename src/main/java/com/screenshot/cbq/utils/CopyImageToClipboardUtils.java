package com.screenshot.cbq.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CopyImageToClipboardUtils {
    public static void copy(BufferedImage image){

        // 将图片转换为字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) image, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // 将字节数组放入剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) {
                if (isDataFlavorSupported(flavor)) {
                    return image;
                } else {
                    return null;
                }
            }
        };
        try {
            clipboard.setContents(transferable, null);
        }catch (Exception e){

        }
        System.out.println("图片已复制到剪切板！");
    }
}
