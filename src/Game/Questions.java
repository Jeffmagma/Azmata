package Game;

/**
 * Created by richard on 05/06/17.
 */
public class Questions {
    private static String[][][] info = {
            {
                    {"Hardware is _____ equipment", "a", "All software requires memory to operate.\nThe software uses memory to keep track of\nuser input and the result of calculations."},
                    {"What does all software require in order to operate?", "memory", "An algorithm is a specific set of instructions\nwith a specific input and output.\nAn algorithm is just a concept and has to be\ntranslated to become software."}
            },
    //===============================================
            {

            },
    //===============================================
            {

            }
    };

    public static String firsts[] = {
            "Hardware is the physical equipment that carries out tasks and instructions.\nSoftware is instructions that are meant to be run by hardware",
            "",
            ""
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
