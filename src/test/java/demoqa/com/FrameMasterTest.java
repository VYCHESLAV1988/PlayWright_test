package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameMasterTest {

    // =================== Переменные =====================

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
    static void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false) //
                .setSlowMo(500)); // Замедление демонстрации
    }

    @Test
    void testFrameWorkflow(){
        // 1. Открываем страницу с фреймами
        page.navigate("https://demoqa.com/frames");

        // 2. Захватываем первый фрейм
        FrameLocator firstFrame = page.frameLocator("#frame1");

        // 3. Проверяем текст внутри фрейма
        assertThat(firstFrame.locator("#sampleHeading"))
                .hasText("This is a sample page");

        // 4. Границы фрейма
        page.locator("#frame1").evaluate("e => e.style.border = '3px solid red'");

        // 5. Переходим на страницу с вложенными фреймами
        page.locator("'Nested Frames'").click();

        // 6. Работаем с иерархией фреймов
        FrameLocator parentFrame = page.frameLocator("#frame1");
        FrameLocator childFrame = parentFrame.frameLocator("iframe");

        // 7. Проверка текста в дочерном фрейме



















    }


















































    @AfterAll
    static void tearDown(){
        browser.close();
        playwright.close();
    }





















}
