import java.util.*;
import java.util.logging.Filter;

/**
 * Created by nathanliu on 16/07/2016.
 */
abstract class Assistant {

    public static final String Line = "--------------------------------------";

    public static LinkedList<String> SubjectList = new LinkedList<String>();
    public static LinkedList<String> NameList = new LinkedList<String>();

    private static String[] dictionary = {"DEPARTMENT", "DIVISIONS", "SUBJECTS", "FACULTY",
            "FACULTIES", "DEPARTMENTS", "SUBJECT", "INSTITUTE", "INSTITUTES"};

    public static String[] StaffSemantics = {"PEOPLE", "STAFF"};

    // Given an ordered list and an input x, this function will return true if x is in the list
    // binary search is used
    // e.g. list = ["ANNA", "BILL", "CLINTON", "DANIEL", "FERNANDO"]
    // IsName("anna") => true
    // IsName("ANNA") => true
    // IsName("hello") => false
    public static boolean IsName(String line) {
        ArrayList<String> nameFields = new ArrayList<>(GetWords(line));

        for (String part : nameFields) {
            // System.out.println(part + ":" + Collections.binarySearch(NameList, part.toUpperCase()));
            // if greater than 1 return true
            if (Collections.binarySearch(NameList, part.toUpperCase()) >= 0) {
                return true;
            }
        }
        return false;
    }

    // Given a string x
    // If the first 6 letters are 'm', 'a', 'i', 'l', 't', 'o' then the function will return true
    // E.g.
    // IsEmail("mailto:quantumbiology@hotmail.wong") => true
    // IsEmail("helo123") => false
    public static boolean IsEmail(String line) {
        if (line.substring(0, 6).equals("mailto")) {
            return true;
        }
        return false;
    }

    // Given an ordered list and an input x, this function will return true if x is in the list
    // binary search is used
    // e.g. list = ["BIOLOGY", "CHEMISTRY", "GEOGRAPHY", "MATH", "PHYSICS"]
    // IsDepartment("PHYSICS") => true
    // IsDepartment("Physics") => true
    // IsDepartment("anna") => false
    public static boolean IsDepartmentsLink(String line) {
        ArrayList<String> fields = new ArrayList<>(GetWords(line));

        for (String field : fields) {
            field = field.toUpperCase();
            for (String word : dictionary) {
                if (word.equals(field)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean InMainUrl(String url, String line) {

        String mainUrl = "";
        String[] typicalComs = {".com", ".co.uk", ".ac.uk", ".edu"};

        for (String t : typicalComs) {
            for (int i = 0 ; i < mainUrl.length(); i++) {
                if (url.substring(i, i + t.length()).equals(t)) {
                    for (int j = i; j > 0; j--) {
                        if (url.charAt(j) == '.') {
                            break;
                        }
                        mainUrl += url.charAt(j);
                    }
                    break;
                }
            }
        }

        if (mainUrl.equals(line)) {
            return true;
        } else {
            return false;
        }

    }

    // Given a sentence the function will return the individual words
    // E.g.
    // GetWords("hello my name is nathan") => ["hello", "my", "name", "is", "nathan"]
    // GetWords("hello") => ["hello"]
    // GetWords("hello world") => ["hello", "world"]
    public static boolean IsValidURL(String url) {

        if (url.contains("http") || url.contains("https") || url.contains("www")) {
            return true;
        }
        return false;
    }

    public static String parseURL(String url) {

        if (url.length() == 0) {
            return url;
        }

        if (url.charAt(0) == '/' && url.charAt(1) == '/') {
            return "http:" + url;
        }

        return url;

    }

    // Given a sentence the function will return the individual words
    // E.g.
    // GetWords("hello my name is nathan") => ["hello", "my", "name", "is", "nathan"]
    // GetWords("hello") => ["hello"]
    // GetWords("hello world") => ["hello", "world"]
    static ArrayList<String> GetWords(String line) {

        ArrayList<String> fields = new ArrayList<>();
        String temp = "";

        for (int  i = 0 ; i < line.length(); i++) {

            temp += line.charAt(i);

            if (line.charAt(i) == ' ' || i == line.length() - 1) {
                fields.add(FilterCharacters(temp));
                if (i == line.length() - 1) {
                    break;
                }
                temp = "";
            }
        }
        return fields;
    }

    // Given a senentence this function will break it down into a triangle
    // e.g.
    // Breaker("hello world") => ["hello world one",
    //                                  "world one",
    //                                        "one"]
    // Breaker("hello my name is nathan okay") => ["hello my name is nathan okay",
    //                                                   "my name is nathan okay",
    //                                                      "name is nathan okay",
    //                                                           "is nathan okay",
    //                                                              "nathan okay",
    //                                                                     "okay"]
    public static ArrayList<String> Breaker(String line) {

        ArrayList<String> fields = new ArrayList<>();
        fields.add(line);

        int end = line.length();

        for (int i = 0; i < end; i++) {
            if (line.charAt(0) == ' ') {
                fields.add(line.substring(1));
            }
            line = line.substring(1);
        }

        return fields;

    }

    
    // Filters characters from certain characters
    // E.g.
    // FilterCharacters("hello, it is quite cold; I think.") => "helloitisquitecoldIthink"
    // FilterCharacters("bool:") => "bool"
    private static String FilterCharacters(String line) {
        String[] rchars = {";", ":", ".", ",", " "};
        for (String character : rchars) {
            line = line.replace(character, "");
        }
        return line;
    }

    // Given an input string x, CompareSubject(string) will return true if either GetWords() or Breaker() returns true
    // SubjectList = ["BIOLOGY DEPARTMENT", "CHEMISTRY", "INSTITUTE OF GEOGRAPHY", "MATH", "SCHOOL OF PHYSICS"]
    // CompareSubject("Biology") => true
    // CompareSubject("School Of Physics") => true
    // CompareSubject("hello") => false
    public static boolean CompareSubject(String line) {

        for (String word : Assistant.GetWords(line)) {
            // Check if word matches a department name
            if (Collections.binarySearch(Assistant.SubjectList, word) >= 0) {
                // System.out.println("True 1");
                return true;
            }
        }

        for (String chunk : Assistant.Breaker(line)) {
            // Check if phrase matches a department name
            if (Collections.binarySearch(Assistant.SubjectList, chunk) >= 0) {
                // System.out.println("True 2");
                return true;
            }
        }

        return false;
    }

    // Given an input string x, CompareSubject(string) will return true if either GetWords() or Breaker() returns true
    // SubjectList = ["BIOLOGY DEPARTMENT", "CHEMISTRY", "INSTITUTE OF GEOGRAPHY", "MATH", "SCHOOL OF PHYSICS"]
    // CompareSubject("Biology") => true
    // CompareSubject("School Of Physics") => true
    // CompareSubject("hello") => false
    public static boolean CompareSubject(String line, List<String> inputList) {

        for (String word : Assistant.GetWords(line)) {
            // Check if word matches a department name
            if (Collections.binarySearch(inputList, word) >= 0) {
                // System.out.println("True 1");
                return true;
            }
        }

        for (String chunk : Assistant.Breaker(line)) {
            // Check if phrase matches a department name
            if (Collections.binarySearch(inputList, chunk) >= 0) {
                // System.out.println("True 2");
                return true;
            }
        }

        return false;
    }

    public static void print(String x) {
        System.out.println(x);
    }
}

