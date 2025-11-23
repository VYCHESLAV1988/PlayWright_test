package demoqa.com;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdvancedWaitsDemo {

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

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }


    @Test
    void testWaitsInRealScenario() {
        // ? -------------------- 1.АВТОМАТИЧЕСКОЕ  ОЖИДАНИЯ ---------------------------
        page.navigate("https://demoqa.com/dynamic-properties");

        //Кнопка станет активна через 5 секунд - автоожидание сработает!
        page.locator("#enableAfter").click();

        //Поле появится через 5 секунд
        page.locator("#visibleAfter").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        page.locator("#visibleAfter").click();

        // ? -------------------- 2.ЯВНОЕ ОЖИДАНИЯ ДЛЯ СЛОЖНЫХ УСЛОВИЙ ---------------------------
        //Ждем появление элемента с таймаутом 7 секунд!
        page.waitForSelector("#visibleAfter", new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(7000));

        //Ожидаем изменения CSS- своиства (кастомное условие)
        page.waitForFunction(
                "() => window.getComputedStyle(document.querySelector('#colorChange')).color === 'rgb(220, 53, 69)'");

        //Ожидание перехода на страницу
        page.navigate("https://demoqa.com");
        page.locator("text=Elements").click();

        page.waitForURL(
                "**/elements",
                new Page.WaitForURLOptions().setTimeout(5000)
        );

        // ОБЯЗАТЕЛЬНО!!!
        // Возвращаемся на страницу с тестовым текстом
        page.navigate("https://demoqa.com/dynamic-properties");




        // ? -------------------- 3.УМНЫЕ АССЕРТЫ С ОЖИДАНИЯМИ ---------------------------
        //Проверка текста с автоматическим ожиданием
        assertThat(
                page.getByText("This text has random Id", new Page.GetByTextOptions().setExact(true))
        ).hasText(
                "This text has random Id",
                new LocatorAssertions.HasTextOptions().setTimeout(5000)
        );

        //Правельная проверка видимости и активности
        Locator checkoutButton = page.locator("#visibleAfter");

        //Отдельные ассерты для каждого условия
        assertThat(checkoutButton).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(6000));

        //Дополнительная проверка атрибута
        assertThat(checkoutButton)
                .hasAttribute("type", "button",
                        new LocatorAssertions.HasAttributeOptions().setTimeout(6000));
        //Важно если тест падает с ошибкой not visible - это не ошибка теста а всего напросто елемент или кнопка еще не прогрузилась!
        //Требуется тогда увеличеть время ожидания с 5000 == до 7000 итд

    }


}
