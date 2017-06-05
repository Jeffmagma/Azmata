package Game;

/**
 * Created by richard on 05/06/17.
 */
public class Questions {
    private static String[][][] info = {
            {
                    {"Question", "A", "learn about question 2\nasdsd"},
                    {"Question2", "B", "learn about question 3\njskd"}
            },
    //===============================================
            {

            },
    //===============================================
            {

            }
    };

    public static String[][] questions = new String[3][5], answers = new String[3][5], material = new String[3][5];

    public static void init(){
        for(int i = 0; i < info.length; i++){
            for(int j = 0; j < info[i].length; j++){
                questions[i][j] = info[i][j][0];
                answers[i][j] = info[i][j][1];
                material[i][j] = info[i][j][2];
            }
        }
    }
}
