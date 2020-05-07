package sample;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class QuestionUtils {

    private static Map<String, String> questionAnswer = new HashMap<String, String>();
    private static String path;
    private static ArrayList<Map> allLevels;
    private static BufferedReader bufReader;

    public static Map<String, String> getQuestions(int currentLevel) {
        if(currentLevel == 1){path = "src/sample/data/lv1.txt";}
        else if(currentLevel == 2){path = "src/sample/data/lv2.txt";}


        try {
            bufReader = new BufferedReader(new FileReader(new File(path)));
            String row = bufReader.readLine();
            questionAnswer.clear(); 
            while(row != null){
                String hangul = row.split(":")[0];
                String romanized = row.split(":")[1];
                questionAnswer.put(hangul, romanized);
                row = bufReader.readLine();
            }
            bufReader.close();
        }catch (FileNotFoundException fnfe) {
            System.out.println("Exception: " + fnfe.toString());
            System.out.println("Stack trace:");fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            System.out.println("Exception: " + ioe.toString());
            System.out.println("Stack trace:");ioe.printStackTrace();
            return null;
        }
        return questionAnswer;
    }

    public static ArrayList<Map> initializeQuestions(){
        Map<String, String> level1questions = QuestionUtils.getQuestions(1);
        level1questions = shuffleQuestions(level1questions);

        Map<String, String> level2questions = QuestionUtils.getQuestions(2);
        level2questions = shuffleQuestions(level2questions);
        System.out.println("Level2questions: " + level2questions );

        allLevels = new ArrayList<>();
        allLevels.add(level1questions);
        allLevels.add(level2questions);
        return allLevels;
    }

    private static Map<String, String> shuffleQuestions(Map<String, String> questions){
        List<Map.Entry<String, String>> entries = new ArrayList<>(questions.entrySet());
        Collections.shuffle(entries);
        Map<String, String> shuffledQuestions = new LinkedHashMap<>();
        for(Map.Entry<String, String> entry : entries){
            shuffledQuestions.put(entry.getKey(), entry.getValue());
        }
        return shuffledQuestions;
    }

    public static ArrayList<Map> getAllLevels(){
        return allLevels;
    }
}
