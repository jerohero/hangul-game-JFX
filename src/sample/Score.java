package sample;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Score extends Pane {
    private int playerScore;
    private int maxScore;
    private int x;
    private int y;
    private int scoreBarWidth;
    private Rectangle scoreProgress;
    private double maxScoreToWidth;
    private double playerScoreToWidth;

    public Score(int x, int y){
        this.x = x;
        this.y = y;
        playerScore = 0;
        scoreBarWidth = 300;

        ImageView scoreBar = new ImageView("sample/resources/img/scorebar.png");

        scoreProgress = new Rectangle(0, 22);
        scoreProgress.setX(8);
        scoreProgress.setY(8);
        scoreProgress.setFill(Color.LIMEGREEN);

        this.getChildren().add(scoreBar);
        this.getChildren().add(scoreProgress);
        this.setLayoutX(x-24);
        this.setLayoutY(y+4);
    }


    public void setPlayerScore(int playerScore){
        if(playerScore < 0) Main.setPlayerScore(0);
        scoreProgress.setWidth(playerScore * maxScoreToWidth);
        this.playerScore = playerScore;
        if(playerScore == maxScore) {
            setPlayerScore(0);
            Main.setPlayerScore(0);
            Main.nextStage();
        }
    }
    public int getPlayerScore(){
        return playerScore; }

    public void setMaxScore(int maxScore){
        this.maxScore = maxScore;
        maxScoreToWidth = scoreBarWidth / maxScore;
    }
    public int getMaxScore(){
        return maxScore;
    }
}
