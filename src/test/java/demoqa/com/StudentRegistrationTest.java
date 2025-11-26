package demoqa.com;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRegistrationTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {

        // 0. ================== Создаем контекст и вкладку ========================
        context = browser.newContext();
        page = context.newPage();

        // проверяем что вкладка создана
        assertNotNull(page, "Страница должна была создаться!");
    }

    @BeforeAll
    static void setUp() {

        // 0.1 ================== Запуск Playwright и браузера =====================
        playwright = Playwright.create();
        assertNotNull(playwright, "Playwright должен был запуститься!");

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)   // замедление
        );

        assertNotNull(browser, "Браузер должен был запуститься!");
    }

    @Test
    @DisplayName("Полный тест регистрации студента")
    void testFormElements() {

        // 1. =================== Переход на форму ================================
        page.navigate("https://demoqa.com/automation-practice-form");

        assertTrue(page.url().contains("automation-practice-form"),
                "URL должен содержать automation-practice-form");

        // убираем рекламу
        page.evaluate("() => document.getElementById('fixedban')?.remove()");
        page.evaluate("() => document.querySelector('footer')?.remove()");

        System.out.println("Открыли форму, убрали рекламу");


        // 2. =================== First Name ======================================
        page.locator("#firstName").fill("Slava");

        assertEquals("Slava", page.locator("#firstName").inputValue(),
                "First Name заполнен неправильно!");

        System.out.println("Ввел First Name");


        // 3. =================== Last Name =======================================
        page.locator("#lastName").fill("Vasiltsov");

        assertEquals("Vasiltsov", page.locator("#lastName").inputValue(),
                "Last Name заполнен неправильно!");

        System.out.println("Ввел Last Name");


        // 4. =================== Email ===========================================
        page.locator("#userEmail").fill("test@test.com");

        assertEquals("test@test.com", page.locator("#userEmail").inputValue(),
                "Email заполнен неправильно!");

        System.out.println("Ввел Email");


        // 5. =================== Gender ==========================================
        page.locator("label[for='gender-radio-1']").click();

        assertTrue(page.locator("#gender-radio-1").isChecked(),
                "Gender Male должен быть выбран!");

        System.out.println("Выбрал Male");


        // 6. =================== Mobile ==========================================
        page.locator("#userNumber").fill("0501234567");

        assertEquals("0501234567", page.locator("#userNumber").inputValue(),
                "Номер телефона заполнен неправильно!");

        System.out.println("Ввел номер телефона");


        // 7. =================== Date of Birth ===================================
        page.locator("#dateOfBirthInput").click();
        page.locator(".react-datepicker__year-select").selectOption("2025");
        page.locator(".react-datepicker__month-select").selectOption("November");
        page.locator(".react-datepicker__day--024").click();
        assertTrue(page.locator("#dateOfBirthInput").inputValue().contains("2025"),
                "Дата не выбрана корректно!");
        System.out.println("Выбрал дату рождения");


        // 8. =================== Subjects ========================================
        page.locator("#subjectsInput").fill("Maths");
        page.keyboard().press("Enter");
        assertTrue(page.locator(".subjects-auto-complete__multi-value").isVisible(),
                "Предмет Maths не выбран!");
        System.out.println("Выбрал предмет Maths");


        // 9. =================== Хобби (играем с чекбоксами) ===========================
        // 1) Выбираем Sport + Reading
        page.locator("label[for='hobbies-checkbox-1']").click(); // Sport
        page.locator("label[for='hobbies-checkbox-2']").click(); // Reading
        assertTrue(page.locator("#hobbies-checkbox-1").isChecked(),
                "Sport должен быть выбран!");
        assertTrue(page.locator("#hobbies-checkbox-2").isChecked(),
                "Reading должен быть выбран!");
        System.out.println("Выбрал Sport + Reading");


        // 2) Снимаем Reading
        page.locator("label[for='hobbies-checkbox-2']").click();
        assertFalse(page.locator("#hobbies-checkbox-2").isChecked(),
                "Reading должен быть снят!");
        assertTrue(page.locator("#hobbies-checkbox-1").isChecked(),
                "Sport должен остаться выбран!");
        System.out.println("Снял Reading, Sport остался");


        // 3) Выбираем чекбокс Music
        page.locator("label[for='hobbies-checkbox-3']").click();
        assertTrue(page.locator("#hobbies-checkbox-3").isChecked(),
                "Music должен быть выбран!");
        System.out.println("Выбрал Music");


        // 4) Снимаем чекбокс Sport
        page.locator("label[for='hobbies-checkbox-1']").click();
        assertFalse(page.locator("#hobbies-checkbox-1").isChecked(),
                "Sport должен быть снят!");
        assertFalse(page.locator("#hobbies-checkbox-2").isChecked(),
                "Reading должен быть снят!");
        assertTrue(page.locator("#hobbies-checkbox-3").isChecked(),
                "Должно остаться только Music!");
        System.out.println("Оставил только Music");


        // 10. ================== Загрузка файла ==================================
        FileChooser fileChooser = page.waitForFileChooser(() ->
                page.locator("input#uploadPicture").click()
        );
        fileChooser.setFiles(Paths.get("C:\\Tools\\test.png"));
        assertTrue(Paths.get("C:\\Tools\\test.png").toFile().exists(),
                "Файл test.png должен существовать!");
        System.out.println("Файл успешно прикреплён!");

        // 11. ================== Current Address =================================
        page.locator("#currentAddress").fill("Israel, Haifa");

        assertTrue(page.locator("#currentAddress").inputValue().contains("Haifa"),
                "Адрес заполнен неправильно!");

        System.out.println("Ввел текущий адрес");


        // 12. ================== State ===========================================
        page.locator("#state").click();
        page.locator("#react-select-3-option-0").click(); // NCR

        // стабильный локатор вместо твоего
        String state = page.locator("#state div[class*='singleValue']").textContent().trim();

        assertEquals("NCR", state, "State выбран неправильно!");

        System.out.println("Выбрал State NCR");


        // 13. ================== City ============================================
        page.locator("#city").click();
        page.locator("#react-select-4-option-0").click(); // Delhi

        String city = page.locator("#city div[class*='singleValue']").textContent().trim();

        assertEquals("Delhi", city, "City выбран неправильно!");

        System.out.println("Выбрал City Delhi");


        // 14. ================== Submit ==========================================
        page.locator("#submit").scrollIntoViewIfNeeded();
        page.locator("#submit").click(new Locator.ClickOptions().setForce(true));

        // проверяем что модалка открылась
        assertTrue(page.locator("#example-modal-sizes-title-lg").isVisible(),
                "Модальное окно должно было появиться!");

        System.out.println("Нажали Submit");

        // 15. ================== Скриншот таблицы регистрации =======================

        // Локатор таблицы
        Locator table = page.locator(".table-responsive");

        // Проверяем, что таблица видна
        assertTrue(table.isVisible(), "Таблица регистрации не отображается после Submit!");

        // Создаем уникальное имя по миллисекундам
        String fileName = "registration_screenshot_" + System.currentTimeMillis() + ".png";

        // ПОЛНЫЙ путь к папке (как ты просил)
        String folderPath = "src/test/java/demoqa/com/registationscreenshots/";

        // Создаем папку, если её нет
        java.io.File folder = new java.io.File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Полный путь к файлу
        String screenshotPath = folderPath + fileName;

        // Делаем скриншот таблицы
        table.screenshot(new Locator.ScreenshotOptions()
                .setPath(Paths.get(screenshotPath)));

        // Проверка
        assertTrue(Paths.get(screenshotPath).toFile().exists(),
                "Скриншот таблицы регистрации не был создан!");
        System.out.println("Скриншот таблицы регистрации сохранён: " + screenshotPath);

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
