package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private static ArrayList<Node> platforms = new ArrayList<Node>();
    private static ArrayList<Node> enemies = new ArrayList<Node>();
    private static ArrayList<Answer> buttons = new ArrayList<Answer>();

    private static ArrayList<Map> questions = new ArrayList<>();
    private static Map<String, String> levelQuestions;
    private static Map<String, String> incorrectAnswers = new HashMap<>();

    private static ArrayList<Map> allContent;

    private static Pane appRoot = new Pane();
    private static Pane gameRoot = new Pane();
    private static Pane uiRoot = new Pane();

    private static ImageView player;
    private static ImageView enemy;
    private static Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private static Sprite sprite;
    private static Question question;

    private static int levelWidth;
    private static int blockSize;

    private static int questionColumn;
    private static int questionRow;

    private static int playerWidth;
    private static int playerHeight;
    private static int playerX;
    private static int playerY;

    private static int gameWidth = 490;

    private static String correctAnswer;

    private static boolean dialogEvent = false, running = true;

    private static int globaltick;
    private static int animationtick;
    private static int second;

    private static int currentLevel = 0;
//    private static int currentLevel = 2;

    private static Score score;
    private static int playerScore;

    private static Map<String, String> enemyPaths;

    private static boolean enemyIdlePaused = false;

    private static Background background = new Background();

    private static void initContent() {
        ImageView bg = background.getBackground();

        blockSize = 60;
        levelWidth = GameLayout.GAME[0].length() * blockSize;

        for (int row = 0; row < GameLayout.GAME.length; row++) {
            String line = GameLayout.GAME[row];
            for (int column = 0; column < line.length(); column++) {
                switch (line.charAt(column)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createSprite(column*blockSize, row*blockSize, 60, 60, "sample/resources/img/floortile.png");
                        platforms.add(platform);
                        break;
                    case '2':
                        enemyPaths = Sprite.initEnemyPaths(1);
                        enemy = createSprite(column*blockSize, row*blockSize-(133/2+45), 120, 172, enemyPaths.get("default"));
                        enemies.add(enemy);
                        break;
                    case '3':
                        playerWidth = 120; playerHeight = 162;
                        player = createSprite(column*blockSize-(30), row*blockSize-(175/2+14), playerWidth, playerHeight, "sample/resources/img/player.png");
                        break;
                    case '4':
                        questionColumn = column;
                        questionRow = row;
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
                    case '5':
                        score = new Score(column * blockSize, row * blockSize);
                        uiRoot.getChildren().add(score);
                        break;
                }
            }
        }

        for (Answer button : buttons){
            button.setOnMouseClicked(event -> {
                System.out.println(button.getAnswer());
                if(question.isCorrect(button.getAnswer())){
                    //volgende vraag
                    answeredCorrectly(button);
                }
                else{
                    //takehit
                    answeredIncorrectly(button);
                }
            });
        }
        allContent = QuestionUtils.initializeQuestions();

        currentLevel = 3;
        nextStage();

        appRoot.setStyle("-fx-background-color: #e3d7bf");
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }

    private static void answeredCorrectly(Answer button){
        playerScore++;
        score.setPlayerScore(playerScore);
        background.fountainAnimation();
        button.setButtonToGreen();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setDisable(true);
        }

        Task<Void> sleeper = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                try{
                    Thread.sleep(500);
                }
                catch(InterruptedException e){}
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                for (int i = 0; i < buttons.size(); i++) {
                    buttons.get(i).setDisable(false);
                }
                Sprite.enemyTalk(enemy, animationtick);
                question.updateQuestion(currentLevel);
                Answer.updateAnswers(question.getCorrectAnswer());
                System.out.println("Answered:" + questions);
                for (int j = 0; j < 4 ; j++) {
                    buttons.get(j).setButtonToWhite();
                }
            }
        });
        new Thread(sleeper).start();
    }

    private static void answeredIncorrectly(Answer button){
        //take damage > give enemy health
        //damage animation
        //add to incorrectly answered list
        score.setMaxScore(score.getMaxScore() + 1);
        System.out.println(score.getMaxScore());
        playerScore--;
        score.setPlayerScore(playerScore);
        Sprite.spriteHit(enemy, animationtick, "enemy");
        correctAnswer = question.getCorrectAnswer();
        String hangul = Utilities.getKeyByValue(levelQuestions, correctAnswer);
        incorrectAnswers.put(hangul, question.getCorrectAnswer());
        button.setButtonToRed();
        System.out.println("All incorrect answered: " + incorrectAnswers);
    }

    public static void nextStage(){
        currentLevel++;
//        currentLevel = 2;
        Question.resetResetCounter();
        Question.clearQuestionsLeft();
        levelQuestions = QuestionUtils.getQuestions(currentLevel);
        question.updateQuestion(currentLevel);
        Answer.updateAnswers(question.getCorrectAnswer());
        score.setMaxScore(levelQuestions.size());

        if(currentLevel > 1){
//            enemyPaths = Sprite.initEnemyPaths(currentLevel);
//            updateEnemyPaths();

            Sprite.newEnemy(enemy);
            question.hideBriefly();
//          if(currentLevel >= 4){
            if(currentLevel >= 1){
                System.out.println(currentLevel);
                question.biggerQuestion();
                for (int i = 0; i < buttons.size() ; i++) {
                    buttons.get(i).biggerAnswer();
                }
            }
        }
    }

    private static void update() {
        globaltick++;
        animationtick++;

        Sprite.spriteIdle(player, animationtick, "player");
        if(!enemyIdlePaused) Sprite.spriteIdle(enemy, animationtick, "enemy");
    }

    private static ImageView createSprite(int x, int y, int w, int h, String path){
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

        Scene scene = new Scene(appRoot, gameWidth, 700);
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

    public static Map<String, String> getEnemyPaths(){
        return enemyPaths;
    }

    public static void updateEnemyPaths(){
        Map<String, String> newEnemyPaths = Sprite.initEnemyPaths(currentLevel);
        newEnemyPaths = enemyPaths;
    }

    public static int getCurrentLevel(){return currentLevel;}

    public static int getGameWidth(){return gameWidth;}

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

    public static void setPlayerScore(int newPlayerScore){
        playerScore = newPlayerScore;
    }
}