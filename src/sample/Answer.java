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
import javafx.scene.text.TextAlignment;

public class Answer extends GridPane {
    private String answer;
    private String side;
    private Text text;

    public Answer(String answer, int x, int y, String side){
        this.answer = answer;
        this.side = side;

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        Image buttonImg = new Image("sample/resources/img/buttonbubble.png");
        ImageView buttonbubble = new ImageView(buttonImg);
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
        text.setText(answer);
    }
}
