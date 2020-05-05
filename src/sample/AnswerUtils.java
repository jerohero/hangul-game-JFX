package sample;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnswerUtils {

//    private static Map<String, String> questionsLeft = new HashMap<>();
    private static Map<String, String> allQuestions = new HashMap<>();
    private static ArrayList<Answer> buttons;
    private static ArrayList<String> answers = new ArrayList<>();

    public static void updateAnswers(String correctAnswer){
        allQuestions = Question.getLevelQuestions();
//        if(questionsLeft.isEmpty()){
//             questionsLeft.putAll(allQuestions);
//        }
//        while(questionsLeft.values().remove(correctAnswer));

        buttons = Main.getButtons();

        int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        buttons.get(randomNum).setAnswer(correctAnswer);

        for (int i = 0; i < 4; i++) {
            if(i != randomNum){
                Random random = new Random();
                Object[] values = allQuestions.values().toArray();   //vervangen met questionsleft
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
//        System.out.println("Questions left: " + questionsLeft);
        System.out.println("All questions: " + allQuestions);
        answers.clear();
    }

}
