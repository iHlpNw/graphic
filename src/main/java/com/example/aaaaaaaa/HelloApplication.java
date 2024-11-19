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
    private int detailU = 60; // Количество точек по оси u
    private int detailV = 60; // Количество точек по оси v
    private double a = 100; // Параметр a
    private double b = 100; // Параметр b
    private double c = 40;  // Параметр c
    private double sPower = 1; // Степень для u
    private double tPower = 1; // Степень для v
    private double size = 1; // Масштабирование
    private double moveX = 300; // Смещение по X
    private double moveY = 300; // Смещение по Y
    private double moveZ = 0;   // Смещение по Z
    private double turnX = 30; // Поворот по X
    private double turnY = 30; // Поворот по Y
    private double turnZ = 0;  // Поворот по Z
    

    public void clear(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 1280, 720);
    }
    public void render3d(GraphicsContext gc){
        clear(gc);
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);

        double[][][] points = new double[detailU][detailV][3];

        // Вычисление точек супертороида
        double du = 2 * Math.PI / detailU;
        double dv = 2 * Math.PI / detailV;

        for (int i = 0; i < detailU; i++) {
            double u = i * du;
            double cu = cosPower(u, sPower);
            double su = sinPower(u, sPower);

            for (int j = 0; j < detailV; j++) {
                double v = j * dv;
                double cv = cosPower(v, tPower);
                double sv = sinPower(v, tPower);

                double x = (a + c * cu) * cv;
                double y = (b + c * cu) * sv;
                double z = c * su;

                // Применение вращения
                double[] rotated = rotate(x, y, z);
                points[i][j][0] = rotated[0] * size + moveX;
                points[i][j][1] = rotated[1] * size + moveY;
                points[i][j][2] = rotated[2] * size + moveZ;
            }
        }

        // Рисование линий
        for (int i = 0; i < detailU; i++) {
            for (int j = 0; j < detailV; j++) {
                int nextI = (i + 1) % detailU;
                int nextJ = (j + 1) % detailV;

                // Соединяем горизонтальные и вертикальные линии
                drawLine(gc, points[i][j], points[nextI][j]);
                drawLine(gc, points[i][j], points[i][nextJ]);
            }
        }
    }

    private double cosPower(double w, double m) {
        return Math.signum(Math.cos(w)) * Math.pow(Math.abs(Math.cos(w)), m);
    }


    private double sinPower(double w, double m) {
        return Math.signum(Math.sin(w)) * Math.pow(Math.abs(Math.sin(w)), m);
    }

    // Функция вращения точки
    private double[] rotate(double x, double y, double z) {
        double sina = Math.sin(Math.toRadians(turnX));
        double sinb = Math.sin(Math.toRadians(turnY));
        double sinc = Math.sin(Math.toRadians(turnZ));
        double cosa = Math.cos(Math.toRadians(turnX));
        double cosb = Math.cos(Math.toRadians(turnY));
        double cosc = Math.cos(Math.toRadians(turnZ));

        double nx = x * cosb * cosc + y * (sina * sinb * cosc - cosa * sinc) + z * (cosa * sinb * cosc + sinc * sina);
        double ny = x * cosb * sinc + y * (cosa * cosc + sina * sinb * sinc) + z * (z * sinb * sinc - sina * cosc);
        double nz = -x * sinb + y * cosb * sina + z * cosb * cosa;
        return new double[]{nx, ny, nz};
    }
    private void drawLine(GraphicsContext gc, double[] p1, double[] p2) {
        gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1280, 720);
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        TextField turnx = new TextField(String.valueOf(turnX));
        TextField turny = new TextField(String.valueOf(turnY));
        TextField turnz = new TextField(String.valueOf(turnZ));
        TextField movex = new TextField(String.valueOf(moveX));
        TextField movey = new TextField(String.valueOf(moveY));
        TextField movez = new TextField(String.valueOf(moveZ));
        TextField sizeText = new TextField(String.valueOf(size));
        Label tx = new Label("turn X");
        Label ty = new Label("turn Y");
        Label tz = new Label("turn Z");
        Label mx = new Label("move X");
        Label my = new Label("move Y");
        Label mz = new Label("move Z");
        Label sl = new Label("size");

        tx.setLayoutX(400);
        tx.setLayoutY(620);
        ty.setLayoutX(450);
        ty.setLayoutY(620);
        tz.setLayoutX(500);
        tz.setLayoutY(620);
        mx.setLayoutX(550);
        mx.setLayoutY(620);
        my.setLayoutX(600);
        my.setLayoutY(620);
        mz.setLayoutX(650);
        mz.setLayoutY(620);
        sl.setLayoutX(700);
        sl.setLayoutY(620);

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
                size = Double.parseDouble(sizeText.getText());
                moveX = Double.parseDouble(movex.getText());
                moveY = Double.parseDouble(movey.getText());
                moveZ = Double.parseDouble(movez.getText());
                render3d(gc);
            }
        });
        root.getChildren().add(canvas);
        root.getChildren().addAll(
                btn1, turnx, turny, turnz, movex, movey, movez, sizeText,
                tx, ty, tz, mx, my, mz, sl
                );
        render3d(gc);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}