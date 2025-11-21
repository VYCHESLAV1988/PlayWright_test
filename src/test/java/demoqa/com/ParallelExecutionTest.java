package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParallelExecutionTest {

    private Playwright playwright;

    private Browser browser;

    @BeforeEach
    void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
    }

   @AfterEach
    void tearDown(){
        browser.close();
        playwright.close();
    }

    // ? -------------------- 1.ТЕСТ ПРОВЕРКА ЗАГОЛОВКА GOOGLE---------------------------------

    @Test
    void testGoogleTitle(){
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://google.com");
        assertTrue(page.title().contains("Google"));
        context.close();
    }

    // ? -------------------- 2.ТЕСТ ПРОВЕРКА Playwright документацию---------------------------------

    @Test
    void  testPlaywrightDocs(){
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://playwright.dev/java");
        assertTrue(page.title().contains("Playwright"));
        context.close();
    }

    // ? -------------------- 3.ТЕСТ ПРОВЕРКА Wikipedia ---------------------------------

    @Test
    void testWikipedia(){
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://en.wikipedia.org");
        assertTrue(page.title().contains("Wikipedia"));
        context.close();
    }


}
