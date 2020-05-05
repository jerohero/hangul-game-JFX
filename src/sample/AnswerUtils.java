package sample;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AnswerUtils {

    private static Map<String, String> currentLevelQuestions;
    private static ArrayList<Answer> buttons;
    private static ArrayList<String> answers = new ArrayList<>();

    public static void updateAnswers(String correctAnswer){
        currentLevelQuestions = Question.getLevelQuestions();
        while(currentLevelQuestions.values().remove(correctAnswer));

        buttons = Main.getButtons();

        int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        buttons.get(randomNum).setAnswer(correctAnswer);


        for (int i = 0; i < 4; i++) {
            if(i != randomNum){
                Random random = new Random();
                Object[] values = currentLevelQuestions.values().toArray();
                String newAnswer = (String) values[random.nextInt(values.length)];
//                if(answers.contains(newAnswer)){i--;}
//                else{
                    buttons.get(i).setAnswer(newAnswer);
                    answers.add(newAnswer);
//                }
//                System.out.println(i);
//                System.out.println(answers);
            }
        }
        System.out.println(answers);
        answers.clear();
    }

}
