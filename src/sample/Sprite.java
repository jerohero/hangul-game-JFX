package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
    private int h;
    private int w;
    private int x;
    private int y;
    private String path;

    private static int tick;
    private static AnimationTimer walkTimer;
    private static AnimationTimer talkTimer;

    private static boolean animationIsActive = false;
    private static int distancemoved;
    private static int movetick;

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

    public static void spriteIdle(ImageView sprite, int animationtick, String type) {
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
//            if(animationtick % 420 == 0){
//                changeSpriteImg(sprite, "sample/resources/img/enemy1v3old.png");
//            }
        }
    }

    public static void spriteHit(ImageView sprite, int animationtick, String type){
        if(type == "enemy"){
            changeSpriteImg(sprite, "sample/resources/img/enemy1-hit.png");
        }
    }

    public static void enemyTalk(ImageView sprite, int animationtick){
        Main.pauseIdleEnemy();
        if(!animationIsActive){
            talkTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    tick++;
                    animationIsActive = true;
                    if(tick % 10 == 0){
                        changeSpriteImg(sprite, "sample/resources/img/enemy1-talk.png");
                    }
                    if(tick % 20 == 0){
                        changeSpriteImg(sprite, "sample/resources/img/enemy1.png");
                    }
                    if(tick > 60){
                        talkTimer.stop();
                        Main.unpauseIdleEnemy();
                        tick = 0;
                        animationIsActive = false;
                    }
                }
            };
            talkTimer.start();
        }
    }

    public static void moveInNewEnemy(){
        walkTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movetick++;

                if(movetick == 40){

                }
            }
        };

    }

    public static void moveSpriteAway(ImageView sprite) {
        boolean spriteOnRight =  sprite.getX() > Main.getGameWidth()/2 ? true : false;

        talkTimer.stop();
        walkTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sprite.setTranslateX(sprite.getTranslateX() + (spriteOnRight ? -3 : 3));
                movetick++;

                if(movetick == 30){
                    sprite.setDisable(true);
                    walkTimer.stop();
                }
            }
        };
        walkTimer.start();
    }

    private ImageView getSpriteImgView(){
        return sprite;
    }

//    private void movePlayerY(int value) {
//        boolean movingDown = value > 0;
//
//        for (int i = 0; i < Math.abs(value); i++) {
//            for (Node platform : platforms) {
//                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
//                    if (movingDown) {
//                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
//                            player.setTranslateY(player.getTranslateY() - 1);
//                            canJump = true;
//                            return;
//                        }
//                    }
//                    else {
//                        if (player.getTranslateY() == platform.getTranslateY() + blockSize) {
//                            return;
//                        }
//                    }
//                }
//            }
//            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
//        }
//    }
}
