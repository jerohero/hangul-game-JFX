package sample;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> enemies = new ArrayList<Node>();
    private ArrayList<Answer> buttons = new ArrayList<Answer>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private ImageView player;
    private ImageView enemy;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private Sprite sprite;

    private int levelWidth;
    private int blockSize;

    private int playerWidth;
    private int playerHeight;
    private int playerX;
    private int playerY;

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
                        enemy = createSprite(j*blockSize, i*blockSize-(133/2+45), 120, 172, "sample/resources/img/enemy1.png");
                        enemies.add(enemy);
                        break;
                    case '3':
                        playerWidth = 76; playerHeight = 126;
                        player = createSprite(j*blockSize, i*blockSize-(126/2+4), playerWidth, playerHeight, "sample/resources/img/player.png");
                        break;
                    case '4':
                        String hangul = "동";
                        Question question = new Question(hangul, j*blockSize, i*blockSize-180);
                        gameRoot.getChildren().add(question);
                        break;
                    case '6':
                        Answer button1 = new Answer("g", j*blockSize - 23, i*blockSize);
                        uiRoot.getChildren().add(button1);
                        buttons.add(button1);
                        break;
                    case '7':
                        Answer button2 = new Answer("j", j*blockSize - 20, i*blockSize);
                        uiRoot.getChildren().add(button2);
                        buttons.add(button2);
                        break;
                    case '8':
                        Answer button3 = new Answer("b", j*blockSize - 23, i*blockSize);
                        uiRoot.getChildren().add(button3);
                        buttons.add(button3);
                        break;
                    case '9':
                        Answer button4 = new Answer("ch", j*blockSize - 20, i*blockSize);
                        uiRoot.getChildren().add(button4);
                        buttons.add(button4);
                        break;
                }
            }
        }

        for (Answer button : buttons){
            button.setOnMouseClicked(event -> {
//                System.out.println(button.getAnswer());

            });
        }

        appRoot.setStyle("-fx-background-color: #e3d7bf");
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void update() {
        globaltick++;
        animationtick++;

        sprite.spriteAnimation(player, animationtick, "player");
        sprite.spriteAnimation(enemy, animationtick, "enemy");

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

    private ImageView createSprite(int x, int y, int w, int h, String path){
        Sprite spriteObj = new Sprite(x, y, w, h, path);
        ImageView sprite = spriteObj.createSprite();
        gameRoot.getChildren().add(sprite);
        return sprite;
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
        primaryStage.setTitle("한글");
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                }

//                if (dialogEvent) {
//                    dialogEvent = false;
//                    keys.keySet().forEach(key -> keys.put(key, false));
//
//                    GameDialog dialog = new GameDialog();
//                    dialog.setOnCloseRequest(event -> {
//                        if (dialog.isCorrect()) {
//                            System.out.println("Correct");
//                        }
//                        else {
//                            System.out.println("Wrong");
//                        }
//
//                        running = true;
//                    });
//                    dialog.open();
//                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}