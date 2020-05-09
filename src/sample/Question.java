package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.crypto.spec.PSource;
import java.util.*;

public class Question extends Pane {
    private Map<String, String> questionAnswer = new HashMap<>();
    private String question;
    private String answer;
    private Text text;
    private static Map<String, String> levelQuestions;
    private static ArrayList<Map> askedQuestions;
    private static Map<String, String> questionsLeft = new HashMap<>();
    private static int resetcounter;
    private ImageView bubble;

    private AnimationTimer timer;
    private int tick;

    public Question(int x, int y){
        Image bubbleImg = new Image("sample/resources/img/textbubble0.png");
        bubble = new ImageView(bubbleImg);
        bubble.setFitWidth(140);
        bubble.setFitHeight(140);

        text = new Text();
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("questiontxt");
        text.setTranslateX(25);
        text.setTranslateY(90);

        this.getChildren().add(bubble);
        this.getChildren().add(text);

        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    public void biggerQuestion(){
        Image bubbleImg;
        if (Main.getCurrentLevel() == 5){
            bubbleImg = new Image("sample/resources/img/textbubble1.png");
            bubble.setFitWidth(180);

            text.setStyle("-fx-font: 70 'serif';");
            text.setTranslateY(text.getTranslateY() - 6);
            text.setTranslateX(text.getTranslateX()  - 2);

            this.setTranslateX(this.getTranslateX() - 20);
            bubble.setImage(bubbleImg);
        }
        if (Main.getCurrentLevel() == 6){
            bubbleImg = new Image("sample/resources/img/textbubble2.png");
            bubble.setFitWidth(270);

            text.setStyle("-fx-font: 65 'serif';");
            text.setTranslateX(text.getTranslateX()  + 15);
            text.setTranslateY(text.getTranslateY() - 8);

            this.setTranslateX(this.getTranslateX() - 95);
            bubble.setImage(bubbleImg);
        }
    }

    public void updateQuestion(){
        String newQuestion = "";
        String newAnswer = null;
        Map<String, String> incorrectAnswers;

        levelQuestions = QuestionUtils.getAllLevels().get(Main.getCurrentLevel()-1);
        if(Main.getCurrentLevel() > 3){
            System.out.println(levelQuestions);

            for (int i = 0; i < levelQuestions.size()/3; i++) {

                Object[] keys = levelQuestions.keySet().toArray();
                Object key = keys[new Random().nextInt(keys.length)];

                String hoi = levelQuestions.get(random.nextInt(levelQuestions.size()));
                System.out.println(hoi);
            }
            System.out.println(levelQuestions);
        }
        askedQuestions = Main.getAskedQuestions();
        if(questionsLeft.isEmpty()){
            questionsLeft.putAll(levelQuestions);
            resetcounter++;
        }

        if(!levelQuestions.isEmpty()){
            Random random = new Random();
            Object[] values;
            if (resetcounter == 1){
                System.out.println("Phase 1: All questions");
                values = questionsLeft.values().toArray();    // levelquestions of questionsleft
                newAnswer = (String) values[random.nextInt(values.length)];
                answer = newAnswer;
                newQuestion = Utilities.getKeyByValue(questionsLeft, newAnswer);
            }
            else if (resetcounter == 2){
                if(!Main.getIncorrectAnswers().isEmpty()){
                    System.out.println("Phase 2: Incorrect answers");
                    incorrectAnswers = Main.getIncorrectAnswers();
                    values = incorrectAnswers.values().toArray();
                    newAnswer = (String) values[random.nextInt(values.length)];

                    answer = newAnswer;
                    newQuestion = Utilities.getKeyByValue(Main.getIncorrectAnswers(), newAnswer);
                    incorrectAnswers.remove(newQuestion);

                    Main.setIncorrectAnswers(incorrectAnswers);
                }
                else{
                    resetcounter = 1;
                    this.updateQuestion();
                    return;
                }
            }
            text.setText(newQuestion);
            questionAnswer.put(newQuestion, newAnswer);
            Main.updateAskedQuestions(questionAnswer);
        }

        while(questionsLeft.values().remove(answer));

        System.out.println("Questions left: " + questionsLeft);
    }

    public void hideBriefly(){
        this.setVisible(false);
        Question questionObject = this;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tick++;
                if(tick == 170) {
                    questionObject.setVisible(true);
                    tick = 0; timer.stop();
                }
            }
        };
        timer.start();
    }

    public String getCorrectAnswer(){
        return answer;
    }

    public boolean isCorrect(String answered){
        if (answered.equals(answer)){
            return true;
        }
        else{
            return false;
        }
    }

    public static Map<String,String> getLevelQuestions(){
        return levelQuestions;
    }

    public static void clearQuestionsLeft(){
        questionsLeft.clear();
    }

    public static void resetResetCounter(){
        resetcounter = 1;
    }
}
