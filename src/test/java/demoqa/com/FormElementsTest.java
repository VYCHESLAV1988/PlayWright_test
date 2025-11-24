package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FormElementsTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {

        // Создаем контекст без viewport → окно будет максимального размера
        context = browser.newContext();

        // Создаем вкладку
        page = context.newPage();
    }

    @BeforeAll
    static void setUp() {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)
        );
    }

    @Test
    @DisplayName("Полный тест регистрации студента")
    void testFormElements() {

        // ---------------------- Переход на форму ------------------------
        page.navigate("https://demoqa.com/automation-practice-form");

        // Убираем рекламу (иначе перекрывает State/City)
        page.evaluate("() => document.getElementById('fixedban')?.remove()");
        page.evaluate("() => document.querySelector('footer')?.remove()");
        System.out.println("Открыли форму, убрали рекламу");

        // ---------------------- First Name ------------------------
        page.locator("#firstName").fill("Slava");
        assertEquals("Slava", page.locator("#firstName").inputValue());
        System.out.println("Ввел First Name");

        // ---------------------- Last Name ------------------------
        page.locator("#lastName").fill("Vasiltsov");
        assertEquals("Vasiltsov", page.locator("#lastName").inputValue());
        System.out.println("Ввел Last Name");

        // ---------------------- Email ------------------------
        page.locator("#userEmail").fill("test@test.com");
        assertEquals("test@test.com", page.locator("#userEmail").inputValue());
        System.out.println("Ввел Email");

        // ---------------------- Gender ------------------------
        page.locator("label[for='gender-radio-1']").click();
        assertTrue(page.locator("#gender-radio-1").isChecked());
        System.out.println("Выбрал Male");

        // ---------------------- Mobile ------------------------
        page.locator("#userNumber").fill("0501234567");
        assertEquals("0501234567", page.locator("#userNumber").inputValue());
        System.out.println("Ввел номер телефона");

        // ---------------------- Date of Birth ------------------------
        page.locator("#dateOfBirthInput").click();
        page.locator(".react-datepicker__year-select").selectOption("2025");
        page.locator(".react-datepicker__month-select").selectOption("November");
        page.locator(".react-datepicker__day--024").click();
        System.out.println("Выбрал дату рождения");

        // ---------------------- Subjects ------------------------
        page.locator("#subjectsInput").fill("Maths");
        page.keyboard().press("Enter");
        assertTrue(page.locator(".subjects-auto-complete__multi-value").isVisible());
        System.out.println("Выбрал предмет Maths");

        // ---------------------- Хобби ------------------------
        page.locator("label[for='hobbies-checkbox-1']").click(); // Sport
        page.locator("label[for='hobbies-checkbox-2']").click(); // Reading
        System.out.println("Выбрал хобби Sport + Reading");

        // ---------------------- Upload picture ------------------------
        // ВНИМАНИЕ — файл должен существовать в resources !!!
        String filePath = Paths.get("src/test/resources/test.png").toAbsolutePath().toString();

        page.setInputFiles("#uploadPicture", Paths.get(filePath));
        System.out.println("Загрузил файл: " + filePath);

        // ---------------------- Current Address ------------------------
        page.locator("#currentAddress").fill("Israel, Haifa");
        System.out.println("Ввел текущий адрес");

        // ---------------------- State ------------------------
        page.locator("#state").click();
        page.locator("#react-select-3-option-0").click(); // NCR
        System.out.println("Выбрал State NCR");

        // Ждём 1 секунду
        page.waitForTimeout(1000);

        // ---------------------- City ------------------------
        page.locator("#city").click();
        page.locator("#react-select-4-option-0").click(); // Delhi
        System.out.println("Выбрал City Delhi");

        // ---------------------- Проверки State/City ------------------------
        assertEquals("NCR", page.locator("#state .css-1ucc91-singleValue").innerText());
        assertEquals("Delhi", page.locator("#city .css-1ucc91-singleValue").innerText());

        System.out.println("Проверки State/City прошли успешно");
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
