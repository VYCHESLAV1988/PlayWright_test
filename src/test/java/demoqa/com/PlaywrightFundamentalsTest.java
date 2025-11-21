package demoqa.com;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    static void closeBrowser(){
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

        //Явное ожидание вместо Tread.sleep()
        page.waitForSelector(".card", new Page.WaitForSelectorOptions().setTimeout(10000));

        // ? -------------------- 2.ПОИСК ЭЛЕМЕНТОВ ---------------------------------

        //Стабильный CSS селектор
        Locator elementsCard = page.locator("div.card:has-text('Elements')");
        elementsCard.click();

        //Поиск по тексту
        page.locator("li.btn-light:has-text('Text Box')").click();

        //Поиск по роли ARIA (лучшая практика используется для кнопок и ссылок)
        Locator fullNameLabel = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Full Name"));

        // ? -------------------- 3.ВЗАИМОДЕЙСТВИЕ С ЭЛЕМЕНТАМИ ---------------------------------

        //Fill vs Type
        fullNameLabel.fill("Иван Иванов"); // Быстрое заполнение

        Locator emailInput = page.locator("#userEmail");
        emailInput.type("test@example.com"); // Посимвольный ввод почты

        Locator addressArea = page.locator("#currentAddress");
        addressArea.fill("Ул. Пушкина, д. Колотушкина");

        //Клик по кнопки
        Locator submitButton = page.locator("#submit");
        submitButton.click();

        // ? -------------------- 4.ПРОВЕРКА И ПОЛУЧЕНИЕ ДАННЫХ ---------------------------------

        //Ожидание появления результата - (после кнопки сабмит результат появляется не сразу!)
        page.waitForSelector("#output");

        //Проверка текста (ассерт - важно знать!)
        Locator nameResult = page.locator("#name");
        assertTrue(nameResult.textContent().contains("Иван Иванов"),
                "Неверное имя в результате");

        //Проверка атрибута
        Locator emailResult = page.locator("#email");
        assertEquals(":test@example.com", emailResult.textContent().replace("Email",
                "").trim(), "Неверный email в результате");

        // ? -------------------- 5.РАБОТА С ЧЕКБОКСАМИ И РАДИО КНОПКАМИ ---------------------------------

        page.locator("li:has-text('Check Box')").click();

        //Чекбоксы
        Locator homeCheckbox = page.locator("label:has-text('Home') .rct-checkbox");
        homeCheckbox.check();
        assertTrue(homeCheckbox.isChecked(), "Чекбокс Home должен быть выбран");

        //Радио кнопка
        page.locator("li:has-text('Radio Button')").click();

        Locator impressiveRadio = page.locator("label:has-text('Impressive')");
        impressiveRadio.check();
        assertTrue(impressiveRadio.isChecked(), "Радио кнопка Impressive должна быть выбрана");


    }
}
