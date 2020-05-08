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
    private String hangul;
    private String romanized;
    private static Map<String, String> levelQuestions;
    private static ArrayList<Map> askedQuestions;
    private static Map<String, String> questionsLeft = new HashMap<>();
    private static int resetcounter;

    private AnimationTimer timer;
    private int tick;

    //    public Question(String hangul, String romanized){
    public Question(){
        Image bubbleImg = new Image("sample/resources/img/textbubble0.png");
        ImageView bubble = new ImageView(bubbleImg);
        bubble.setFitWidth(140);
        bubble.setFitHeight(140);

//        questionAnswer.put(hangul, romanized);
//        Main.addToQuestionsArray(questionAnswer);

        text = new Text();
//        answer = questionAnswer.get(hangul);

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("questiontxt");
        text.setTranslateX(25);
        text.setTranslateY(90);

        this.getChildren().add(bubble);
        this.getChildren().add(text);

        this.setTranslateX(Main.getQuestionColumn() * Main.getBlockSize() - 12);
        this.setTranslateY(Main.getQuestionRow() * Main.getBlockSize() - 170);
    }

    public void updateQuestion(int currentLevel){
        String newQuestion = "";
        String newAnswer = null;
        Map<String, String> incorrectAnswers = new HashMap<>();

        levelQuestions = QuestionUtils.getAllLevels().get(currentLevel-1);
//        System.out.println(levelQuestions);
        askedQuestions = Main.getAskedQuestions();
        if(questionsLeft.isEmpty()){
            questionsLeft.putAll(levelQuestions);
            resetcounter++;
        }

        if(!levelQuestions.isEmpty()){
//            if(levelQuestions.size() == 1){
//                newQuestion = levelQuestions.keySet().toString().replace("]", "").replace("[", "");
//                newAnswer = levelQuestions.values().toString().replace("]", "").replace("[", "");
//                answer = newAnswer;
//            }
//            else {
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
                    this.updateQuestion(currentLevel);
                    return;
                }
            }
//            }
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
//        if (true){

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

    public static Map<String, String> getQuestionsLeft(){
        return questionsLeft;
    }

    public static void setQuestionsLeft(Map<String, String> newQuestionsLeft){
        questionsLeft = newQuestionsLeft;
    }

    public static void clearQuestionsLeft(){
        questionsLeft.clear();
    }

    public static void resetResetCounter(){
        resetcounter = 1;
    }


}
