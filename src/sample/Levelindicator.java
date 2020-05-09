package sample;

import javafx.scene.text.Text;

public class Levelindicator extends Text {
    private int x;
    private int y;

    public Levelindicator(int x, int y) {
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setStyle("-fx-font: 30 'SF Pixelate';");
    }

    public void updateLevelIndicator(){
        String currentLevel = String.valueOf(Main.getCurrentLevel());
        this.setText(currentLevel + "/" + Main.getLevelAmount());
    }
}

//String.valueOf(Main.getCurrentLevel())