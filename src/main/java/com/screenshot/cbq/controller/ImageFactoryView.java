package com.screenshot.cbq.controller;

import com.screenshot.cbq.model.ImageModel;
import com.screenshot.cbq.utils.CopyImageToClipboardUtils;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageFactoryView extends Stage {

    private int selectSwitch = 0;//框选开关

    private double startX =0;
    private double startY=0;
    private double endX =0; //最后移动X坐标
    private double endY =0; //最后移动Y坐标

    //选择窗口
    ImageView selectView = null;

    public ImageFactoryView(){
        initStyle(StageStyle.TRANSPARENT);
        setFullScreenExitHint("");
        setTitle("ImageView");
        setFullScreen(true);
        setAlwaysOnTop(true);

    }

    public static void init(ImageModel imageModel){

        //高清截图
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //加载图像
        Image image = new Image("file:/"+imageModel.getAllPath());
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setFitHeight(screenSize.getHeight());
        imageView.setFitWidth(screenSize.getWidth());

        //创建透明矩形
        Rectangle transparentRect = new Rectangle(100,100,200,150);
        transparentRect.setFill(Color.TRANSPARENT);




        //创建灰色背景
        Rectangle grayBackGround = new Rectangle(screenSize.getWidth(),screenSize.getHeight());
        grayBackGround.setFill(Color.rgb(128,128,128,0.5));

        //混合模式
        imageView.setBlendMode(BlendMode.SRC_OVER);




        ImageFactoryView imageFactoryView= new ImageFactoryView();
        imageFactoryView.setWidth(1);
        imageFactoryView.setHeight(1);



        //创建面板
        Pane stackPane = new Pane();
        stackPane.getChildren().addAll(grayBackGround,imageView,transparentRect);


        //创建场景
        Scene scene = new Scene(stackPane,350,200);

        imageFactoryView.setScene(scene);
        imageFactoryView.show();

        //ESC设置为退出
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()== KeyCode.ESCAPE)
                    imageFactoryView.close();
            }
        });

        //鼠标点击
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //鼠标左键点击
                if(mouseEvent.getButton()==MouseButton.PRIMARY){
                    imageFactoryView.selectSwitch = 1;
                    imageFactoryView.startX = mouseEvent.getSceneX();
                    imageFactoryView.startY = mouseEvent.getSceneY();
                    System.out.println("鼠标左点击");
                }
                //鼠标右键点击
                if(mouseEvent.isSecondaryButtonDown()){
                    System.out.println("鼠标右点击");
                }
            }
        });

        //移动就触发
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if(mouseEvent.getButton()==MouseButton.PRIMARY && imageFactoryView.selectSwitch==1) {

                    imageFactoryView.endX = mouseEvent.getSceneX();
                    imageFactoryView.endY = mouseEvent.getSceneY();
                    Platform.runLater(()->{
                        //框选矩形
                        Rectangle rectangle = new Rectangle(imageFactoryView.startX,imageFactoryView.startY,imageFactoryView.endX-imageFactoryView.startX,imageFactoryView.endY-imageFactoryView.startY);//,,宽,高
                        rectangle.setFill(Color.TRANSPARENT);
                        rectangle.setStroke(Color.RED);
                        rectangle.setStrokeWidth(3);

                        //框选图片
                        double pointX = imageFactoryView.startX;
                        double pointY = imageFactoryView.startY;
                        double w = imageFactoryView.endX-imageFactoryView.startX;
                        double h = imageFactoryView.endY-imageFactoryView.startY;
                        //这里截图采用四舍五入（防抖）
                        int imageW =(int)Math.round(image.getWidth()/imageView.getFitWidth()*w);
                        int imageH = (int)Math.round(image.getHeight()/imageView.getFitHeight()*h);
                        WritableImage croppedImage = new WritableImage(image.getPixelReader(),
                                (int)Math.round(image.getWidth()/imageView.getFitWidth()*pointX),
                                (int)Math.round(image.getHeight()/imageView.getFitHeight()*pointY),
                                imageW>0?imageW:1,
                                imageH>0?imageH:1
                                );

                        //框选部分图片
                        imageFactoryView.selectView = new ImageView(croppedImage);

                        imageFactoryView.selectView.setFitWidth(w);
                        imageFactoryView.selectView.setFitHeight(h);



                        imageFactoryView.selectView.setX(pointX);
                        imageFactoryView.selectView.setY(pointY);

                        stackPane.getChildren().clear();
                        stackPane.getChildren().addAll(imageView,grayBackGround,rectangle,imageFactoryView.selectView);
                    });

                }
            }
        });

        //放开鼠标
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()== MouseButton.PRIMARY && imageFactoryView.selectSwitch==1) {
                    System.out.println("鼠标左放开");
                    System.out.println("起始X坐标："+imageFactoryView.startX+"，起始Y坐标："+imageFactoryView.startY+"\n终点X坐标："+imageFactoryView.endX+"终点Y坐标："+imageFactoryView.endY);
                    imageFactoryView.selectSwitch = 0;
//
                    HBox pane = new HBox();
                    pane.setLayoutX(imageFactoryView.endX-300);
                    pane.setLayoutY(imageFactoryView.endY+5);
                    pane.setPrefHeight(40);
                    pane.setPrefWidth(300);
                    pane.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));

                    Button button1 = new Button();
                    button1.setPrefHeight(40);
                    button1.setPrefWidth(50);
                    button1.setStyle("""
                            -fx-background-color: #FAFAFA;
                            -fx-border-width: 0;
                            """);
                    button1.setText("保存");
                    button1.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            FileChooser chooser = new FileChooser();
                            chooser.setInitialDirectory(new File("C:\\Users"));
                            chooser.setTitle("请选择保存的位置");
                            //设置默认文件名和拓展名
                            chooser.setInitialFileName("CBQ"+(int)(Math.random()*999999999)+".png");
                            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG(*.png)",".png");
                            chooser.getExtensionFilters().add(extensionFilter);

                            //显示对话框
                            File file = chooser.showSaveDialog(imageFactoryView);
                            try {
//                                imageFactoryView.selectView.snapshot();
                                imageFactoryView.selectView.getViewport();
                                ImageIO.write(SwingFXUtils.fromFXImage(imageFactoryView.selectView.getImage(),null),"png",file);
                                System.out.println("保存成功");
                            } catch (IOException e) {
                                System.out.println("保存失败");
                                throw new RuntimeException(e);
                            }

                        }
                    });

                    //复制按钮
                    Button button2 = new Button();
                    button2.setPrefHeight(40);
                    button2.setPrefWidth(50);
                    button2.setStyle("""
                            -fx-background-color: #FAFAFA;
                            -fx-border-width: 0;
                            """);
                    button2.setText("复制");
                    button2.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageFactoryView.selectView.getImage(), new BufferedImage((int)imageFactoryView.selectView.getImage().getWidth(),(int)imageFactoryView.selectView.getImage().getHeight(),BufferedImage.TYPE_INT_RGB));
                            CopyImageToClipboardUtils.copy(bufferedImage);
                            imageFactoryView.close();
                        }
                    });

                    pane.getChildren().add(button1);
                    pane.getChildren().add(button2);
                    stackPane.getChildren().add(pane);
                }
            }
        });



        //鼠标移动事件
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(imageFactoryView.selectSwitch==1) {
                    double sceneX = mouseEvent.getSceneX();
                    double sceneY = mouseEvent.getSceneY();
                }
            }
        });
    }
}
