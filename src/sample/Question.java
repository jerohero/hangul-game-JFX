package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

public class Question extends Pane {
    private Map<String, String> questionAnswer = new HashMap<>();
    private String question;
    private String answer;
    private Text text;
    private String hangul;
    private String romanized;
    private static Map<String, String> levelQuestions;
    private static ArrayList<Map> askedQuestions;
    private static Map<String, String> questionsLeft = new HashMap<>();
    private static int resetcounter;

    //    public Question(String hangul, String romanized){
    public Question(){
        Image bubbleImg = new Image("sample/resources/img/textbubble.png");
        ImageView bubble = new ImageView(bubbleImg);
        bubble.setFitWidth(140);
        bubble.setFitHeight(140);

//        questionAnswer.put(hangul, romanized);
//        Main.addToQuestionsArray(questionAnswer);

        text = new Text();
//        answer = questionAnswer.get(hangul);

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("questiontxt");
        text.setTranslateX(25);
        text.setTranslateY(90);

        this.getChildren().add(bubble);
        this.getChildren().add(text);

        this.setTranslateX(Main.getQuestionColumn() * Main.getBlockSize());
        this.setTranslateY(Main.getQuestionRow() * Main.getBlockSize() - 180);
    }

    public void updateQuestion(int currentLevel){
        String newQuestion = "";
        String newAnswer;

        levelQuestions = QuestionUtils.getAllLevels().get(currentLevel-1);
        askedQuestions = Main.getAskedQuestions();
        if(questionsLeft.isEmpty()){
            questionsLeft.putAll(levelQuestions);
            resetcounter++;
        }

        if(!levelQuestions.isEmpty()){
            if(levelQuestions.size() == 1){
                newQuestion = levelQuestions.keySet().toString().replace("]", "").replace("[", "");
                newAnswer = levelQuestions.values().toString().replace("]", "").replace("[", "");
                answer = newAnswer;
            }
            else {
                
                Random random = new Random();
                Object[] values = questionsLeft.values().toArray();    // levelquestions of questionsleft
                newAnswer = (String) values[random.nextInt(values.length)];

                answer = newAnswer;

                for (Map.Entry<String, String> entry : questionsLeft.entrySet()) {
                    if (Objects.equals(newAnswer, entry.getValue())) {
                        newQuestion = entry.getKey();
                    }
                }

            }
            text.setText(newQuestion);
            questionAnswer.put(newQuestion, newAnswer);
            Main.updateAskedQuestions(questionAnswer);
        }
//        if(!askedQuestions.isEmpty()){
//            if(askedQuestions.get(0).containsValue(newAnswer)){
//                System.out.println("hehehehehhe");
//            }
//        }

        while(questionsLeft.values().remove(answer));

        System.out.println("questions left: " + questionsLeft);
    }

    public String getCorrectAnswer(){
        return answer;
    }

    public boolean isCorrect(String answered){
//        if (true){

        if (answered.equals(answer)){
            return true;
        }
        else{
            return false;
        }
    }

    public static Map<String,String> getLevelQuestions(){
        return levelQuestions;
    }

    public static Map<String, String> getQuestionsLeft(){
        return questionsLeft;
    }

    public static void setQuestionsLeft(Map<String, String> newQuestionsLeft){
        questionsLeft = newQuestionsLeft;
    }


}
