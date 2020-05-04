package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Answer extends GridPane {
    private String answer;
    private String side;

    public Answer(String answer, int x, int y, String side){
        this.answer = answer;
        this.side = side;

        Image buttonImg = new Image("sample/resources/img/buttonbubble.png");
        ImageView buttonbubble = new ImageView(buttonImg);
        buttonbubble.setFitWidth(192);
        buttonbubble.setFitHeight(120);

        Text text = new Text(answer);
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("answer");
        text.setTranslateX(80);
        text.setTranslateY(-7);

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

    public String getAnswer(){
        return answer;
    }
}
