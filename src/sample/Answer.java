package sample;

import javafx.scene.control.Button;

public class Answer extends Button {

    public Answer(String text, int x, int y){
        super(text);
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("answer");
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
}
