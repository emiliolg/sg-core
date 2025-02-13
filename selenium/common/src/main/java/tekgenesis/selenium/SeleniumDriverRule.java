
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.Select;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.tagName;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.tools.test.StatementBuilder.create;

/**
 * Rule for Selenium Tests.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber", "JavaDoc" })
public abstract class SeleniumDriverRule implements TestRule {

    //~ Instance Fields ..............................................................................................................................

    private RemoteWebDriver driver = null;

    //~ Methods ......................................................................................................................................

    @Override public Statement apply(final Statement statement, Description description) {
        return create(statement, this::after, this::before);
    }

    public void closeCurrentTab() {
        final Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.remove(driver.getWindowHandle());
        driver.executeScript("window.close()");
        if (!windowHandles.isEmpty()) driver.switchTo().window(Colls.immutable(windowHandles).getFirst().get());
    }

    public Option<ConfirmDom> confirmDom() {
        try {
            final ConfirmDom confirmDom = new ConfirmDom();
            return of(confirmDom);
        }
        catch (final Exception ignore) {
            return Option.empty();
        }
    }

    public void delay() {
        delay(DELAY);
    }

    public FormDom formDom(String formFqn) {
        return formDom(formFqn, "");
    }

    public FormDom formDom(String formFqn, String instanceId) {
        String url = baseUrl + "/#form/" + formFqn;
        if (!instanceId.isEmpty()) url += "/" + instanceId;

        driver.get(url);
        // waitForFeedback();
        assertThat(driver.getCurrentUrl()).contains(url);
        delay(500);
        return new FormDom(formFqn);
    }

    public FormDom formDomInNewTab(String formFqn, String instanceId) {
        String url = baseUrl + "/#form/" + formFqn;
        if (!instanceId.isEmpty()) url += "/" + instanceId;
        driver.executeScript("window.open(arguments[0])", url);
        driver.get(url);
        // waitForFeedback();
        driver.switchTo().window(Colls.immutable(driver.getWindowHandles()).revert().getFirst().get());
        assertThat(driver.getCurrentUrl()).contains(url);
        delay(500);
        return new FormDom(formFqn);
    }

    public void login() {
        login(user, password);
    }

    public void login(String username, String pass) {
        final String loginUrl = baseUrl + Constants.LOGIN_URI;
        driver.get(loginUrl);
        driver.findElementById(USERNAME).clear();
        driver.findElementById(USERNAME).sendKeys(username);
        driver.findElementById(PASSWORD).clear();
        driver.findElementById(PASSWORD).sendKeys(pass);
        byCss("button.btn.btn-primary").click();

        waitForUrl("/");
    }

    public void logout() {
        // noinspection SpellCheckingInspection
        driver.findElementById("userDropdown").click();
        delay(1000);
        byCss("a[href=\"/sg/logout\"]").click();

        waitForUrl(Constants.LOGIN_URI);
    }

    public Option<ModalDom> modalDom() {
        try {
            final ModalDom modalDom = new ModalDom();
            return of(modalDom);
        }
        catch (final Exception ignore) {
            return Option.empty();
        }
    }

    public void pressEnterAndDelay() {
        pressKeyAndDelay(Keys.ENTER);
    }

    public void pressKeyAndDelay(Keys key) {
        driver.getKeyboard().pressKey(key);
        delay();
    }

    public void reInitDriver()
        throws MalformedURLException
    {
        if (driver != null) driver.close();
        initDriver();
    }

    public Option<SwipeDom> swipeDom() {
        try {
            final SwipeDom swipeDom = new SwipeDom();
            return of(swipeDom);
        }
        catch (final Exception ignore) {
            return Option.empty();
        }
    }

    public void waitForFeedback() {
        final WebElement feedback = byCss("div.feedBack .fade");
        // wait until the feedback starts hiding
        int retries = 0;
        while (feedback.getAttribute("class").contains("in") && retries < 20) {
            checkCorrectFeedback();
            retries++;
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public Keyboard getKeyboard() {
        return driver.getKeyboard();
    }

    public abstract DriverService getService();
    protected abstract SeleniumBrowser getBrowser();

    private void after() {
        try {
            delay(3000);  // TODO a better way to wait processing to finish before logging out
            logout();
            takeScreenShot();
        }
        catch (final Throwable t) {
            takeScreenShot();
            throw t;
        }
        finally {
            driver.quit();
            try {
                Runtime.getRuntime().exec("killall -9 chrome").waitFor();
            }
            catch (final InterruptedException | IOException e) {
                // ignore
            }
        }
    }

    private Seq<WebElement> allByCss(String selector) {
        try {
            return toList(driver.findElementsByCssSelector(selector));
        }
        catch (final Throwable t) {
            throw new IllegalArgumentException("Elements not found: " + selector, t);
        }
    }

    private void before()
        throws Exception
    {
        initDriver();
        login();
    }

    private WebElement byCss(String selector) {
        try {
            return driver.findElementByCssSelector(selector);
        }
        catch (final Throwable t) {
            throw new IllegalArgumentException("Element not found: " + selector, t);
        }
    }

    private void checkCorrectFeedback() {
        try {
            delay();
            final WebElement msg = byCss("div.feedBack .fade div");
            if (msg.getAttribute(Constants.CLASS).contains("error")) throw new IllegalStateException("Error: " + msg.getText());
        }
        catch (final Exception ignored) {  // Ignored
        }
    }
    private void clickAndDelay(WebElement e) {
        if (!e.isDisplayed()) {
            final int elementY = e.getLocation().getY();
            driver.executeScript("window.scroll(0, " + elementY + ")");
        }
        e.click();
        // waitForFeedback();
    }

    private void delay(long delay) {
        try {
            Thread.sleep(delay);
        }
        catch (final InterruptedException ignore) {}
    }

    private void initDriver()
        throws MalformedURLException
    {
        switch (getBrowser()) {
        case CHROME:
            // To remove message "You are using an unsupported command-line flag: --ignore-certificate-errors.
            // Stability and security will suffer."
            // Add an argument 'test-type'
            final DesiredCapabilities chrome  = DesiredCapabilities.chrome();
            final ChromeOptions       options = new ChromeOptions();
            options.addArguments("test-type");
            options.addArguments("--no-sandbox");
            if (isNotEmpty(System.getProperty("suigen.selenium.userDataDir")))
                options.addArguments("--user-data-dir=" + System.getProperty("suigen.selenium.userDataDir"));
            chrome.setCapability(ChromeOptions.CAPABILITY, options);

            final String hubUrl = System.getProperty(SELENIUM_HUB_URL, "");

            driver = new RemoteWebDriver((!hubUrl.isEmpty() ? new URL(hubUrl) : getService().getUrl()), chrome);
            break;
        case FIREFOX:
            driver = new FirefoxDriver(DesiredCapabilities.firefox());
            break;
        }

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.manage().window().setPosition(new Point(0, 0));
        final String seleniumScreenResolution = System.getProperty("suigen.selenium.screenResolution");  // "height,width
        if (seleniumScreenResolution != null) {
            final String[] resValues = seleniumScreenResolution.split(",");
            driver.manage().window().setSize(new Dimension(Integer.parseInt(resValues[0]), Integer.parseInt(resValues[1])));
        }
    }

    private void sendKeyByKeyAndDelay(WebElement e, String text) {
        final Random r = new Random();
        for (final char c : text.toCharArray()) {
            e.sendKeys(String.valueOf(c));
            delay(r.nextInt(100));
        }
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private void sendKeysAndDelay(WebElement e, String text) {
        e.sendKeys(text);
    }

    private void takeScreenShot() {
        try {
            final File tmpDir = new File("target/selenium-photos");
            tmpDir.mkdirs();
            final File tempFile = File.createTempFile("screenshots-", ".jpg", tmpDir);
            logger.error("ScreenShot taken for selenium test  in: " + tempFile.getAbsolutePath());

            final FileOutputStream file       = new FileOutputStream(tempFile);
            final byte[]           screenShot = driver.getScreenshotAs(OutputType.BYTES);
            Files.copy(new ByteArrayInputStream(screenShot), file);
        }
        catch (final IOException e) {
            logger.error("Could not write screenShot for selenium test", e);
        }
    }

    private void waitForUrl(String toUrl) {
        final String url = baseUrl + toUrl;
        // wait for app page to change
        int retries = 0;
        while (!url.equals(driver.getCurrentUrl()) && retries < 10) {
            delay();
            retries++;
        }
        assertThat(retries).isLessThanOrEqualTo(10);
        // check that the new page is the app page
        assertThat(driver.getCurrentUrl()).endsWith(url);
    }

    private void setSuggestMailValue(WebElement e, String text, boolean retry) {
        final String value = text.replace(COVER_DELIMITER_CHAR, "");
        if (value.indexOf('@') >= 0) {
            try {
                // select and check that the first suggested option is the correct one
                if (e.getAttribute("value") != null) {
                    final int length = e.getAttribute("value").length();
                    for (int i = 0; i < length; i++)
                        e.sendKeys(Keys.BACK_SPACE);
                }

                delay(300);
                sendKeyByKeyAndDelay(e, value);
                delay(250);

                // noinspection SpellCheckingInspection
                final List<WebElement> parts = driver.findElementsByCssSelector(
                        "body > div.typeahead.dropdown-menu tr:first-child td[role=menuitem]");

                clickAndDelay(parts.get(parts.size() > 2 ? 1 : 0));
            }
            catch (final Throwable t) {
                if (!retry) throw new IllegalArgumentException("Trying to set '" + value + "'. But Suggest element was not found", t);
                else setSuggestMailValue(e, value, false);
            }
        }
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private void setSuggestValue(WebElement e, String value) {
        setSuggestValue(e, value, true);
    }

    private void setSuggestValue(WebElement e, String text, boolean retry) {
        final String value = text.replace(COVER_DELIMITER_CHAR, "");
        try {
            // select and check that the first suggested option is the correct one
            if (e.getAttribute("value") != null) {
                final int length = e.getAttribute("value").length();
                for (int i = 0; i < length; i++)
                    e.sendKeys(Keys.BACK_SPACE);
            }

            delay(300);
            sendKeyByKeyAndDelay(e, value);
            delay(250);

            // noinspection SpellCheckingInspection
            final List<WebElement> parts = driver.findElementsByCssSelector("body > div.typeahead.dropdown-menu tr:first-child td[role=menuitem]");

            final StringBuilder partsText = new StringBuilder();
            for (final WebElement part : parts) {
                if (isNotEmpty(part.getText())) {
                    if (partsText.length() > 0) partsText.append(" ");
                    partsText.append(part.getText());
                }
            }

            assertThat(partsText.toString().replace(COVER_DELIMITER_CHAR, "")).startsWith(value);
            clickAndDelay(parts.get(parts.size() > 2 ? 1 : 0));
        }
        catch (final Throwable t) {
            if (!retry) throw new IllegalArgumentException("Trying to set '" + value + "'. But Suggest element was not found", t);
            else setSuggestValue(e, value, false);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(SeleniumDriverRule.class);

    private static final Function<WebElement, String> WEB_ELEMENT_TO_TEXT = WebElement::getText;

    public static final String SELENIUM_HUB_URL = "selenium.hub.url";

    private static final String baseUrl  = System.getProperty("suigen.selenium.baseUrl", "http://localhost:8024");  // -Dsuigen.selenium.baseUrl=demo:8082
    private static final String user     = System.getProperty("suigen.selenium.user", "admin");                     // "demo"
    private static final String password = System.getProperty("suigen.selenium.password", "password");              // "d3m0123!"

    private static final String BUTTON_BTN_BTN_PRIMARY = "button.btn.btn-primary";
    private static final String USERNAME               = "username";
    private static final String PASSWORD               = "password";
    private static final String COVER_DELIMITER_CHAR   = "\u2009";
    public static final int     DELAY                  = 300;

    //~ Inner Classes ................................................................................................................................

    public class ConfirmDom {
        private final WebElement confirm;

        protected ConfirmDom() {
            confirm = driver.findElementByClassName("modal-alert");
        }

        public void cancel() {
            clickButton("Cancel");
        }

        public void ok() {
            clickButton("Ok");
        }

        public boolean isOpened() {
            return confirm.getAttribute("style").isEmpty();
        }

        private void clickButton(@NotNull final String label) {
            immutable(confirm.findElements(By.tagName("button"))).getFirst(b -> label.equals(b.getText())).ifPresent(WebElement::click);
        }
    }  // end class ConfirmDom

    public abstract class FieldFinder {
        protected final String formName;

        protected FieldFinder(String formFqn) {
            final int fqnIndex = formFqn.lastIndexOf('.');
            if (fqnIndex != -1) formName = formFqn.substring(fqnIndex + 1);
            else formName = formFqn;
        }

        public Button button(String label) {
            return new Button(findElement(label, "button"));
        }

        public Button buttonByClass(String clazz) {
            return new Button(driver.findElementByClassName(clazz));
        }

        public CheckBox checkBox(String id) {
            return new CheckBox(id);
        }

        public ComboBox comboBox(String id) {
            return new ComboBox(id);
        }

        public DateBox dateBox(String id) {
            return new DateBox(id);
        }

        public Display display(String id) {
            return new Display(id);
        }

        public Dropdown dropdown(String id) {
            return new Dropdown(id);
        }

        public Label label(String id) {
            return new Label(id);
        }

        public Button link(String id) {
            return new Button(findElement(id, "a"));
        }

        public Button linkByText(String text) {
            return new Button(driver.findElement(By.linkText(text)));
        }

        public MailBox mailbox(String id) {
            return new MailBox(id);
        }

        public Message message(String id) {
            return new Message(id);
        }

        public RadioGroup radioGroup(String id) {
            return new RadioGroup(id);
        }

        public void set(String id, String elemType, String value) {
            final WebElement e = findElement(id, elemType);
            e.clear();
            sendKeysAndDelay(e, value);
            assertThat(get(id, elemType)).isEqualTo(value);
        }

        public Subform subform(String id, String subformName) {
            return new Subform(formName, id, subformName);
        }

        public SuggestBox suggestBox(String id) {
            return new SuggestBox(id);
        }

        public TagComboBox tagComboBox(String id) {
            return new TagComboBox(id);
        }

        public Tags tags(String id) {
            return new Tags(id);
        }

        public TagsSuggestBox tagsSuggestBox(String id) {
            return new TagsSuggestBox(id);
        }

        public ElementType textArea(String id) {
            return new ElementType(id, "textarea");
        }

        public ElementType textField(String id) {
            return new ElementType(id, "input");
        }

        public ElementType textFieldParent(String id) {
            return new ElementType(id, "");
        }

        public Upload upload(String id) {
            return new Upload(id);
        }

        public WidgetDefDom widgetDef(String id) {
            return new WidgetDefDom(formName, id);
        }

        protected WebElement findElement(String id) {
            delay();
            return byCss("#" + formName + " #" + id);
        }

        protected WebElement findElement(String id, String elementType) {
            delay();
            return byCss("#" + formName + " #" + id + " " + elementType);
        }

        protected Seq<WebElement> findElements(String id, String elementType) {
            delay();
            return allByCss("#" + formName + " #" + id + " " + elementType);
        }

        protected Seq<WebElement> findElementsByClass(String className) {
            delay();
            return allByCss("#" + formName + " ." + className);
        }

        /** Look for a modal element, which is a child of the body, and not a child of the form. */
        protected WebElement findModalElement(String id, String elementType) {
            delay();
            return byCss("#" + id + " " + elementType);
        }

        Seq<String> listElements(String s) {
            return findElements(s, "div.tagDiv span").map(WebElement::getText).toList();
        }

        private String get(String id, String elemType) {
            return findElement(id, elemType).getAttribute("value");
        }

        public class Button extends SeleniumElement {
            protected WebElement element;

            public Button(WebElement element) {
                super("", "");  // not useful for buttons. See element() implementation.;
                this.element = element;
            }

            public void click() {
                final String oldUrl = driver.getCurrentUrl();
                clickAndDelay(element);
                assertThat(driver.getCurrentUrl()).endsWith(oldUrl);
            }

            public FormDom clickAndExpect(String expectedRedirectFormFqn) {
                clickAndDelay(element);
                delay(500);
                assertThat(driver.getCurrentUrl()).contains(expectedRedirectFormFqn);
                return new FormDom(expectedRedirectFormFqn);
            }

            public void confirmAction() {
                driver.findElementByXPath("//div[@class='okCancelModalFooter']//button").click();
            }

            public String getText() {
                return element.getText();
            }

            @Override protected WebElement element() {
                return element;
            }
        }

        public class CheckBox extends SeleniumElement {
            public CheckBox(String id) {
                super(id, "input");
            }

            public void setSelected(boolean selected) {
                final WebElement c = element();
                if (selected != c.isSelected()) clickAndDelay(c);  // toggle
            }
        }

        public class ComboBox extends SeleniumElement {
            public ComboBox(String id) {
                super(id, "select");
            }

            public void select(int index) {
                asSelect().selectByIndex(index);
            }

            public String getText() {
                return asSelect().getFirstSelectedOption().getText();
            }

            public void setValue(String value) {
                asSelect().selectByVisibleText(value);
                delay(100);
                assertThat(getText()).isEqualTo(value);
            }

            private Select asSelect() {
                return new Select(element());
            }
        }

        public class DateBox extends ElementType {
            public DateBox(String id) {
                super(id, "input");
            }

            public void setValue(String value) {
                set(id, tag, value);
                element().sendKeys(Keys.TAB);
            }
        }

        public class Display extends SeleniumElement {
            public Display(String id) {
                super(id, "span");
            }

            public String getText() {
                return element().getText();
            }
        }

        public class Dropdown extends SeleniumElement {
            public Dropdown(String id) {
                super(id, "div");
            }

            public void clickMenuItem(int idx) {
                final Seq<WebElement> lis = findElements(id, "li");
                if (lis.getSize().orElse(0) > idx) {
                    final WebElement       webElement = lis.toList().get(idx);
                    final List<WebElement> as         = webElement.findElements(By.tagName("a"));
                    for (final WebElement a : as) {
                        if (a.getAttribute("class").isEmpty()) a.click();
                    }
                }
            }

            public void openMenu() {
                final WebElement ddown  = findElement(id);
                final WebElement toggle = ddown.findElement(By.className("dropdown-toggle"));
                clickAndDelay(toggle);
            }
        }

        public class ElementType extends SeleniumElement {
            public ElementType(String id, String type) {
                super(id, type);
            }

            public String getStyle() {
                return element().getAttribute("style");
            }

            public String getText() {
                return element().getAttribute("value");
            }

            public void setValue(String value) {
                set(id, tag, value);
            }
        }

        public class Label extends SeleniumElement {
            public Label(String id) {
                super(id, "");
            }

            public void click() {
                element().findElement(By.tagName("a")).click();
            }
        }

        public class MailBox extends ElementType {
            public MailBox(String id) {
                super(id, "input");
            }

            @Override public void setValue(String value) {
                final WebElement e = element();
                setSuggestMailValue(e, value, true);
                assertThat(getText()).contains(value);
            }
        }

        public class Message extends SeleniumElement {
            public Message(String id) {
                super(id, "div");
            }

            public String getText() {
                return element().getText();
            }
        }

        public class RadioGroup extends SeleniumElement {
            public RadioGroup(String id) {
                super(id, "input[checked] + label");
            }

            public String getText() {
                return element().getText();
            }

            public void setText(int index) {
                final WebElement input;
                if (index < 0) input = findElement(id, "a");  // clear
                else {
                    final Seq<WebElement> inputs = findElements(id, "input");
                    input = inputs.toList().get(index);
                }
                clickAndDelay(input);
            }
        }  // end class RadioGroup

        public class SeleniumElement {
            final String id;
            final String tag;

            protected SeleniumElement(String id, String tag) {
                this.id  = id;
                this.tag = tag;
            }

            public boolean hasError() {
                return findElement(id).getAttribute("class").contains("has-error");
            }

            public boolean hasErrorInsideTable(int row) {
                return findElementsByClass("has-error").exists(e -> e != null && e.getAttribute("id").equals(id + "#" + row));
            }

            public boolean isDisplayed() {
                return element().isDisplayed();
            }
            public boolean isEnabled() {
                return element().isEnabled();
            }
            public boolean isFocused() {
                return element().getAttribute("class").contains("focused");
            }
            public boolean isSelected() {
                return element().isSelected();
            }

            protected WebElement element() {
                return findElement(id, tag);
            }
        }

        public class Subform extends SeleniumElement {
            private final FormDom subformDom;
            private final String  subformName;

            public Subform(String fatherFormFqn, String id, String subformName) {
                super(id, "div");
                this.subformName = subformName;
                subformDom       = new FormDom(subformName);
            }

            public void cancel() {
                clickAndDelay(byCss(".modal .modal-footer .cancel-subform"));
            }

            public void ok() {
                clickAndDelay(byCss(".modal .modal-footer .ok-subform"));
            }

            public void open() {
                clickAndDelay(element().findElement(tagName("a")));
                checkSubform();
            }

            public FormDom subformDom() {
                return subformDom;
            }

            private void checkSubform() {
                findModalElement(subformName, "div");

                byCss(".modal .modal-footer .ok-subform");
                byCss(".modal .modal-footer .cancel-subform");
            }
        }

        public class SuggestBox extends ElementType {
            public SuggestBox(String id) {
                super(id, "input");
            }

            @Override public void setValue(String value) {
                final WebElement e = element();
                setSuggestValue(e, value, true);
                assertThat(value).contains(getText());
            }

            public void setValue(String value, String description) {
                final WebElement e = element();
                setSuggestValue(e, description, true);
                assertThat(value).contains(getText());
            }
        }

        public class TagComboBox {
            private final String id;

            public TagComboBox(String id) {
                this.id = id;
            }

            public Seq<String> getList() {
                return listElements(id);
            }

            public void setValues(Iterable<String> values) {
                clickAndDelay(findElement(id, "i"));
                for (final String value : values)
                    clickItem(value);
            }

            private void clickItem(String value) {
                // noinspection SpellCheckingInspection
                for (final WebElement e : allByCss("body > div.typeahead.dropdown-menu label")) {
                    final String text = e.getText();
                    if (text.equals(value)) {
                        clickAndDelay(e);
                        assertThat(getList()).contains(text);
                        return;
                    }
                }
            }
        }

        public class Tags extends SeleniumElement {
            public Tags(String id) {
                super(id, "input");
            }

            public Seq<String> getList() {
                return listElements(id);
            }

            public void setValues(Iterable<String> values) {
                final WebElement e = element();
                e.clear();
                for (final String v : values) {
                    e.sendKeys(v);
                    pressEnterAndDelay();  // add
                    assertThat(getList()).contains(v);
                }
            }
        }

        public class TagsSuggestBox extends SuggestBox {
            public TagsSuggestBox(String id) {
                super(id);
            }

            public void addTag(String value) {
                setSuggestValue(element(), value, true);
            }

            public Seq<String> getTagsText() {
                return listElements(id);
            }

            @Override public void setValue(String value) {
                addTag(value);
            }
        }

        public class Upload extends SeleniumElement {
            public Upload(String id) {
                super(id, "input[type=\"text\"] " + "+ a");
            }  // In this case, the button acts as element.

            public int count() {
                return findElements(id, ".thumbnail").size() + findElements(id, "tr").size();
            }

            public void upload(String value) {
                final WebElement textInput    = findElement(id, "input[type=\"text\"]");
                final WebElement uploadButton = element();
                sendKeysAndDelay(textInput, value);
                clickAndDelay(uploadButton);
            }
        }
    }  // end class FieldFinder

    public class FormDom extends FieldFinder {
        public FormDom(String formFqn) {
            super(formFqn);
        }

        public FormDom cancel() {
            String expectUrl = driver.getCurrentUrl().substring(0, driver.getCurrentUrl().lastIndexOf('/'));
            try {
                byCss(".form-actions button.btn-danger");
            }
            catch (final Throwable t) {
                expectUrl = baseUrl + "/#";
            }

            return new Button(byCss(".form-actions > div > div:nth-child(2) button")).clickAndExpect(expectUrl);
        }

        public void delete() {
            new Button(byCss(".form-actions button.btn-danger")).clickAndExpect(
                driver.getCurrentUrl().substring(0, driver.getCurrentUrl().lastIndexOf('/')));
        }

        public Seq<String> feedback() {
            return feedbackWebElements().map(WEB_ELEMENT_TO_TEXT);
        }

        public boolean hasErrorAction() {
            return feedbackWebElements().exists(we -> we.getAttribute("class").contains("alert-danger"));
        }

        public boolean hasSuccessAction() {
            return feedbackWebElements().exists(we -> we.getAttribute("class").contains("alert-success"));
        }

        public boolean hasWarningAction() {
            return feedbackWebElements().exists(we -> we.getAttribute("class").contains("alert-warning"));
        }

        public void saveAndExpect(String expectedRedirectUrl) {
            new Button(byCss(".form-actions " + BUTTON_BTN_BTN_PRIMARY)).clickAndExpect(expectedRedirectUrl);
        }

        public void saveAndStay() {
            boolean hasDelete = true;
            try {
                byCss(".form-actions button.btn-danger");
            }
            catch (final Throwable e) {
                hasDelete = false;
            }

            if (hasDelete) saveAndExpect(driver.getCurrentUrl().substring(0, driver.getCurrentUrl().lastIndexOf('/')));
            else saveAndExpect(driver.getCurrentUrl());
        }

        public void search(String instance) {
            search(instance, false);
        }

        public void search(String instance, boolean includeDeprecated) {
            if (includeDeprecated) byCss(".page-header input[type='checkbox']").click();
            setSuggestValue(byCss(".page-header input"), instance);
            delay();
        }

        public SectionDom section(String id) {
            return new SectionDom(formName, byCss("#" + id));
        }

        public void selectTab(String tabsId, int tabNumber) {
            final Seq<WebElement> tabs = findElements(tabsId, "ul.nav > li > a");
            final WebElement      tab  = tabs.toList().get(tabNumber - 1);
            clickAndDelay(tab);
        }

        public TableDom table(String id) {
            return new TableDom(formName, byCss("#" + id));
        }

        public void toggleDeprecate() {
            new Button(byCss(".form-actions button.btn-warning")).clickAndExpect(driver.getCurrentUrl());
        }

        @NotNull private Seq<WebElement> feedbackWebElements() {
            return allByCss("div.feedBack .fade.in div");
        }
    }  // end class FormDom

    // all field finder utilities must be overriden and search inside modal
    public class ModalDom extends FieldFinder {
        private final WebElement modal;

        protected ModalDom() {
            super("");
            modal = driver.findElementByClassName("modal-box");
        }

        public void close() {
            final WebElement close = findElementByClass("close");
            close.click();
        }

        @Override public ElementType textField(String id) {
            return super.textField(id + "I");
        }

        public boolean isOpened() {
            return modal.getAttribute("style").isEmpty();
        }

        @Override protected WebElement findElement(String id) {
            delay();
            return byCss(".modal-box #" + id);
        }

        @Override protected WebElement findElement(String id, String elementType) {
            return findElement(id);
        }

        protected WebElement findElementByClass(String className) {
            delay();
            return byCss(".modal-box ." + className);
        }

        protected WebElement findElementNotInActive(String id) {
            delay();
            return byCss(".modal-box #" + id);
        }
        @Override protected Seq<WebElement> findElements(String id, String elementType) {
            delay();
            return allByCss(".modal-box #" + id + " " + elementType);
        }

        @Override protected Seq<WebElement> findElementsByClass(String className) {
            delay();
            return allByCss(".modal-box ." + className);
        }
    }  // end class ModalDom

    public class RowDom extends FieldFinder {
        private final WebElement element;

        private final int row;

        public RowDom(String formFqn, int row, WebElement element) {
            super(formFqn);
            this.row     = row;
            this.element = element;
        }

        public void click() {
            final String oldUrl = driver.getCurrentUrl();
            clickAndDelay(element);
            assertThat(driver.getCurrentUrl()).endsWith(oldUrl);
        }
        @Override protected WebElement findElement(String id, String elementType) {
            return super.findElement(id + "\\#" + row, elementType);
        }

        @Override protected Seq<WebElement> findElements(String id, String elementType) {
            return super.findElements(id + "\\#" + row, elementType);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public class SectionDom {
        private final String     formName;
        private final WebElement section;

        public SectionDom(String formName, WebElement webElement) {
            this.formName = formName;
            section       = webElement;
        }

        public SectionRowDom section(int row) {
            return new SectionRowDom(formName, row);
        }

        public boolean isDisplayed() {
            return section.isDisplayed();
        }
        public boolean isEnabled() {
            return section.isEnabled();
        }
        public boolean isSelected() {
            return section.isSelected();
        }
    }

    public class SectionRowDom extends FieldFinder {
        private final int row;

        public SectionRowDom(String formFqn, int row) {
            super(formFqn);
            this.row = row;
        }

        @Override protected WebElement findElement(String id, String elementType) {
            return super.findElement(id + "\\#" + row, elementType);
        }

        @Override protected Seq<WebElement> findElements(String id, String elementType) {
            return super.findElements(id + "\\#" + row, elementType);
        }
    }

    public class SwipeDom extends FieldFinder {
        private final WebElement swipe;

        protected SwipeDom() {
            super("");
            swipe = driver.findElementByClassName("swipePopup");
        }

        public void close() {
            final Seq<WebElement> closes = findElementsByClassNotInActive("swipeCloseContainer");
            if (!closes.isEmpty()) closes.getFirst().get().click();
        }

        @Override protected WebElement findElement(String id) {
            delay();
            return byCss(".swipePopup .swipeview-active #" + id);
        }

        @Override protected WebElement findElement(String id, String elementType) {
            delay();
            return byCss(".swipePopup .swipeview-active #" + id + " " + elementType);
        }

        protected WebElement findElementNotInActive(String id) {
            delay();
            return byCss(".swipePopup #" + id);
        }
        @Override protected Seq<WebElement> findElements(String id, String elementType) {
            delay();
            return allByCss(".swipePopup .swipeview-active #" + id + " " + elementType);
        }

        @Override protected Seq<WebElement> findElementsByClass(String className) {
            delay();
            return allByCss(".swipePopup .swipeview-active ." + className);
        }

        protected Seq<WebElement> findElementsByClassNotInActive(String className) {
            delay();
            return allByCss(".swipePopup ." + className);
        }

        protected Seq<WebElement> findElementsNotInActive(String id, String elementType) {
            delay();
            return allByCss(".swipePopup #" + id + " " + elementType);
        }
    }  // end class SwipeDom

    public class TableDom {
        private final String     formName;
        private final WebElement table;

        public TableDom(String formName, WebElement webElement) {
            table         = webElement;
            this.formName = formName;
        }

        public RowDom row(int row) {
            return new RowDom(formName, row, getRow(row + 1));
        }

        public int rowCount() {  // noinspection SpellCheckingInspection
            return table.findElements(By.cssSelector("table > tbody > tr:not(.hide)")).size();
        }

        public boolean isDisplayed() {
            return table.isDisplayed();
        }
        public boolean isEnabled() {
            return table.isEnabled();
        }
        public boolean isSelected() {
            return table.isSelected();
        }

        private WebElement getRow(int rowIndex) {
            // noinspection SpellCheckingInspection
            final String selector = "table > tbody > tr:nth-child(" + rowIndex + ")";
            return table.findElement(By.cssSelector(selector));
        }
    }

    public class WidgetDefDom extends FormDom {
        private final WebElement element;
        private final String     widgetDef;

        public WidgetDefDom(String formName, String defName) {
            super(formName);
            widgetDef = defName;
            element   = findElement(widgetDef, "");
        }

        /** Returns true if the WidgetDef has label. */
        public boolean hasLabel() {
            return element.getAttribute("class").contains("named");
        }

        @Override public FieldFinder.ElementType textField(String id) {
            return super.textField(widgetDef + "\\." + id);
        }

        /** Returns the WidgetDef icon if it has one, empty if it doesn't. */
        @NotNull public String getIcon() {
            if (!hasLabel()) return "";
            final WebElement i = element.findElement(By.cssSelector("h4 > i"));
            if (i == null) return "";
            final Seq<String> icon = fromArray(i.getAttribute("class").split("\\s+")).filter(s -> s.startsWith("fa-"));
            return icon.isEmpty() ? "" : icon.getFirst().get().replace("fa-", "");
        }

        /** Returns the WidgetDef label if it has one, empty if it doesn't. */
        @NotNull public String getLabel() {
            return !hasLabel() ? "" : element.findElement(By.tagName("h4")).getText();
        }

        /** Returns true if the WidgetDef is hidden. */
        public boolean isHidden() {
            return element.getAttribute("style").contains("display: none;");
        }

        /** Returns the WidgetDef style if it has one, empty if it doesn't. */
        @NotNull public String getStyleClasses() {
            return element.getAttribute("class");
        }
    }  // end class WidgetDefDom
}  // end class SeleniumDriverRule
