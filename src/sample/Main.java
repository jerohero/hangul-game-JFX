package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.LevelData;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> enemies = new ArrayList<Node>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;
    private int blockSize;

    private boolean dialogEvent = false, running = true;

    private void initContent() {
        Image bgImg = new Image("sample/resources/img/bg.jpg");
        ImageView bg = new ImageView(bgImg);

        blockSize = 60;
        levelWidth = LevelData.LEVEL1[0].length() * blockSize;

        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*blockSize, i*blockSize, 60, 60, "sample/resources/img/floortile.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        Node enemy = createEntity(j*blockSize, i*blockSize-(133/2+7), 93, 133, "sample/resources/img/enemy1.png");
                        enemies.add(enemy);
                        break;
                    case '3':
                        player = createEntity(j*blockSize, i*blockSize, 40, 40, "sample/resources/img/enemy1.png");
                        break;
                }
            }
        }

//        player.translateXProperty().addListener((obs, old, newValue) -> {
//            int offset = newValue.intValue();
//
//            if (offset > 640 && offset < levelWidth - 640) {
//                gameRoot.setLayoutX(-(offset - 640));
//            }
//        });

        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update() {
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(5);
        }

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY());

        for (Node enemy : enemies) {
            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                enemy.getProperties().put("alive", false);
                dialogEvent = true;
                running = false;
            }
        }

        for (Iterator<Node> it = enemies.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {       //collision met platform
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {             //collides rechts
                            return;                                                                //kan niet meer bewegen
                        }
                    }
                    else {                                                                      //height player
                        if (player.getTranslateX() == platform.getTranslateX() + blockSize) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + blockSize) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    private Node createEntity(int x, int y, int w, int h, String path) {
        Image img = new Image(path);
        ImageView entity = new ImageView(img);
        entity.setFitHeight(h);
        entity.setFitWidth(w);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.getProperties().put("alive", true);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
                if (running) {
                    update();
                }

                if (dialogEvent) {
                    dialogEvent = false;
                    keys.keySet().forEach(key -> keys.put(key, false));

                    GameDialog dialog = new GameDialog();
                    dialog.setOnCloseRequest(event -> {
                        if (dialog.isCorrect()) {
                            System.out.println("Correct");
                        }
                        else {
                            System.out.println("Wrong");
                        }

                        running = true;
                    });
                    dialog.open();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}