package Game;

/**
 * Created by richard on 05/06/17.
 */
public class Questions {
    private static String[][][] info = {
            {
                    {"What is the technology that connects millions of devices together and contains websites?", "internet", "HTML, or HyperText Markup Language, is a markup language\nthat specifies how data is arranged on a website."},
                    {"What type of language is HTML?", "markup", "CSS, or Cascading Style Sheets, is a language\nthat specifies how elements should be positioned\non a website."},
                    {"What does the C in CSS stand for?", "cascading", "JavaScript (sometimes called JS), is a programming language\nresponsible for almost all logic and interaction\non a website."},
                    {"What type of language is JavaScript", "programming", "A URL or Uniform Resource Locator is a reference to a web\nresource, and it also specifies how the resource is retrieved."},
                    {"What does the U in URL stand for?", "uniform", "PHP is a server-side programming language used to\nstore and process user information\nand runs even if you're not on the site."}
            },
            //===============================================
            {
                    {"A line is created by joining two _____.", "points", "The point of intersection between two lines is found\nby solving the system of equations for the\ntwo lines."},
                    {"What do you solve to find where two lines meet?", "system of equations", "To factor the trinomial x² + bx + c, \nfind two integers i and j such that\ni + j = b and i x j = c.\nThe factors are (x + i) and (x + j)"},
                    {"If (x + r)(x + s) = x² + bx + c, then c is the ____ of i and j", "product", "The roots of a quadratic equation can\nbe found with the quadratic formula:\n(-b ± √(b² - 4ac)) / 2a\n"},
                    {"What do you use to find the roots of a quadratic equation?", "quadratic formula", "The circumcenter of a triangle is the center of a circle\nthat touches all 3 points of the triangle.\nIt is the point of intersection of all perpendicular bisectors."},
                    {"The circumcenter can be found where the ____ ____ meet.", "perpendicular bisectors", "Mathematician Pierre de Fermat did so much math that\n\"Fermat's Theorem\" can refer to any of 4 different theorems."}
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
