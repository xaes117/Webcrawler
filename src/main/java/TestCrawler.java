import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

/**
 * Created by nathanliu on 16/07/2016.
 */
public class TestCrawler {

    private String homePage;
    private University CurrentUniversity;

    public TestCrawler(String homePage, University university) {
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
                }
            }
        }
    }
}
