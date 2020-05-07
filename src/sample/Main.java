package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> enemies = new ArrayList<Node>();
    private static ArrayList<Answer> buttons = new ArrayList<Answer>();

    private static ArrayList<Map> questions = new ArrayList<>();
    private static Map<String, String> levelQuestions;
    private static Map<String, String> incorrectAnswers = new HashMap<>();

    private static ArrayList<Map> allContent;

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private ImageView player;
    private ImageView enemy;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private Sprite sprite;
    private static Question question;

    private int levelWidth;
    private static int blockSize;

    private static int questionColumn;
    private static int questionRow;

    private int playerWidth;
    private int playerHeight;
    private int playerX;
    private int playerY;

    private String correctAnswer;

    private boolean dialogEvent = false, running = true;

    private int globaltick;
    private static int animationtick;
    private int second;

    private static int currentLevel = 1;

    private static boolean enemyIdlePaused = false;

    private void initContent() {
        Background background = new Background();
        ImageView bg = background.getBackground();

        blockSize = 60;
        levelWidth = LevelData.LEVEL1[0].length() * blockSize;

        for (int row = 0; row < LevelData.LEVEL1.length; row++) {
            String line = LevelData.LEVEL1[row];
            for (int column = 0; column < line.length(); column++) {
                switch (line.charAt(column)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createSprite(column*blockSize, row*blockSize, 60, 60, "sample/resources/img/floortile.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        enemy = createSprite(column*blockSize, row*blockSize-(133/2+45), 120, 172, "sample/resources/img/enemy1.png");
                        enemies.add(enemy);
                        break;
                    case '3':
                        playerWidth = 120; playerHeight = 162;
                        player = createSprite(column*blockSize-(30), row*blockSize-(175/2+14), playerWidth, playerHeight, "sample/resources/img/player.png");
                        break;
                    case '4':
                        questionColumn = column;
                        questionRow = row;
                        String hangul = "";
                        String romanized = "";

                        question = new Question();
                        gameRoot.getChildren().add(question);
                        break;
                }
            }
        }

        for (int row = 0; row < UIData.UI.length; row++) {
            String line = UIData.UI[row];
            for (int column = 0; column < line.length(); column++) {
                switch (line.charAt(column)) {
                    case '0':
                        break;
                    case '1':
                        Answer button1 = new Answer("g", column * blockSize - 23, row * blockSize, "left");
                        uiRoot.getChildren().add(button1);
                        buttons.add(button1);
                        break;
                    case '2':
                        Answer button2 = new Answer("ae", column * blockSize - 20, row * blockSize, "right");
                        uiRoot.getChildren().add(button2);
                        buttons.add(button2);
                        break;
                    case '3':
                        Answer button3 = new Answer("b", column * blockSize - 23, row * blockSize, "left");
                        uiRoot.getChildren().add(button3);
                        buttons.add(button3);
                        break;
                    case '4':
                        Answer button4 = new Answer("ch", column * blockSize - 20, row * blockSize, "right");
                        uiRoot.getChildren().add(button4);
                        buttons.add(button4);
                        break;
                }
            }
        }

        for (Answer button : buttons){
            button.setOnMouseClicked(event -> {
                System.out.println(button.getAnswer());
                if(question.isCorrect(button.getAnswer())){
                    //volgende vraag
                    answeredCorrectly();

                }
                else{
                    //takehit
                    answeredIncorrectly(button);
                }
            });
        }

        levelQuestions = QuestionUtils.getQuestions(currentLevel);
        allContent = QuestionUtils.initializeQuestions();
        question.updateQuestion(currentLevel);
        Answer.updateAnswers(question.getCorrectAnswer());

        appRoot.setStyle("-fx-background-color: #e3d7bf");
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private void answeredCorrectly(){
        int i = 0;
        for (Map questionAnswer : questions) {
            if(questionAnswer.values().contains(question.getCorrectAnswer())){
                Sprite.enemyTalk(enemy, animationtick);
                question.updateQuestion(currentLevel);
                Answer.updateAnswers(question.getCorrectAnswer());
//                while(questionAnswer.values().remove(answer));
                System.out.println("Answered:" + questions);
                for (int j = 0; j < 4 ; j++) {
                    buttons.get(j).setButtonToWhite();
                }
            }
            i++;
        }
    }

    private void answeredIncorrectly(Answer button){
        //take damage > give enemy health
        //damage animation
        //add to incorrectly answered list
        Sprite.spriteHit(enemy, animationtick, "enemy");
        correctAnswer = question.getCorrectAnswer();
        String hangul = Utilities.getKeyByValue(levelQuestions, correctAnswer);
        incorrectAnswers.put(hangul, question.getCorrectAnswer());
        button.setButtonToRed();
        System.out.println("All incorrect answered: " + incorrectAnswers);
    }

//    useless code that gets the clicked answer's map stuff
//    String hangul = Utilities.getKeyByValue(levelQuestions, answerGiven);
//        incorrectAnswers.put(hangul, answerGiven);
//        System.out.println(hangul + "  " + answerGiven);

    private void update() {
        globaltick++;
        animationtick++;

        Sprite.spriteIdle(player, animationtick, "player");
        if(!enemyIdlePaused) Sprite.spriteIdle(enemy, animationtick, "enemy");

//        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
//            movePlayerX(-5);
//        }
//
//        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
//            movePlayerX(5);
//        }

        for (Node enemy : enemies) {
            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                enemy.getProperties().put("alive", false);
                dialogEvent = true;
                running = false;   //redundant
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

//    private void movePlayerX(int value) {
//        boolean movingRight = value > 0;
//
//        for (int i = 0; i < Math.abs(value); i++) {
//            for (Node platform : platforms) {
//                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {       //collision met platform
//                    if (movingRight) {
//                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {             //collides rechts
//                            return;                                                                //kan niet meer bewegen
//                        }
//                    }
//                    else {                                                                      //height player
//                        if (player.getTranslateX() == platform.getTranslateX() + blockSize) {
//                            return;
//                        }
//                    }
//                }
//            }
//            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
//        }
//    }
//
//    private void movePlayerY(int value) {
//        boolean movingDown = value > 0;
//
//        for (int i = 0; i < Math.abs(value); i++) {
//            for (Node platform : platforms) {
//                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
//                    if (movingDown) {
//                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
//                            player.setTranslateY(player.getTranslateY() - 1);
//                            canJump = true;
//                            return;
//                        }
//                    }
//                    else {
//                        if (player.getTranslateY() == platform.getTranslateY() + blockSize) {
//                            return;
//                        }
//                    }
//                }
//            }
//            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
//        }
//    }

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

        Scene scene = new Scene(appRoot, 490, 700);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("한글");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static int getBlockSize(){
        return blockSize;
    }

    public static int getQuestionColumn(){
        return questionColumn;
    }
    public static int getQuestionRow(){
        return questionRow;
    }

//    public static ArrayList<Map> getQuestionsArray(){
//        return questions;
//    }
    public static void updateAskedQuestions(Map questionAnswer) {
        questions.clear();
        questions.add(questionAnswer);
    }

    public static ArrayList<Map> getAskedQuestions(){
        return questions;
    }
//
//    public static void setQuestionList(ArrayList<Map> newQuestions){
//        questions = newQuestions;
//    }

    public static void setAnimationTick(int newAnimationtick){
        animationtick = newAnimationtick;
    }

    public static int getAnimationTick(){
        return animationtick;
    }

    public static Map<String, String> getQuestionsArray(){
        return levelQuestions;
    }

    public static void setQuestionList(Map<String, String> newQuestions){
        levelQuestions = newQuestions;
    }

    public static Question getQuestion(){
        return question;
    }

    public static void setQuestion(Question newQuestion){
        question = newQuestion;
    }

    public static ArrayList<Answer> getButtons(){
        return buttons;
    }

    public static void pauseIdleEnemy(){
        enemyIdlePaused = true;
    }

    public static void unpauseIdleEnemy(){
        enemyIdlePaused = false;
    }

    public static Map<String, String> getIncorrectAnswers(){
        return incorrectAnswers;
    }

    public static void setIncorrectAnswers(Map<String, String> newIncorrectAnswers){
        incorrectAnswers = newIncorrectAnswers;
    }
}