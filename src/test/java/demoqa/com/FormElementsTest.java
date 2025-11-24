package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormElementsTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {

        // Делаем окно FullHD — иначе элементы State/City не видны
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));

        page = context.newPage();
    }

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(400));
    }

    @Test
    @DisplayName("Работа с элементами формы")
    void testFormElements() {

        // Переход на форму
        page.navigate("https://demoqa.com/automation-practice-form");

        // Удаляем рекламу — она закрывает City
        page.evaluate("() => document.getElementById('fixedban')?.remove()");
        page.evaluate("() => document.querySelector('footer')?.remove()");

        // 1. Radio button
        page.locator("label[for='gender-radio-1']").click();
        assertTrue(page.locator("#gender-radio-1").isChecked());

        // 2. Checkboxes
        page.locator("label[for='hobbies-checkbox-1']").click();
        page.locator("label[for='hobbies-checkbox-3']").click();
        assertTrue(page.locator("#hobbies-checkbox-1").isChecked());
        assertTrue(page.locator("#hobbies-checkbox-3").isChecked());

        // Снимаем Music
        page.locator("label[for='hobbies-checkbox-3']").click();
        assertTrue(!page.locator("#hobbies-checkbox-3").isChecked());

        // 3. Работа с STATE
        page.locator("#state").scrollIntoViewIfNeeded();
        page.locator("#state").click();
        page.locator("#react-select-3-option-0").click(); // NCR

        // Проверяем что выбралось
        String selectedState = page.locator("#state .css-1ucc91-singleValue").innerText();
        assertEquals("NCR", selectedState);

        // Ждем пока City станет активным
        page.waitForSelector("#city input:not([disabled])");

        // 4. Работа с CITY
        page.locator("#city").scrollIntoViewIfNeeded();
        page.locator("#city").click();
        page.locator("#react-select-4-option-0").click(); // Delhi

        String selectedCity = page.locator("#city .css-1ucc91-singleValue").innerText();
        assertEquals("Delhi", selectedCity);
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}
