package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sprite {
    private static int h;
    private static int w;
    private static int x;
    private static int y;
    private String path;

//    private static double originalpos;

    private static int tick;
    private static AnimationTimer walkTimer;
    private static AnimationTimer talkTimer;

    private static boolean animationIsActive = false;
    private static int distancemoved;
    private static int movetick;

    private static Map<String, String> enemy_path;

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

    public static Map<String, String> initEnemyPaths(int currentLevel){
        enemy_path = new HashMap<>();

        if(currentLevel == 1){
            enemy_path.put("default", "sample/resources/img/enemy1.png");
            enemy_path.put("idle", "sample/resources/img/enemy1v2.png");
            enemy_path.put("hit", "sample/resources/img/enemy1-hit.png");
            enemy_path.put("talk", "sample/resources/img/enemy1-talk.png");
        }
        if(currentLevel == 2){
            enemy_path.put("default", "sample/resources/img/enemy2.png");
            enemy_path.put("idle", "sample/resources/img/enemy2v2.png");
            enemy_path.put("hit", "sample/resources/img/enemy2-hit.png");
            enemy_path.put("talk", "sample/resources/img/enemy2-talk.png");
        }
        if(currentLevel == 3){
            enemy_path.put("default", "sample/resources/img/enemy3.png");
            enemy_path.put("idle", "sample/resources/img/enemy3v2.png");
            enemy_path.put("hit", "sample/resources/img/enemy3-hit.png");
            enemy_path.put("talk", "sample/resources/img/enemy3-talk.png");
        }

//        Sprite enemyObj = new Sprite(x, y, w, h, enemy_path.get("default"));
//        ImageView enemy = enemyObj.createSprite();

        return enemy_path;
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
                changeSpriteImg(sprite, enemy_path.get("default"));
            }
            if(animationtick % 120 == 0){
                changeSpriteImg(sprite, enemy_path.get("idle"));
            }
//            if(animationtick % 420 == 0){
//                changeSpriteImg(sprite, "sample/resources/img/enemy1v3old.png");
//            }
        }
    }

    public static void spriteHit(ImageView sprite, int animationtick, String type){
        if(type == "enemy"){
            changeSpriteImg(sprite, enemy_path.get("hit"));
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
                        changeSpriteImg(sprite, enemy_path.get("talk"));
                    }
                    if(tick % 20 == 0){
                        changeSpriteImg(sprite, enemy_path.get("default"));
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

    public static void newEnemy(ImageView sprite) {
        boolean spriteOnRight =  sprite.getX() > Main.getGameWidth()/2 ? true : false;
        double originalpos = sprite.getTranslateX();
        movetick = 0;


        talkTimer.stop();
        walkTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movetick++;

                if(movetick < 90){
                    sprite.setTranslateX(sprite.getTranslateX() + (spriteOnRight ? -3 : 3));
                }
                if(movetick > 90){
                    if(sprite.getTranslateX() != originalpos){
                        initEnemyPaths(Main.getCurrentLevel());
                        sprite.setTranslateX(sprite.getTranslateX() + (spriteOnRight ? 3 : -3));
                    }
                    else {
                        walkTimer.stop();
                        return;
                    }
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
