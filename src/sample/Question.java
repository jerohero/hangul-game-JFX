package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

public class Question extends Pane {
    private Map<String, String> questionAnswer;
    private String question;
    private String answer;
    private Text text;
    private String hangul;
    private String romanized;

    public Question(String hangul, String romanized){
        Image bubbleImg = new Image("sample/resources/img/textbubble.png");
        ImageView bubble = new ImageView(bubbleImg);
        bubble.setFitWidth(140);
        bubble.setFitHeight(140);

        Map<String, String> questionAnswer = new HashMap<>();
        questionAnswer.put(hangul, romanized);
        this.questionAnswer = questionAnswer;
        Main.addToQuestionsArray(questionAnswer);

        //bij opstarten random lijst maken met vragen

        text = new Text(hangul);
        answer = questionAnswer.get(hangul);

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("questiontxt");
        text.setTranslateX(25);
        text.setTranslateY(90);

        this.getChildren().add(bubble);
        this.getChildren().add(text);

        this.setTranslateX(Main.getQuestionColumn() * Main.getBlockSize());
        this.setTranslateY(Main.getQuestionRow() * Main.getBlockSize() - 180);
    }

    public String getCorrectAnswer(){
        return answer;
    }

    public boolean isCorrect(String answered){
        if (answered == answer){
            return true;
        }
        else{
            return false;
        }
    }
}
