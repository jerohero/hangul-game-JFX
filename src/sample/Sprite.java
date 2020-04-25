package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Sprite extends ImageView {
    Image img;
    ImageView sprite;

    public Sprite(int x, int y, int w, int h, String path){
        img = new Image(path);
        sprite = new ImageView(img);
        sprite.setFitHeight(h);
        sprite.setFitWidth(w);
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
        sprite.getProperties().put("alive", true);
    }

    public ImageView getSprite(){
        return sprite;
    }
}
