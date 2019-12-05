import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

public class WebCrawler {

    private String homePage;
    private University CurrentUniversity;

    public WebCrawler(String homePage, University university) {
        this.CurrentUniversity = university;
        this.homePage = homePage;
    }

    public void start() {
        WebDriver driver = new HtmlUnitDriver();
        driver.get(this.homePage);
        List<WebElement> faculties = driver.findElements(By.className("equalize"));

        for (WebElement faculty : faculties) {
            WebElement subc1s = faculty.findElement(By.className("subcl"));
            if (subc1s.findElements(By.className("heading")).get(0).getText().equals("Departments")) {
                WebElement ul = subc1s.findElement(By.tagName("ul"));
                List<WebElement> links = ul.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String departmentLink = link.getAttribute("href");

                    // Create new subject
                    Subject subject = new Subject(departmentLink, link.getText());

                    // Add subject to current university
                    CurrentUniversity.addSubject(subject);
                    visitDepartmentPage(departmentLink, subject);
                }
            }
        }
    }

    private void visitDepartmentPage(String url, Subject subject) {
        WebDriver auxDriver = new HtmlUnitDriver();
        auxDriver.get(getBaseUrl(url, "people"));

        // Get all a tags
        List<WebElement> links = auxDriver.findElements(By.tagName("a"));
        for (WebElement element : links) {

            try {
                // Iterate through each link
                System.out.println(element.toString() + element.getAttribute("href"));

                // If the link is a name click on the link
                if (Assistant.IsName(element.getText()) && !Assistant.IsEmail(element.getAttribute("href"))) {

                    // Create new teacher
                    Teacher teacher = new Teacher(element.getText(), element.getAttribute("href"));

                    // Add teacher to the subject field
                    subject.addTeacher(teacher);

                    // Visit link
                    visitPersonPage(element.getAttribute("href"), teacher);
                }

            } catch (org.apache.http.client.ClientProtocolException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("ERROR OCCURED AT DEPARTMENT LEVEL");
            }
        }
    }

    private void visitPersonPage(String url, Teacher teacher) throws Exception {

        WebDriver driver = new HtmlUnitDriver();
        driver.get(url);

        // Get p tags for description
        List<WebElement> paragraph = driver.findElements(By.tagName("p"));
        String des = "";

        for (WebElement element : paragraph) {
            des += element.getText();
        }
        teacher.setDescription(des);

        System.out.println(teacher.getName() + " -- " + teacher.getDescription());

        // Get a tags for email

        // Iterate through all a tags
        // if email then add to teacher email
        // email parser


    }

    private static String getBaseUrl(String departmentLink, String peoplePage) {
        return departmentLink.concat("/").concat(peoplePage);
    }
}
