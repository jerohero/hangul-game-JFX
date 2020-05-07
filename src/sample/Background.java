package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Background extends ImageView{
    private AnimationTimer timer;
    private ImageView bg;
    private Image bgImgORI;
    private int tick;
    private boolean animationRunning = false;

    public Background(){
        bgImgORI = new Image("sample/resources/img/bg.png");
        bg = new ImageView(bgImgORI);
    }

    public void fountainAnimation() {
        if (!animationRunning) {
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    tick++;
                    animationRunning = true;
                    if (tick == 10) setBackground("sample/resources/img/fountain/fountain0.png");
                    if (tick == 20) setBackground("sample/resources/img/fountain/fountain1.png");
                    if (tick == 30) setBackground("sample/resources/img/fountain/fountain2.png");
                    if (tick == 40) setBackground("sample/resources/img/fountain/fountain3.png");
                    if (tick == 50) setBackground("sample/resources/img/fountain/fountain4.png");
                    if (tick == 60) setBackground("sample/resources/img/fountain/fountain5.png");
                    if (tick == 70) setBackground("sample/resources/img/fountain/fountain6.png");
                    if (tick == 80) setBackground("sample/resources/img/fountain/fountain7.png");
                    if (tick == 90) {
                        bg.setImage(bgImgORI);
                        timer.stop();
                        tick = 0;
                        animationRunning = false;
                    }
                }
            };
            timer.start();
        }
    }

    public ImageView getBackground(){
        return bg;
    }

    public void setBackground(String path){
        Image newImg = new Image(path);
        bg.setImage(newImg);
    }
}
