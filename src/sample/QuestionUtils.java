package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionUtils {

    private static Map<String, String> questionAnswer = new HashMap<String, String>();
    private static String path;

    public static Map<String, String> getQuestions(int currentLevel) {
        if(currentLevel == 1){path = "src/sample/data/lv1.txt";}
        if(currentLevel == 2){path = "src/sample/data/lv2.txt";}

        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(new File(path)));
            String row = bufReader.readLine();
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

//    public static Map<String, String> getRandomQuestion(Map<String, String> ){

//    }
}
