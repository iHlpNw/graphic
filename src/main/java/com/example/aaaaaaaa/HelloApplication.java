package com.example.aaaaaaaa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.Arrays;

import java.io.IOException;

public class HelloApplication extends Application {
    private int A1 = 2;
    private int B1 = 2;
    private int R1 = 10; // R
    private int dl = 5;
    private double turnX = 15;
    private double turnY = 15;
    private double turnZ = 0;
    private int size = 1;
    private double moveX = 0;
    private double moveZ = 200;
    private double moveY = 700;
    private double getC(double w, double m){
        return(Math.signum(Math.cos(w))*Math.pow(Math.abs(Math.cos(w)), m));
    }
    private double getS(double w, double m){
        return(Math.signum(Math.sin(w))*Math.pow(Math.abs(Math.sin(w)), m));
    }

    public void clear(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 1280, 720);
    }
    public void render3d(GraphicsContext gc){
        clear(gc);
        gc.setFill(Color.BLACK);
        double A = A1;
        double B = B1;
        double R = R1;
        double z = 0;
        double y = 0;
        double x = 0;

        double[] circleUpX = new double[60];
        double[] circleUpY = new double[60];
        double[] circleUpZ = new double[60];

        double[] circleDownX = new double[60];
        double[] circleDownY = new double[60];
        double[] circleDownZ = new double[60];

        for(int l = 0; l < 4; l++){

            z = l;
            y = -R1;
            double alpha = 0;
            double dalpha = 2 * Math.PI / 60;

            for (int i = 0; i < 60; i++){
                //circleUpZ[i] = Math.signum(Math.sin())
                circleUpZ[i] = l * R1;
                circleUpY[i] = R1 * (1 + Math.abs(Math.sin(2 * alpha))) * Math.cos(alpha);
                circleUpX[i] = R1 * (1 + Math.abs(Math.sin(2 * alpha))) * Math.sin(alpha);
                alpha += dalpha;
            }

            y = R1;
            alpha = 0;

            for (int i = 0; i < 60; i++)
            {
                circleUpZ[i] = l * R1;
                circleUpY[i] = R1 * (1 + Math.abs(Math.sin(2 * alpha))) * Math.cos(alpha);
                circleUpX[i] = R1 * (1 + Math.abs(Math.sin(2 * alpha))) * Math.sin(alpha);
                alpha += dalpha;
            }

            for (int j = 0; j < 60; j++){
                double sina = Math.sin(turnX);
                double sinb = Math.sin(turnY);
                double sinc = Math.sin(turnZ);
                double cosa = Math.cos(turnX);
                double cosb = Math.cos(turnY);
                double cosc = Math.cos(turnZ);

                x = circleUpX[j];
                y = circleUpY[j];
                z = circleUpZ[j];

                circleUpX[j] = (x * cosb * cosc + y * sina * sinb * cosc - y * sinc * cosa + z * sinc * sina + z * sinb * cosa * cosc) * size * 10 + moveX;
                circleUpY[j] = ((x * sinc * cosb + y * cosa * cosc + y * sinb * sina * sinc - z * sina * cosc + z * sinb * sinc * cosa) * size * 10) + dl + moveY;
                circleUpZ[j] = (-x * sinb + y * cosb * sina + z * cosa * cosb) * size * 10 + moveZ; //263
            }
            if (l == 0){
                circleDownX = Arrays.copyOf(circleUpX, 59);
                circleDownY = Arrays.copyOf(circleUpY, 59);
                circleDownZ = Arrays.copyOf(circleUpZ, 59);
            }
            for (int q = 0; q < 59; q++){
                gc.strokeLine(circleUpY[q], circleUpZ[q], circleUpY[q + 1], circleUpZ[q + 1]);
            }
            gc.strokeLine(circleUpY[59], circleUpZ[59],circleUpY[0], circleUpZ[0]);
        }
        for (int i = 0; i < 59; i++){
            gc.strokeLine(circleDownY[i], circleDownZ[i], circleUpY[i], circleUpZ[i]);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1280, 720);
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        TextField turnx = new TextField("x");
        TextField turny = new TextField("y");
        TextField turnz = new TextField("z");
        TextField movex = new TextField("movex");
        TextField movey = new TextField("movey");
        TextField movez = new TextField("movez");
        TextField sizeText = new TextField("size");
        Button btn1 = new Button("press");
        btn1.setLayoutX(1280/2);
        btn1.setLayoutY(680);
        turnx.setLayoutX(400);
        turnx.setLayoutY(640);
        turny.setLayoutX(450);
        turny.setLayoutY(640);
        turnz.setLayoutX(500);
        turnz.setLayoutY(640);
        movex.setLayoutX(550);
        movex.setLayoutY(640);
        movey.setLayoutX(600);
        movey.setLayoutY(640);
        movez.setLayoutX(650);
        movez.setLayoutY(640);
        sizeText.setLayoutX(700);
        sizeText.setLayoutY(640);
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                turnX = Double.parseDouble(turnx.getText());
                turnY = Double.parseDouble(turny.getText());
                turnZ = Double.parseDouble(turnz.getText());
                size = Integer.parseInt(sizeText.getText());
                moveX = Double.parseDouble(movex.getText());
                moveY = Double.parseDouble(movey.getText());
                moveZ = Double.parseDouble(movez.getText());
                render3d(gc);
            }
        });
        root.getChildren().add(canvas);
        root.getChildren().addAll(btn1, turnx, turny, turnz, movex, movey, movez, sizeText);
        render3d(gc);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}