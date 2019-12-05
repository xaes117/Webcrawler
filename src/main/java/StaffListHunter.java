import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import QB.quantumbiology.HtmlClustering;

import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by nathanliu on 23/07/2016.
 */
public class StaffListHunter {

    private static String homePage;
    private static final List<String> DepartmentUrls = new LinkedList<>();
    private static List<String> VisitedPages = new ArrayList<>();
    private static List<String> OutputPages = new ArrayList<>();

    public static void SetHomePage(String url) {
        homePage = url;
        XaCluster cluster = new XaCluster();
        DepartmentUrls.clear();
        for (Element e : cluster.CreateClusters(homePage)) {
            for (Element link : e.getElementsByAttribute("href")) {

                String deptUrl = link.attr("href");
                if (Assistant.IsValidURL(deptUrl)) {
                    System.out.println(link.attr("href"));
                    DepartmentUrls.add(link.attr("href"));
                }
            }
        }
        System.out.println("---------------");
        System.out.println("---------------");
        System.out.println("---------------");
    }

    public static void Hunt() {

//        for (String dept : DepartmentUrls) {
//            Assistant.print(dept);
//        }

        for (String department : DepartmentUrls) {
            VisitedPages.clear();
            GetStaffPages(department);
        }
        System.out.println("----------------------");
        OutputPages.forEach(System.out::println);
    }

    private static void GetStaffPages(final String url) {
        WebDriver driver = new HtmlUnitDriver();
        driver.get(Assistant.parseURL(url));
        List<WebElement> queue = driver.findElements(By.tagName("a"));

        int pagesVisited = 0;
        int currentIndex = 0;

        while (currentIndex < queue.size()) {

            // if it has already been visited remove and skip to next
            // We parse (clean) the url since some urls come in the form of "//www.hello.com"
            String possibleStaffList = queue.get(currentIndex).getAttribute("href");
            System.out.println(possibleStaffList + " is currently under review");
            if (pagesVisited > 500) {
                return;
            }

            try {

                if (!AlreadyVisited(possibleStaffList)) {

                    possibleStaffList = Assistant.parseURL(possibleStaffList);

                    System.out.println(possibleStaffList + " has not been visited before");
                    VisitedPages.add(possibleStaffList);
                    // if it does not contain a student list get all staff/people/academic links
                    // if it contains a list of names add to final output
                    // if it contains staff/people/academic links add to queue
                    for (String str : Assistant.StaffSemantics) {
                        if (queue.get(currentIndex).getText().toUpperCase().contains(str) || possibleStaffList.toUpperCase().contains(str)) {
                            if (IsAStaffPage(possibleStaffList)) {
                                System.out.println(possibleStaffList + " Adding to staff list");
                                OutputPages.add(possibleStaffList);
                            } else {
                                //
                                WebDriver proxy = new HtmlUnitDriver();
                                proxy.get(possibleStaffList);
                                List<WebElement> proxyLinks = proxy.findElements(By.tagName("a"));
                                for (WebElement p : proxyLinks) {
                                    for (String str1 : Assistant.StaffSemantics) {
                                        if (p.getText().toUpperCase().contains(str1)) {
                                            System.out.println(p.getAttribute("href") + " Has been added to the queue");
                                            queue.add(p);
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index out of bound exception");
                //break;
            } catch (Exception e) {
                e.printStackTrace();
                Assistant.print("Connection timeout");
                return;
            } finally {
                Assistant.print(pagesVisited + " pages visited");
                currentIndex++;
                pagesVisited++;
            }
        }
    }

    private static boolean AlreadyVisited(final String url) {
        if (url == null)
            return false;
        for (String page : VisitedPages)
            if (page.equals(url))
                return true;
        return false;
    }

    private static boolean IsAStaffPage(final String url) {

        System.out.println("Checking if "  + url + " is a staff page");

        if (url == null || url.contains("#") || url.contains("@")) {
            return false;
        }

        XaCluster clusters = new XaCluster(Assistant.NameList, 0.65);
        clusters.CreateClusters(url, clusters.getCompareList());

        System.out.println("Ratio = " + ((double)clusters.getValidClusters().size() / (double) clusters.getOriginalClusters().size()));

        return (clusters.getValidClusters().size() / clusters.getOriginalClusters().size()) > 0.5;

//        for (WebElement possibleName : possibleNames) {
//            if (Assistant.IsName(possibleName.getText())) {
//                numberOfNames++;
//            }
//        }
//
//        if (numberOfNames > 0) {
//            if ((numberOfNames / possibleNames.size()) > 0.6) {
//                return true;
//            }
//        }

    }

//    private static void GetDepartmentsPages(String url) {
//        WebDriver driver = new HtmlUnitDriver();
//        driver.get(url);
//        List<WebElement> faculties = driver.findElements(By.className("equalize"));
//        for (WebElement faculty : faculties) {
//            WebElement subc1s = faculty.findElement(By.className("subcl"));
//            if (subc1s.findElements(By.className("heading")).get(0).getText().equals("Departments")) {
//                WebElement ul = subc1s.findElement(By.tagName("ul"));
//                List<WebElement> links = ul.findElements(By.tagName("a"));
//                for (WebElement link : links)
//                    DepartmentUrls.add(link.getAttribute("href"));
//            }
//        }
//    }
}
