package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestChema {

        static Playwright playwright;
        static Browser browser;
        BrowserContext context;
        Page page;

        @BeforeEach
        void createContextAndPage() {
            context = browser.newContext();
            page = context.newPage();
        }

        @BeforeAll
        static void setUp() {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(500));
        }














        @AfterEach
        void closeContext(){
            context.close();
        }

        @AfterAll
        static void tearDown() {
            browser.close();
            playwright.close();
        }

    }




