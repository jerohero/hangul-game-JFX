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
        System.out.println("All questions (Answer): " + allQuestions);

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

    public void setButtonToRed(){
        if(timerIsRunning)return;
        Image buttonImgRed = new Image("sample/resources/img/buttonbubble-red.png");
        buttonbubble.setImage(buttonImgRed);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tick++;
                if(tick >= 60){
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
        if(answer.length() >= 4){
            text.setStyle("-fx-font: 60 'SF Pixelate';");
        }
        else{
            text.setStyle("-fx-font: 80 'SF Pixelate';");
        }
        text.setText("");
        text.setText(answer);
    }
}
