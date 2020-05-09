package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Answer extends GridPane {
    private String answer;
    private String side;
    private Text text;
    private static Map<String, String> allQuestions = new HashMap<>();
    private static ArrayList<Answer> buttons;
    private static ArrayList<String> answers = new ArrayList<>();
    private ImageView buttonbubble;
    private Image buttonImg;
    private int tick;
    private AnimationTimer timer;
    private boolean timerIsRunning;

    public Answer(String answer, int x, int y, String side){
        this.answer = answer;
        this.side = side;

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        buttonImg = new Image("sample/resources/img/buttonbubble.png");
        buttonbubble = new ImageView(buttonImg);
        buttonbubble.setFitWidth(192);
        buttonbubble.setFitHeight(120);

        text = new Text(answer);
        text.setWrappingWidth(buttonbubble.getFitWidth());
        text.setTextAlignment(TextAlignment.CENTER);
        getStyleClass().add("answer");

        this.setTranslateY(y-30);
        if(side == "left"){
            this.setTranslateX(x + 20);
        }
        else{
            this.setTranslateX(x - 20);
        }

        this.getChildren().add(buttonbubble);
        this.getChildren().add(text);
    }

    public static void updateAnswers(String correctAnswer){
        allQuestions = Question.getLevelQuestions();

        buttons = Main.getButtons();

        int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        buttons.get(randomNum).setAnswer(correctAnswer);

        ArrayList<String> newAnswerList = new ArrayList<>();

        Random random = new Random();
        Object[] values = allQuestions.values().toArray();   //vervangen met questionsleft?
        for (int i = 0; i < 4; i++) {
            if(i != randomNum){
                String newAnswer = (String) values[random.nextInt(values.length)];
                if(newAnswerList.contains(newAnswer) || newAnswer == correctAnswer){
                    i--; }
                else{
                    newAnswerList.add(newAnswer);
                    buttons.get(i).setAnswer(newAnswer);
                    answers.add(newAnswer);
                }
            }
        }
//        System.out.println("Questions left: " + questionsLeft);
//        System.out.println("All questions: " + allQuestions);
        answers.clear();
    }

    public void biggerAnswer(){
        if(Main.getCurrentLevel() == 5) {
            buttonImg = new Image("sample/resources/img/buttonbubble1.png");
            buttonbubble.setFitWidth(211);

            if(side == "left"){
                this.setTranslateX(this.getTranslateX() - 25);
            }
            else{
                this.setTranslateX(this.getTranslateX() - 3);
            }
            text.setTranslateX(text.getTranslateX()+10);
        }

        if(Main.getCurrentLevel() == 6){
            buttonImg = new Image("sample/resources/img/buttonbubble2.png");
            buttonbubble.setFitWidth(453);
            buttonbubble.setFitHeight(72);


            buttons.get(0).setTranslateY(buttons.get(0).getTranslateY() - 6);
            buttons.get(0).setTranslateX(23);

            for (int i = 0; i < buttons.size(); i++) {
                if(i != 0){
                    buttons.get(i).setTranslateY(buttons.get(0).getTranslateY() + 70 * i);
                    buttons.get(i).setTranslateX(buttons.get(0).getTranslateX() + 4);
                }
            }
//            buttons.get(0).setTranslateX(buttons.get(0).getTranslateX() + 4);
            text.setWrappingWidth(buttonbubble.getFitWidth());
        }
        buttonbubble.setImage(buttonImg);
    }

    public void setButtonToRed(){
        if(timerIsRunning)return;

        Image buttonImgRed;
        if(buttons.get(0).getWidth() < 200)
            buttonImgRed = new Image("sample/resources/img/buttonbubble-red.png");
        else if(buttons.get(0).getWidth() > 200 && buttons.get(0).getWidth() < 300)
            buttonImgRed = new Image("sample/resources/img/buttonbubble1-red.png");
        else
            buttonImgRed = new Image("sample/resources/img/buttonbubble2-red.png");
        buttonbubble.setImage(buttonImgRed);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tick++;
                if(tick >= 20){
                    buttonbubble.setImage(buttonImg);
                    tick = 0;
                    timer.stop();
                    timerIsRunning = false;
                }
            }
        };
        timer.start();
        timerIsRunning = true;
    }

    public void setButtonToGreen(){
        Image buttonImgGreen;
        if(buttons.get(0).getWidth() < 200)
            buttonImgGreen = new Image("sample/resources/img/buttonbubble-green.png");
        else if(buttons.get(0).getWidth() > 200 && buttons.get(0).getWidth() < 300)
            buttonImgGreen = new Image("sample/resources/img/buttonbubble1-green.png");
        else
            buttonImgGreen = new Image("sample/resources/img/buttonbubble2-green.png");
        buttonbubble.setImage(buttonImgGreen);
    }

    public void setButtonToWhite(){
        buttonbubble.setImage(buttonImg);
        if(timerIsRunning){
            timer.stop();
            timerIsRunning = false;
        }
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
        int length = answer.length();

        if(length == 1) text.setStyle("-fx-font: 80 'SF Pixelate';");
        else if(length == 2 || length == 3) text.setStyle("-fx-font: 70 'SF Pixelate';");
        else if(length == 4) text.setStyle("-fx-font: 55 'SF Pixelate';");
        else if(length >= 5 && length < 7) text.setStyle("-fx-font: 45 'SF Pixelate';");
        else if(length == 7) text.setStyle("-fx-font: 38 'SF Pixelate';");
        else text.setStyle("-fx-font: 33 'SF Pixelate';");

        text.setText("");
        text.setText(answer);
    }
}
