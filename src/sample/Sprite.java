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


    public static void changeSpriteImg(ImageView sprite, String path){
        Image image = new Image(path);
        sprite.setImage(image);
    }

//    public static void spriteAnimation(ImageView sprite, int animationtick, String type) {
//        if(type == "player"){
//            if(animationtick == 60){
//                changeSpriteImg(sprite, "sample/resources/img/player.png");
//            }
//            if(animationtick == 120){
//                changeSpriteImg(sprite, "sample/resources/img/playerv2.png");
//            }
//        }
//        else if(type == "enemy"){
//            if(animationtick == 60){
//                changeSpriteImg(sprite, "sample/resources/img/enemy1.png");
//            }
//            if(animationtick == 120){
//                changeSpriteImg(sprite, "sample/resources/img/enemy1v2.png");
//            }
//        }
//    }
public static void spriteAnimation(ImageView sprite, int animationtick, String type) {
    if(type == "player"){
        if(animationtick % 60 == 0){
            changeSpriteImg(sprite, "sample/resources/img/player.png");
        }
        if(animationtick % 120 == 0){
            changeSpriteImg(sprite, "sample/resources/img/playerv2.png");
        }
    }
    else if(type == "enemy"){
        if(animationtick % 60 == 0){
            changeSpriteImg(sprite, "sample/resources/img/enemy1.png");
        }
        if(animationtick % 120 == 0){
            changeSpriteImg(sprite, "sample/resources/img/enemy1v2.png");
        }
        if(animationtick % 420 == 0){
            changeSpriteImg(sprite, "sample/resources/img/enemy1v3.png");
        }
    }
}
}
