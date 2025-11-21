package example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FirstTest {

    Playwright playwright;

    Browser browser;

    Page page;

    @BeforeEach
    void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    void openBrowser(){
        page.navigate("https://playwright.dev");
        String title = page.title();

       Assertions.assertEquals("Playwright", title);
    }

    @AfterEach
    void teardown(){
        browser.close();
        playwright.close();
    }
}
