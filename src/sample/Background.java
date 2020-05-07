package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Background extends ImageView{
    private AnimationTimer timer;
    private ImageView bg;
    private Image bgImgORI;
    private int tick;

    public Background(){
        bgImgORI = new Image("sample/resources/img/bg.png");
        bg = new ImageView(bgImgORI);
    }

    public void fountainAnimation(){
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tick++;
                if(tick == 10){
//                    setBackground();
                }
            }
        };
        timer.start();
    }

    public ImageView getBackground(){
        return bg;
    }

    public void setBackground(String path){
        Image newImg = new Image(path);
        bg.setImage(newImg);
    }
}
