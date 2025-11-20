import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class PlaywrightFundamentalsTest {

    // =================== Переменные =====================

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;


    // ============ Первый метод Инициаций браузера setUp ======================

    @BeforeAll
    static void launchBrowser(){
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false) //
            .setSlowMo(500)); // Замедление демонстрации
    }

    // ============ Второй метод Заверщение работы браузера tearDown ======================

    @AfterAll
    void closeBrowser(){
        browser.close();
        playwright.close();
    }

    // ============ Первый метод Изоляция текста через новый контекст ======================

    @BeforeEach
    void createContexAndPage(){
        context = browser.newContext();
        page = context.newPage();
    }

    // ============ Второй метод Закрытие текста через новый контекст ======================

    @AfterEach
    void closeContext(){
        context.close();
    }

    //============================= ТЕСТЫ =========================================

    @Test
    @DisplayName("Основы Playwright: Навигация, поиск элементов и взаимодействие")
    void testPlaywrightFundamentals(){
        // ? -------------------- 1.НАВИГАЦИЯ И ОЖИДАНИЯ ---------------------------
        page.navigate("https://demoqa.com");

        //Яное ожидание вместо Tread.sleep()
        page.waitForSelector(".card", new Page.WaitForSelectorOptions().setTimeout(10000));

        // ? -------------------- 2.ПОИСК ЭЛЕМЕНТОВ ---------------------------------

        
    }
}
