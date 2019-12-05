import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathanliu on 23/07/2016.
 */
public class HTMLFilter {

    private static List<List<String>> clusters;
    private static int numberOfStartIndicators = 0;
    private static int numberOfEndIndicators = 0;

    private static ArrayList<String> openTags;
    private static ArrayList<String> closeTags;
    private static int currentIndex = 0;

    public static void AttemptToFiler(String uni) {

        openTags = new ArrayList<>();
        closeTags = new ArrayList<>();

        clusters = new ArrayList<>();
        clusters.add(new ArrayList<>());

        WebDriver driver = new HtmlUnitDriver();
        driver.get(uni);

        System.out.println(ExtractBody(driver.getPageSource()));
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("---------------------");

        System.out.println(ExtractMain(ExtractBody(driver.getPageSource())));

        for (String str : clusters.get(0)) {
            System.out.println(str);
        }

//        System.out.println(ExtractBody())

//        String hello = "<\\/script>";
//        System.out.println(hello.substring(0,10));

    }

//    public static String FilterScripts(String input) {
//
//    }


    public static String ExtractBody(String input) {
        String retString = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i, i+5).equals("<body")) {
                for (int j = i; j < input.length(); j++) {
                    if (input.substring(j, j+7).equals("</body>")) {
                        retString += "</body>";
                        break;
                    }
                    retString += input.charAt(j);
                }
                break;
            }
        }
        return retString;
    }


    public static String ExtractMain(String urlString) {

        System.out.println("Hello " + urlString.length());

        int parameterX = 0;
        int parameterY = 0;

        boolean withinScript = false;
        int scriptLayer = 0;

        for (int i = 0; i < urlString.length(); i++) {

            if (i + 10 < urlString.length()) {
                if (urlString.substring(i, i + 8).equals("<script>")) {
                    i++;
                    scriptLayer++;
                    withinScript = true;
                } else if (urlString.substring(i, i + 8).equals("</script") || urlString.substring(i, i+10).equals("<\\/script>")) {
                    scriptLayer--;
                    withinScript = false;
                }
            }

            if (!withinScript && scriptLayer == 0) {
                if (urlString.charAt(i) == '<' && urlString.charAt(i+1) == '!') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfStartIndicators++;
                } else if (urlString.charAt(i) == '<' && urlString.charAt(i+1) == '?') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfStartIndicators++;
                } else if (urlString.charAt(i) == '<' && urlString.charAt(i+1) == '!') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfStartIndicators++;
                } else if (urlString.charAt(i) == '<' && urlString.charAt(i+1) == '/') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfEndIndicators++;
                } else if (urlString.charAt(i) == '/' && urlString.charAt(i+1) == '>') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfEndIndicators++;
                } else if (urlString.charAt(i) == '?' && urlString.charAt(i+1) == '>') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfEndIndicators++;
                } else if (urlString.charAt(i) == '-' && urlString.toString().charAt(i+1) == '>') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfEndIndicators++;
                } else if (urlString.charAt(i) == '<') {
                    System.out.println("Start: " + numberOfStartIndicators + " End: " + numberOfEndIndicators);

                    numberOfStartIndicators++;
                }

            }

            if (numberOfStartIndicators - numberOfEndIndicators == 0) {
                parameterY = i;
                while (urlString.charAt(parameterX) != '>') {
                    parameterX++;
                }
                while (urlString.charAt(parameterY) != '>') {
                    parameterY++;
                }

                System.out.println(numberOfStartIndicators);

                clusters.get(currentIndex).add(urlString.substring(0, i+2));
                break;
            }

        }

        return urlString.substring(parameterX, parameterY);

    }

}
