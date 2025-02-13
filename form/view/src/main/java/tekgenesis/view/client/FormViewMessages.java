
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.i18n.client.Messages;

import tekgenesis.common.env.i18n.I18nMessages;
import tekgenesis.common.env.i18n.I18nMessagesFactory;

/**
 * Form Messages class.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "ClassWithTooManyMethods", "OverlyComplexClass" })
public interface FormViewMessages extends I18nMessages {

    //~ Instance Fields ..............................................................................................................................

    FormViewMessages MSGS = I18nMessagesFactory.create(FormViewMessages.class);

    //~ Methods ......................................................................................................................................

    /**  */
    @Messages.DefaultMessage("Add")
    String add();

    /**  */
    @Messages.DefaultMessage("Add...")
    String addATag();

    /**  */
    @DefaultMessage("After")
    String after();

    /**  */
    @DefaultMessage("After or equals to")
    String afterOrEquals();

    /**  */
    @DefaultMessage("Align Center")
    String alignCenter();

    /**  */
    @DefaultMessage("Align Left")
    String alignLeft();

    /**  */
    @DefaultMessage("Align Right")
    String alignRight();

    /**  */
    @DefaultMessage("A new application version is available, please refresh your browser to continue working.")
    String appOutOfDate();

    /**  */
    @Messages.DefaultMessage("You have unsaved changes, discard them?")
    String areYouSure();

    /**  */
    @DefaultMessage("Average")
    String average();

    /**  */
    @DefaultMessage("Before")
    String before();

    /**  */
    @DefaultMessage("Before or equals to")
    String beforeOrEquals();

    /**  */
    @DefaultMessage("Begins with")
    String beginsWith();

    /**  */
    @DefaultMessage("Bold")
    String bold();

    /**  */
    @DefaultMessage("Break Link")
    String breakLink();

    /**  */
    @Messages.DefaultMessage("Upload")
    String browse();

    /**  */
    @DefaultMessage("Calculator")
    String calculator();

    /**  */
    @DefaultMessage("Adds the number to memory")
    String calculatorAddMemory();

    /**  */
    @DefaultMessage("Appends three zeros")
    String calculatorAppendsThreeZeros();

    /**  */
    @DefaultMessage("Appends two zeros")
    String calculatorAppendsTwoZeros();

    /**  */
    @DefaultMessage("Removes last digit")
    String calculatorBackspace();

    /**  */
    @DefaultMessage("Changes displayed number''s sign")
    String calculatorChangeSign();

    /**  */
    @DefaultMessage("Cleans the number in memory")
    String calculatorCleanMemory();

    /**  */
    @DefaultMessage("Clears and reset the display and memory")
    String calculatorClear();

    /**  */
    @DefaultMessage("Exchanges the number with memory")
    String calculatorExchangeMemory();

    /**  */
    @DefaultMessage("Calculates factorial")
    String calculatorFactorial();

    /**  */
    @DefaultMessage("Inserts number to memory")
    String calculatorInsertMemory();

    /**  */
    @DefaultMessage("Recovers number from memory")
    String calculatorRecoverMemory();
    /**  */
    @DefaultMessage("Opens calculator")
    String calculatorShortcut();

    /**  */
    @DefaultMessage("Subtracts the number to memory")
    String calculatorSubtractMemory();

    /**  */
    @Messages.DefaultMessage("Cancel")
    String cancel();

    /**  */
    @Messages.DefaultMessage("<strong>Canceled</strong>")
    String canceled();

    /**  */
    @Messages.DefaultMessage("Canceling...")
    String canceling();

    /**  */
    @DefaultMessage("Switch View HTML/Source")
    String changeView();

    /**  */
    @Messages.DefaultMessage("Choose one:")
    String chooseOne();

    /**  */
    @Messages.DefaultMessage("Clear")
    String clear();

    /**  */
    @Messages.DefaultMessage("Close")
    String close();

    /**  */
    @DefaultMessage("Colors")
    String colors();

    /**  */
    @DefaultMessage("Every common calculator behaviour")
    String commonCalculatorBehavior();

    /**  */
    @DefaultMessage("Contains")
    String contains();

    /**  */
    @DefaultMessage("Count")
    String count();

    /**  */
    @Messages.DefaultMessage("Create")
    String create();

    /**  */
    @Messages.DefaultMessage("Create new...")
    String createNew();

    /**  */
    @Messages.DefaultMessage("Delete")
    String delete();

    /**  */
    @Messages.DefaultMessage("<strong>Deleted</strong>")
    String deleted();

    /**  */
    @Messages.DefaultMessage("Deleting...")
    String deleting();

    /**  */
    @Messages.DefaultMessage("Deactivate")
    String deprecate();

    /**  */
    @DefaultMessage("Description")
    String description();

    /**  */
    @DefaultMessage("Domain {0} doesnt exist.")
    String domainDoesntExist(String domain);

    /**  */
    @DefaultMessage("Download")
    String download();

    /**  */
    @DefaultMessage("Could not validate due to connection issues.")
    String emailCouldntConnect();

    /**  */
    @DefaultMessage("Mail {0} doesnt exist.")
    String emailDoesntExist(String mail);

    /**  */
    @Messages.DefaultMessage("No rows.")
    String emptyTable();

    /**  */
    @DefaultMessage("Ends with")
    String endsWith();

    /**  */
    @DefaultMessage("Equals")
    String equals();

    /**  */
    @Messages.DefaultMessage("Error")
    String error();

    /**  */
    @DefaultMessage("Error reaching server. {0}")
    String errorReachingServer(String message);

    /**  */
    @DefaultMessage("Error reaching server. (HTTP Error code: {0})")
    String errorWithStatusCode(String code);

    /**  */
    @DefaultMessage("Executing...")
    String executing();

    /**  */
    @Messages.DefaultMessage("Export")
    String export();

    /**  */
    @DefaultMessage("False")
    String falseLabel();

    /**  */
    @Messages.DefaultMessage("Favorites")
    String favorites();

    /**  */
    @Messages.DefaultMessage("Star button or f to add current form")
    String favoritesInfo1();

    /**  */
    @Messages.DefaultMessage("Press 1-9 to access form")
    String favoritesInfo2();

    /**  */
    @Messages.DefaultMessage("First form is declared Home")
    String favoritesInfo3();

    /**  */
    @Messages.DefaultMessage("Rearrange order by dragging")
    String favoritesInfo4();

    /**  */
    @Messages.DefaultMessage("Only favorites 1 to 9 will be accessible with keyboard shortcuts")
    String favoritesWarning();

    /**  */
    @DefaultMessage("Grrrr... reporting service is not available.")
    String feedbackError();

    /**  */
    @DefaultMessage("Thanks! Report was successfully sent.")
    String feedbackSuccess();

    /**  */
    @Messages.DefaultMessage("Fetching...")
    String fetching();

    /**  */
    @Messages.DefaultMessage("First")
    String first();

    /**  */
    @Messages.DefaultMessage("Focus tab or field number")
    String focusTabOrField();

    /**  */
    @DefaultMessage("Fonts")
    String fonts();

    /**  */
    @Messages.DefaultMessage("Type for more...")
    String forMore();

    /**  */
    @Messages.DefaultMessage("From")
    String from();

    /**  */
    @DefaultMessage("Fullscreen")
    String fullscreen();

    /**  */
    @DefaultMessage("General")
    String general();

    /**  */
    @DefaultMessage("Generate Link")
    String generateLink();

    /**  */
    @DefaultMessage("Good!")
    String good();

    /**  */
    @DefaultMessage("Great!")
    String great();

    /**  */
    @DefaultMessage("Greater or equals to")
    String greaterOrEquals();

    /**  */
    @DefaultMessage("Greater than")
    String greaterThan();

    /**  */
    @Messages.DefaultMessage("Heads up!")
    String headsUp();

    /**  */
    @DefaultMessage("Help")
    String help();

    /**  */
    @Messages.DefaultMessage("Hide menu")
    String hideMenu();

    /**  */
    @Messages.DefaultMessage("Value: ")
    String hoverableValueLabel();

    /**  */
    @DefaultMessage("Image should have a : {0} x {1} ratio")
    String imageRatioShouldBe(int width, float height);

    /**  */
    @DefaultMessage("Image should be {0} x {1} .")
    String imageShouldBe(int width, int height);

    /**  */
    @DefaultMessage("Image should be smaller than: {0} x {1}")
    String imageTooBig(int width, int height);

    /**  */
    @DefaultMessage("Image should be greater than: {0} x {1}")
    String imageTooSmall(int width, int height);

    /**  */
    @Messages.DefaultMessage("Include deactivated instances.")
    String includeDeprecated();

    /**  */
    @DefaultMessage("Indent Left")
    String indentLeft();

    /**  */
    @DefaultMessage("Indent Right")
    String indentRight();

    /**  */
    @DefaultMessage("You need to initialize subforms on load to use them inline: {0}")
    String initSubformsOnLoad(String message);

    /**  */
    @DefaultMessage("Insert Image")
    String insertImage();

    /**  */
    @Messages.DefaultMessage("Type or drag a url...")
    String insertURL();

    /**  */
    @Messages.DefaultMessage("Invalid download arguments exception, expected path /fqn/uuid")
    String invalidDownloadArgumentException();

    /**  */
    @DefaultMessage("Italic")
    String italic();

    /**  */
    @Messages.DefaultMessage("Enter form name:")
    String jumpToForm();

    /**  */
    @DefaultMessage("Keyboard Shortcuts")
    String keyboardShortcuts();

    /**  */
    @Messages.DefaultMessage("Last")
    String last();

    /**  */
    @DefaultMessage("Less or equals to")
    String lessOrEquals();

    /**  */
    @DefaultMessage("Less than")
    String lessThan();

    /**  */
    @Messages.DefaultMessage("Loading...")
    String loading();

    /**  */
    @Messages.DefaultMessage("Loading inbox...")
    String loadingInbox();

    /**  */
    @Messages.DefaultMessage("Please, login again...")
    String loginTitle();

    /**  */
    @DefaultMessage("Logout")
    String logout();

    /**  */
    @DefaultMessage("Max")
    String max();
    @DefaultMessage("Focus the Menu to navigate with keys")
    String menuFocus();

    /**  */
    @DefaultMessage("Min")
    String min();

    /**  */
    @Messages.DefaultMessage("Next →")
    String next();

    /**  */
    @Messages.DefaultMessage("Next page (tables) or day (dates)")
    String nextPage();

    /**  */
    @Messages.DefaultMessage("Previous row (tables) or week (dates)")
    String nextRow();

    /**  */
    @Messages.DefaultMessage("Next year (dates)")
    String nextYear();

    /**  */
    @Messages.DefaultMessage("No favorites yet. Press <kbd>F2</kbd> to add one.")
    String noFavorites();

    /**  */
    @Messages.DefaultMessage("Here you will see the latest accessed forms.")
    String noRecent();

    /**  */
    @DefaultMessage("Not begins with")
    String notBeginsWith();

    /**  */
    @DefaultMessage("Not contains")
    String notContains();

    /**  */
    @DefaultMessage("Not ends with")
    String notEndsWith();

    /**  */
    @DefaultMessage("Not equals")
    String notEquals();

    /**  */
    @Messages.DefaultMessage("of")
    String of();

    /**  */
    @Messages.DefaultMessage("Ok")
    String ok();

    /**  */
    @Messages.DefaultMessage("Oops! ")
    String oops();

    /**  */
    @Messages.DefaultMessage("or")
    String or();

    /**  */
    @DefaultMessage("Ordered List")
    String orderedList();

    /**  */
    @DefaultMessage("Organizational Units")
    String organizationalUnits();

    /**  */
    @DefaultMessage("OU:")
    String ou();

    /**  */
    @DefaultMessage("The Page Was Not Found.")
    String pageNotFoundMessage();

    /**  */
    @Messages.DefaultMessage("← Previous")
    String previous();

    /**  */
    @Messages.DefaultMessage("Previous page (tables) or day (dates)")
    String previousPage();

    /**  */
    @Messages.DefaultMessage("Next row (tables) or week (dates)")
    String previousRow();

    /**  */
    @Messages.DefaultMessage("Previous year (dates)")
    String previousYear();

    /**  */
    @Messages.DefaultMessage("Processing...")
    String processing();

    /**  */
    @Messages.DefaultMessage("Recent")
    String recent();

    /**  */
    @DefaultMessage("Reload")
    String reload();

    /**  */
    @Messages.DefaultMessage("Remove")
    String remove();

    /**  */
    @DefaultMessage("Remove Formatting")
    String removeFormatting();

    /**  */
    @DefaultMessage("Report")
    String report();

    /**  */
    @DefaultMessage("Rows")
    String rows();

    /**  */
    @Messages.DefaultMessage("Save")
    String save();

    /**  */
    @Messages.DefaultMessage("<strong>Saved</strong>")
    String saved();

    /**  */
    @Messages.DefaultMessage("Saving...")
    String saving();

    /**  */
    @Messages.DefaultMessage("Search")
    String search();

    /**  */
    @Messages.DefaultMessage("Search form")
    String searchForm();

    /**  */
    @Messages.DefaultMessage("Search...")
    String searchPlaceHolder();

    /**  */
    @Messages.DefaultMessage("Send feedback")
    String sendFeedback();
    /**  */
    @DefaultMessage("Internal Server Error Occurred.")
    String serverErrorMessage();

    /**  */
    @Messages.DefaultMessage("Show menu")
    String showMenu();

    /**  */
    @DefaultMessage("So-So!")
    String soSo();

    /**  */
    @DefaultMessage("Stroke")
    String stroke();

    /**  */
    @Messages.DefaultMessage("Suggestion")
    String suggestion();

    /**  */
    @DefaultMessage("Summary")
    String summary();

    /**  */
    @DefaultMessage("Switch User")
    String switchUser();

    /**  */
    @Messages.DefaultMessage("To")
    String to();

    /**  */
    @DefaultMessage("Today")
    String today();

    /**  */
    @DefaultMessage("Total")
    String total();

    /**  */
    @DefaultMessage("True")
    String trueLabel();

    /**  */
    @DefaultMessage("Type")
    String type();

    /**  */
    @DefaultMessage("Type an email...")
    String typeEmail();

    /**  */
    @Messages.DefaultMessage("Activate")
    String undeprecate();

    /**  */
    @DefaultMessage("Underline")
    String underline();

    /**  */
    @DefaultMessage("Unexpected error. {0}")
    String unexpectedError(String message);

    /**  */
    @DefaultMessage("Unordered List")
    String unorderedList();

    /**  */
    @Messages.DefaultMessage("Update")
    String update();

    /**  */
    @DefaultMessage("User profile")
    String userProfile();

    /**  */
    @DefaultMessage("Validate")
    String validate();

    /**  */
    @DefaultMessage("Weak!")
    String weak();

    /**  */
    @DefaultMessage("Welcome")
    String welcome();

    /**  */
    @DefaultMessage("Wrong email syntax.")
    String wrongMailSyntax();

    /**  */
    @DefaultMessage("Yesterday")
    String yesterday();
}  // end interface FormViewMessages
