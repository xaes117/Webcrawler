import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import QB.quantumbiology.HtmlClustering;
import java.util.*;

public class SearchEngine {

    private static String BaseURL = "http://www.bing.com/search?q=";
    private static LinkedList<WebElement> PossibleLinks = new LinkedList<WebElement>();

    public static String SearchForDepartmentLink(String universityName) {

        WebDriver searchPage = new HtmlUnitDriver();
        searchPage.get(BaseURL + universityName);
        WebElement results = searchPage.findElement(By.id("b_results"));
        List<WebElement> links = results.findElements(By.className("b_algo"));
        // Visit home page
        List<WebElement> l1 = links.get(0).findElements(By.tagName("a"));

        String retString = VisitHomePage(l1.get(0).getAttribute("href"));
        return retString;

    }

    private static String VisitHomePage(String URL) {
        System.out.println(URL);
        WebDriver homePage = new HtmlUnitDriver();
        homePage.get(URL);
        List<WebElement> links = homePage.findElements(By.tagName("a"));

        for (WebElement link : links) {
            try {
                List<Element> text = HtmlClustering.getClusterFromString(link.getAttribute("innerHTML"));

                for (Element e : text) {
                    if (Assistant.IsDepartmentsLink(e.text())) {
                        // Every text with an a-tag that is associated with a department listing
                        // Is added as a possible link
                        PossibleLinks.add(link);
                        break;
                    }
                }

                // Unsure if the foreach loop above visits the root node returned
                // So this if statement here is just acts as a guarentee
                if (Assistant.IsDepartmentsLink(link.getText())) {
                    // Every text with an a-tag that is associated with a department listing
                    // Is added as a possible link
                    System.out.println(link.getText());
                    PossibleLinks.add(link);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        for (WebElement str : PossibleLinks) {
//            System.out.println(str.getText());
//        }

        return FindDepartmentList().getAttribute("href");

    }

    private static WebElement FindDepartmentList() {

        System.out.println(PossibleLinks.element().getAttribute("href"));
        if (ConfirmLink(PossibleLinks.element().getAttribute("href"))) {
            // Link confirmed and found
            return PossibleLinks.get(0);

        } else {
            // Link is not department list
            // Remove from queue
            PossibleLinks.remove(0);

            // Carry out the same function with the next item in the queue
            return FindDepartmentList();
        }
    }

    private static boolean ConfirmLink(String url) {

        WebDriver departmentPage = new HtmlUnitDriver();
        departmentPage.get(url);
        List<WebElement> aLinks = departmentPage.findElements(By.tagName("a"));

        int numberOfSubjects = 0;

        for (WebElement element : aLinks) {
            // If Compare is true increment number of subjects
            if (Assistant.CompareSubject(element.getText().toUpperCase())) {
                numberOfSubjects++;
            }

            if (numberOfSubjects > 4) {
                // if more than 5 return true
                // ie this page is a page containing a department list
                return  true;
            }
            // Add WebElement to back of queue
            // In case nothing is found
            // We carry out a BFS
            PossibleLinks.add(element);
        }

        return false;

    }

}
