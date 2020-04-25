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
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;

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

    private int playerWidth;
    private int playerHeight;

    private boolean dialogEvent = false, running = true;

    private int globaltick;
    private int animationtick;
    private int second;

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
                        Node platform = createSprite(j*blockSize, i*blockSize, 60, 60, "sample/resources/img/floortile.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        Node enemy = createSprite(j*blockSize, i*blockSize-(133/2+45), 120, 172, "sample/resources/img/enemy1.png");
                        enemies.add(enemy);
                        break;
                    case '3':
                        playerWidth = 76; playerHeight = 126;
                        player = createSprite(j*blockSize, i*blockSize-(126/2+4), playerWidth, playerHeight, "sample/resources/img/player.png");
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

    private void playerAnimation(int second){
        if(animationtick == 60){
            changeSpriteImg(player, "sample/resources/img/playerv2.png");
        }
        if(animationtick == 120){
            changeSpriteImg(player, "sample/resources/img/player.png");
            animationtick = 0;
        }
    }

    private void update() {
        //        if(tick % 60 == 0){
//            second++;
//        }
        globaltick++;
        animationtick++;
        playerAnimation(animationtick);

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-5);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(5);
        }

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

    private void changeSpriteImg(Node sprite, String path){
        gameRoot.getChildren().removeAll(player);
        player = createSprite((int)player.getTranslateX(), (int)player.getTranslateY(), playerWidth, playerHeight, path);
        gameRoot.getChildren().add(player);
    }

    private Node createSprite(int x, int y, int w, int h, String path) {
        Image img = new Image(path);
        ImageView sprite = new ImageView(img);
        sprite.setFitHeight(h);
        sprite.setFitWidth(w);
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
        sprite.getProperties().put("alive", true);

        gameRoot.getChildren().add(sprite);
        return sprite;
    }

    private Pane getGameRoot(){
        return gameRoot;
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