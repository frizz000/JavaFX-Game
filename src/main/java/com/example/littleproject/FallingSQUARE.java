package com.example.littleproject;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingSQUARE extends Application {

    int missed;
    int passed;
    AnimationTimer timer;
    int fallingSquare;
    double speed;
    List fall = new ArrayList<>();
    Pane root = new Pane();
    Label lblScore = new Label("Current score: " + (int) (((double) passed / (double) (missed + passed)) * 100) + "%");
    Scene scene = new Scene(root, 450, 600);
    Timeline winTimeline;
    Timeline timeline;
    Button btnStart;

    @Override
    public void start(Stage stage) throws IOException {

        root.setStyle("-fx-background-color: rgba(255,153,0,0.95);");

        lblScore.setLayoutX(50);
        lblScore.setLayoutY(50);
        lblScore.setStyle("-fx-font-size: 30px;");
        lblScore.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        lblScore.setTextFill(Color.BLACK);
        lblScore.setCenterShape(true);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: rgb(255,255,255);");
        grid.setLayoutX(0);
        grid.setLayoutY(540);
        grid.setPrefSize(450, 60);
        grid.setAlignment(Pos.CENTER);
        grid.add(lblScore, 0, 0);
        root.getChildren().add(grid);

        passed = 1;
        missed = 0;
        fallingSquare = 1200;
        speed = 2;

        timeline = new Timeline(new KeyFrame(Duration.millis(fallingSquare), event -> {
            fall.add(square());
            root.getChildren().add(((Node) fall.get(fall.size() - 1)));
            if (speed > 4) {
                ((Rectangle) fall.get(fall.size() - 1)).setFill(Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
            }
        }));

        timeline.setCycleCount(1000);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameUpdate();
            }
        };

        timer.start();

        winTimeline = new Timeline(new KeyFrame(Duration.seconds(60), event -> {
            miniWindow("Win");
            timer.stop();
            timeline.stop();
        }));

        btnStart = new Button("Start");
        btnStart.setLayoutX(190);
        btnStart.setLayoutY(200);
        btnStart.setStyle("-fx-font-size: 20px;");
        root.getChildren().add(btnStart);
        btnStart.setOnAction(e -> {
            root.getChildren().remove(btnStart);
            timeline.play();
            winTimeline.play();
        });

        stage.setTitle("FallingSQUARE!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public Rectangle square() {
        Rectangle square = new Rectangle();
        square.setWidth(50);
        square.setHeight(50);
        square.setFill(Color.BLUE);
        square.setLayoutX(Math.random() * 400);
        square.setLayoutY(0);
        square.setOnMouseClicked(e -> {
            passed++;
            square.setVisible(false);
            fall.remove(square);
            speed += 0.15;
        });
        return square;
    }

    public void gameUpdate() {
        lblScore.setText("Current score: " + (int) (((double) passed / (double) (missed + passed)) * 100) + "%");

        for (int i = 0; i < fall.size(); i++) {
            Rectangle square = (Rectangle) fall.get(i);
            square.setLayoutY(square.getLayoutY() + speed);
            if (square.getLayoutY() > 490) {
                missed++;
                fall.remove(square);
                root.getChildren().remove(square);
            }
            if ((int) (((double) passed / (double) (missed + passed)) * 100) < 50) {
                miniWindow("Lose");
            }
        }
    }

    public void miniWindow(String name) {

        Label lbl = new Label("You lose!");
        lbl.setLayoutX(70);
        lbl.setLayoutY(20);

        Label lbl2 = new Label("You survive 60 seconds!" +
                "\nYou passed " + passed + ", missed " + missed);

        Button btn = new Button("Restart");
        btn.setLayoutX(90);
        btn.setLayoutY(50);
        btn.setOnAction(e -> {
            try {
                restart();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        if (name.equals("Lose")) {
            lbl.setVisible(true);
            lbl2.setVisible(false);
            timer.stop();
            timeline.stop();
            winTimeline.stop();
            speed = 0;
        } else {
            lbl2.setVisible(true);
            lbl.setVisible(false);
            winTimeline.stop();
            timer.stop();
            timeline.stop();
            speed = 0;
        }

        Pane secondaryLayout = new Pane();
        lbl.setLayoutX(70);
        lbl.setLayoutY(20);
        lbl2.setLayoutX(40);
        lbl2.setLayoutY(10);
        secondaryLayout.getChildren().add(lbl);
        secondaryLayout.getChildren().add(lbl2);
        secondaryLayout.getChildren().add(btn);

        Scene window = new Scene(secondaryLayout, 200, 100);

        Stage miniWindow = new Stage();
        Button ok = new Button("Ok");
        ok.setLayoutX(80);
        ok.setLayoutY(50);
        secondaryLayout.getChildren().add(ok);
        ok.setOnAction(d -> {
            System.exit(0);
        });

        miniWindow.setTitle(name);
        miniWindow.setScene(window);
        miniWindow.initModality(Modality.WINDOW_MODAL);
        miniWindow.initOwner(scene.getWindow());

        miniWindow.show();
        miniWindow.setResizable(false);
    }

    public void restart() throws IOException {
        Stage stage = (Stage) scene.getWindow();
        speed = 2;
        passed = 1;
        missed = 0;
        winTimeline.play();
        timeline.play();
        stage.close();
        start(stage);
        for (int i = 0; i < fall.size(); i++) {
            root.getChildren().remove(fall.get(i));
        }
        btnStart.setVisible(false);
    }


    public static void main(String[] args) {
        launch();
    }
}