package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<Node>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0,0);
    private boolean canJump = true;

    private int levelWidth;

    private void initContent() {
        Rectangle bg = new Rectangle(1280, 720);

        levelWidth = LevelData.LEVEL1[0].length() * 60; // 60 is blocksize

        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++){
                switch(line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i*60, 60, 60, Color.BROWN);
                        platforms.add(platform);
                        break;
                }
            }
        }

        player = createEntity(0, 600, 40, 40, Color.BLUE);

        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if(offset > 640 && offset < levelWidth - 640){
                gameRoot.setLayoutX(-(offset - 640)); //collision met linkermuur
            }
        });
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update() {
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth- 5) {
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;
    }

    private void movePlayerY(int value) {}

    private void jumpPlayer() {
        if (canJump){
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

//    private Node createEntity(int x, int y, int w, int h, Color color) {}

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initContent();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Hangul game");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    public static void main(String[] args){ launch(args); }
}
