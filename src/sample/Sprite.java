package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.text.Element;

public class Sprite {
    private int h;
    private int w;
    private int x;
    private int y;
    private String path;

    ImageView sprite;

    public Sprite(int x, int y, int w, int h, String path) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.path = path;
    }

    public ImageView createSprite(){
        Image img = new Image(path);
        sprite = new ImageView(img);
        sprite.setFitHeight(h);
        sprite.setFitWidth(w);
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
        sprite.getProperties().put("alive", true);

        return sprite;
    }

}
