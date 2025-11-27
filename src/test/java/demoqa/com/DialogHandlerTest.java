package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DialogHandlerTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://demoqa.com/alerts");
    }

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500));
    }


    @Test
    @DisplayName("Обработка диалоговых окон: alert, confirm, prompt")
    void testDialogHandling(){
        // ================ 1. Обработка Alert ============================
        page.onceDialog(dialog -> {
            assertEquals("alert", dialog.type());
            assertEquals("You clicked a button", dialog.message());
            dialog.accept();
        });
        page.click("#alertButton");
        page.waitForTimeout(1200); // Небольшая пауза для демонстрации ← было 500, ставим 1200
        System.out.println("Нажали на кнопку Alert");

        // ================ 2. Обработка Confirm (Accent) ============================
        page.onceDialog(dialog -> {
            assertEquals("confirm", dialog.type());
            assertEquals("Do you confirm action?", dialog.message());
            dialog.accept();
        });
        page.click("#confirmButton");
        assertEquals("You selected Ok", page.locator("#confirmResult").innerText());
        System.out.println("Нажали Ok");

        // ================ 3. Обработка Confirm (Dismiss) ============================
        page.onceDialog(dialog -> {
            assertEquals("confirm", dialog.type());
            assertEquals("Do you confirm action?", dialog.message());
            dialog.dismiss();
        });
        page.click("#confirmButton");
        assertEquals("You selected Cancel", page.locator("#confirmResult").innerText());
        System.out.println("Нажали Cancel");

        // ================ 4. Обработка Prompt ============================
        page.onceDialog(dialog -> {
            assertEquals("prompt", dialog.type());
            assertEquals("Please enter your name", dialog.message());
            dialog.accept("Slava Vasiltsov");
        });
        page.click("#promtButton");
        assertEquals("You entered Slava Vasiltsov", page.locator("#promptResult").innerText());
        System.out.println("Вы ввели Slava Vasiltsov");

        // ================ 5. Параллельная обработка ============================
        page.onceDialog(dialog -> {
            if ("alert".equals(dialog.type())){
                dialog.accept();
            }
        });
        page.click("#timerAlertButton");
        page.waitForTimeout(6000); // Ожидание таймерного алерта
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
