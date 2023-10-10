package com.screenshot.cbq.utils;

import com.screenshot.cbq.model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {
    private static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    public static ImageModel fullCameraScreen(String filePath, String fileName, String imageType){

        try {

            //高清截图
            GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = defaultScreenDevice.getDisplayMode().getWidth();
            int height = defaultScreenDevice.getDisplayMode().getHeight();

            Robot robot = new Robot(defaultScreenDevice);
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            MultiResolutionImage multiResolutionScreenCapture = robot.createMultiResolutionScreenCapture(rectangle);
            BufferedImage screenShot = (BufferedImage) multiResolutionScreenCapture.getResolutionVariant(width,height);

            File file = new File(filePath+"\\"+fileName+"."+imageType);

            ImageIO.write(screenShot,imageType,file);

            ImageModel imageModel = new ImageModel();

            imageModel.setName(fileName);
            imageModel.setFilePath(filePath);
            imageModel.setType(imageType);
            imageModel.setAllPath(file.getAbsolutePath());

            return imageModel;
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}
