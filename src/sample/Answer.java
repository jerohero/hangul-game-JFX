package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Answer extends Button {
    private String answer;

    public Answer(String answer, int x, int y){
        super(answer);
        this.answer = answer;
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("answer");
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    public String getAnswer(){
        return answer;
    }

    public void checkAnswer(){

    }
}
