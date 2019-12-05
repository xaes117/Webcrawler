import QB.quantumbiology.HtmlClustering;

import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.*;
import javax.xml.parsers.*;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DeathToEikon {

    HtmlClustering clustering = new HtmlClustering();

    public static void main(String[] args) {

        Logger logger = Logger.getLogger ("");
        logger.setLevel (Level.OFF);

        retrieveSubjects();
        retrieveNames();

//        String uni = "http://www.ucl.ac.uk/departments/faculties";
//
//        XaCluster cluster = new XaCluster();
//
//        cluster.CreateClusters(uni);

        StaffListHunter.SetHomePage(SearchEngine.SearchForDepartmentLink("University of Cambridge"));
        StaffListHunter.Hunt();

    }

//    public static void main(String[] args) {
//
//        retrieveNames();
//
//        University UCL = new University("UCL", new ArrayList<Subject>());
//
//        WebCrawler webCrawler = new WebCrawler(homePage, UCL);
//        webCrawler.start();
//
//        System.out.println("---------------------");
//
//        for (Subject subject: UCL.getSubjectList()) {
//
//            System.out.println(subject.getSubjectString());
//
//            for (Teacher teacher : subject.getTeacherList()) {
//
//                System.out.println("Teacher");
//                System.out.println(teacher.getName());
//                System.out.println(teacher.getDescription());
//
//            }
//
//        }
//
//    }

    private static void retrieveSubjects() {

        String filename = "/Users/nathanliu/Documents/Programming/Java/NameGenerator/out/production/NameGenerator/com/Name/subjects-list.txt";

        File textFile = new File(filename);
        try {

            Scanner input = new Scanner(textFile);

            String name;

            while (input.hasNextLine()) {
                name = input.nextLine();
                Assistant.SubjectList.add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void retrieveNames() {

        String filename = "/Users/nathanliu/Documents/Programming/Java/NameGenerator/src/com/Name/names-list2.txt";

        File textFile = new File(filename);
        try {

            Scanner input = new Scanner(textFile);

            String name;

            while (input.hasNextLine()) {
                name = input.nextLine();
                Assistant.NameList.add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}