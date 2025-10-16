package ca.ucalcary.cpsc219.cpsc219projectfinal;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

public class BookTrackerController implements Initializable{

    private final int NUMBER_OF_COLUMNS = 3;
    private List<String> finishedNames = new ArrayList<>();
    private List<String> toBeReadNames = new ArrayList<>();
    private List<String> inProgressNames = new ArrayList<>();
    private List<String> allNames = new ArrayList<>();
    private List<String> titlesAndAuthors = new ArrayList<>();
    private HashSet<Integer> goalsList = new HashSet<>();
    private boolean added = false;

    private Library library = new Library();
    private AddBookPopup popup = new AddBookPopup();
    private final String buttonStyle = "-fx-background-color: #3A7CA5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: transparent;" +
            "-fx-padding: 8 16;";

// Non-gui methods
    /**
     * Loads the data from the preset file SavedData.txt when the appropriate command line arguments are entered
     * @param args - String array of command line arguments
     */
    public void commandLineData(String[] args) {
        // start ExitWrapper
        ExitWrapper exitWrapper = new ExitWrapper();

        // Set exit handler to ensure proper exit (after editing wrapper)
        exitWrapper.setExitHandler(status -> {
            if (status != 0) {
                System.exit(status);
            }
        });

        try {
            // Checks if the number of arguments is 1, to ensure it doesn't crash
            if (args.length == 1) {
                // Sets the default file
                Path dataPath = new File("data").toPath();
                Path filePath = dataPath.resolve("SavedData.txt");
                File fileLoad = filePath.toFile();
                // Makes a new CommandLineLoader
                CommandLineLoader commandLineLoader = new CommandLineLoader(args, exitWrapper, fileLoad);
                commandLineLoader.setController(this);
                // Checks if the load was successful
                boolean wasLoadSuccessful = commandLineLoader.loadFile();
                // Prints out message based on whether the data loaded successfully or not
                if (wasLoadSuccessful) {
                    successLabel.setText("Loaded data successfully");
                } else {
                    successLabel.setText("Error loading data");
                }
            // If the incorrect number of command line arguments is entered, nothing happens and no message is printed
            } else {
                successLabel.setText("");
            }
        // Catches any error related to loading the data
        } catch (Exception e) {
            successLabel.setText("Error loading data");
        }
    }
//  @FXML
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField searchField;
    @FXML
    private ContextMenu suggestionsMenu = new ContextMenu();
    @FXML
    private VBox leftNode;
    @FXML
    private HBox topNode;
    @FXML
    private AnchorPane centerNode;
    @FXML
    private Label successLabel;
    @FXML
    private Button toBeReadButton;
    @FXML
    private Button inProgressButton;
    @FXML
    private Button finishedButton;
    @FXML
    private Button allBooksButton;
    @FXML
    private Button recommendationBookButton;
    @FXML
    private Label numberOfFinishedBooksLabel;
    @FXML
    private TextArea centerTextArea = new TextArea("I'm in the center");
    @FXML
    private GridPane gridPane = new GridPane();
    @FXML
    private ScrollPane scrollPane = new ScrollPane(gridPane);
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField seriesField;
    @FXML
    private TextField milestoneField;
    @FXML
    private TextField milestoneToSeeField;
    @FXML
    private TextField yearToSeeField;
    @FXML
    private ComboBox<String> readingStatusBox;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button purchaseRecommendationButton;
    @FXML
    private Button bookRecommendationButton;


    // On action
    /**
     * Creates a popup when about is clicked, that displays info about the creation of this program
     * @param event - ActionEvent, optional parameter, not used
     */
    @FXML
    public void onAboutClick(ActionEvent event) {
        // Make a new alert and set titles and such
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About");
        about.setHeaderText("CPSC 219 Final Project - Group 42");
        // All the info about the program
        about.setContentText("Created by: \nAmelia Custodio, Sarah Giesbrecht, Gray Wilson, Jessie Estrada, Ife Timothy\n\nContact info: \n\tamelia.custodio@ucalcary.ca\n\tsarah.giesbrecht1@ucalcary.ca\n\tgrace.wilson1@ucalgary.ca\n\tjessie.estrada@ucalcary.ca\n\tifeoluwa.timothy@ucalcary.ca\n\nTutorial: T10\n\nCreated Winter 2025");
        about.showAndWait();
    }
    /**
     * Creates a FileChooser so that the user can pick the file they would like to save to
     * @param event - ActionEvent, optional parameter, not used
     */
    @FXML
    public void onSaveClick(ActionEvent event) {
        // Create FileChooser
        final FileChooser fileChooser = new FileChooser();
        // Set the ending of the files to be .txt
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().getFirst());
        // Set the initial file when the FileChooser is loaded
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setInitialFileName("SavedData.txt");

        File fileSave = fileChooser.showSaveDialog(new Stage());
        if (fileSave != null) {
            // Creates a new FileSaver with the file that the user chose
            FileSaver fileSaver = new FileSaver(fileSave);
            fileSaver.setController(this);
            fileSaver.save();
            // Checks if the data was saved successfully and shows corresponding message
            boolean wasSaveSuccessful = fileSaver.save();
            if (wasSaveSuccessful) {
                successLabel.setText("Saved data successfully");
            } else {
                successLabel.setText("Error saving data");
            }
        }
    }
    /**
     * Creates a FileChooser so that the user can pick the file they would like to load to
     * @param event - ActionEvent, optional parameter, not used
     */
    @FXML
    public void onLoadClick(ActionEvent event) {
        // Creates a new FileChooser
        final FileChooser fileChooser = new FileChooser();
        // Sets the default folder the file is in
        fileChooser.setInitialDirectory(new File("data"));
        File fileLoad = fileChooser.showOpenDialog(new Stage());
        if (fileLoad != null) {
            // Creates a new FileLoader with the file the user chose to open
            MenuFileLoader fileLoader = new MenuFileLoader(fileLoad);
            fileLoader.setController(this);
            // Checks if the file loaded successfully and shows the corresponding message
            boolean wasLoadSuccessful = fileLoader.loadFile();
            if (wasLoadSuccessful) {
                successLabel.setText("Loaded data successfully");
            } else {
                successLabel.setText("Error loading data");
            }
        }
    }

    /**
     * {@code topAuthors()} handles sorting of authors
     * Displays error alert if no books have been added
     * Displays information alert with top five authors + number of books read
     */

    @FXML
    public void onTopAuthorsButton(){
        //Gets formatted list of most read authors
        String formattedAuthors = library.topAuthors();
        //Shows error if no books have been read
        if (formattedAuthors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You have not read any books.");
            alert.showAndWait();
        //Shows top five authors + number of books read by each
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Top Authors");
            alert.setHeaderText("Your Top Five Authors Are:");
            alert.setContentText(formattedAuthors);
            alert.showAndWait();
            return;
        }
    }
    /**
     * Changes the display to show all the books in the library when the user selects the "all" button
     */
    @FXML
    public void onAllBooksButtonClick() {
        resetDisplay();
        setBookListView(ListType.ALL);
        //Load the names of each of the books into the label in each cell
        loadList(ListType.ALL);
        //Dynamically change the size of the window depending on the size of the scene
        allBooksButton.setStyle(buttonStyle + "-fx-background-color: #708999");
    }
    /**
     * Changes the display to show all the books in the in progress list when the user selects the "in progress" button
     */
    @FXML
    public void onInProgressButtonClick() {
        resetDisplay();
        setBookListView(ListType.IN_PROGRESS);
        //Load the names of each of the books into the label in each cell
        loadList(ListType.IN_PROGRESS);
        //Dynamically change the size of the window depending on the size of the scene
        inProgressButton.setStyle(buttonStyle+"-fx-background-color: #708999" );
    }
    /**
     * Changes the display to show all the books in the TBR list when the user selects the "to be read" button
     */
    @FXML
    public void onToBeReadButtonClick() {
        resetDisplay();
        setBookListView(ListType.TO_BE_READ);
        //Load the names of each of the books into the label in each cell
        loadList(ListType.TO_BE_READ);
        //Dynamically change the size of the window depending on the size of the scene
        toBeReadButton.setStyle(buttonStyle+"-fx-background-color: #708999" );
    }
    /**
     * Changes the display to show all the books the user has finished when the user selects the "finished" button
     */
    @FXML
    public void onFinishedButtonClick() {
        resetDisplay();
        setBookListView(ListType.FINISHED);
        //Load the names of each of the books into the label in each cell
        loadList(ListType.FINISHED);
        //Dynamically change the size of the window depending on the size of the scene

        // Sets a label to display the number of books read so far
        numberOfFinishedBooksLabel.setText("Number of finished books: " + finishedNames.size());
        finishedButton.setStyle(buttonStyle+"-fx-background-color: #708999" );
    }
    /**
     * Handles the popup for adding a users book
     *
     */
    @FXML
    public void onEditAddBookButton() {
        popup.showPopup(this);
    }
    /**
     * Handles the popup for moving a users book
     *
     */
    @FXML
    public void onEditMoveBookButton(){
        popup.showMovePopup(this);
    }

    /**
     * Updates milestone list with integer entered by user
     * Catches non-valid input
     */
    @FXML
    public void updateMilestoneList(){
        try {
            //Adds user input to goalslist
            int goal = Integer.parseInt(milestoneField.getText());
            goalsList.add(goal);
            //Catches invalid input
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Adds a goal to the milestone list
     * @param goal - Int, the goal we want to add
     */
    public void addToMilestoneList(int goal){
        goalsList.add(goal);
    }

    /**
     * Shows set milestone popup that allows user to enter a new milestone
     */
    @FXML
    public void onSetMilestoneButtonClick(){
        popup.showSetMileStonePopup(this);
    }

    /**
     * Shows current milestones as an alert
     * Shows error alert if user hasn't entered any milestones
     */

    @FXML
    public void onViewMilestoneButtonClick(){
        //Converts list of integers to string
        String milestone = goalsList.toString();
        //Shows error is no milestones have been added
        if (goalsList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You do not have any milestones to view.");
            alert.showAndWait();
        //Alert shows list of all current milestones
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("View Milestones");
            alert.setHeaderText("Current Milestones:");
            alert.setContentText(milestone);
            alert.showAndWait();
        }

    }

    /**
     * Shows popup for milestone progress
     */
    @FXML
    public void onMilestoneProgressButtonClick(){
        popup.showMilestoneProgressPopup(this);
    }

    /**
     * Handles logic for when 'see progress' is clicked
     * Retrieves milestone from user
     * Calculates progress as a percentage
     * Shows progress bar with milestone progress
     * Shows error alert if milestone is not found
     */
    @FXML
    public void onSeeProgressButtonClick() {
        try {
            //Converts text inputted to integer
            int milestoneWanted = Integer.parseInt(milestoneToSeeField.getText());
            //Checks if milestone entered exists in the milestone list
            if (!goalsList.contains(milestoneWanted)) {
                //Shows error alert if milestone not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This milestone does not exist.");
                alert.showAndWait();
            } else {
                //Calculates milestone progress as a double

                double milestoneProgress = ((double) library.getTotalFinishedBooks() / (double) milestoneWanted);
                //Sets up dialog to show progress bar
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Milestone Progress");
                dialog.setHeaderText("Your reading goal of " + milestoneWanted + ":");
                //New progress bar created using calculated progress
                ProgressBar progressBar = new ProgressBar(milestoneProgress);
                //Layout
                progressBar.setPrefWidth(250);
                Label progressLabel = new Label(String.format("You have completed %.1f%% of this milestone.", milestoneProgress * 100));

                VBox content = new VBox(10, progressLabel, progressBar);
                content.setPadding(new Insets(10));

                dialog.getDialogPane().setContent(content);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            }

        } catch (Exception e) {
            //Shows error alert if non-integer input found
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid input");
            alert.showAndWait();
        }
    }
    /**
     * Handles adding a book from the user's library.
     *If the title field is empty or the book entry isn't valid then a error alert is shown.
     */
    @FXML
    public void submitAddBook(){
        String title = titleField.getText();

        // searchField.setPromptText("Search or type new book author...");
        String author = authorField.getText();

        // searchField.setPromptText("Search or type new book series...");
        String series = seriesField.getText();

        String statusStr = readingStatusBox.getValue();


        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (title.isEmpty() || author.isEmpty() || statusStr == null) {
            alert.setTitle("Error");
            alert.setContentText("Missing Fields ,Please fill in title, author, and select a status.");
            alert.showAndWait();
            return;
        }

        ReadingStatus status;
        switch (statusStr) {
            case "To Be Read" -> status = ReadingStatus.TO_BE_READ;
            case "In Progress" -> status = ReadingStatus.IN_PROGRESS;
            case "Finished" -> status = ReadingStatus.FINISHED;
            default -> status = ReadingStatus.NONE;
        }
        Book book = new Book(title, series, author, status);

        String result;
        switch (status) {
            case TO_BE_READ:
            result = library.addBookToTbr(book);
            if (result.contains("added")) added = true;
            break;

            case FINISHED:
            result = library.addBookToFinished(book);
            if (result.contains("added")) added = true;
            break;

            case IN_PROGRESS: {
                if (!library.getInProgress().contains(book)) {
                    library.getInProgress().add(book);
                    result = "Book added to In Progress.";
                    added = true;
                    break;
                } else {
                    result = "This book is already in progress.";
                    break;
                }
            }
            default:
                result = "Invalid status.";
        }

        alert.setContentText("Add Book" + result);

        if (added) {
            // Added all recent info so it is accounted for if we need to search right after
            List<String> updatedTitlesAndAuthors = new ArrayList<>();
            for (Book b : library.getAllBooks()) {
                if (!updatedTitlesAndAuthors.contains(b.getTitle().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getTitle().toUpperCase());
                }
                if (b.getAuthor() != null && !updatedTitlesAndAuthors.contains(b.getAuthor().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getAuthor().toUpperCase());
                }
            }

            // Most recent info should show in search suggestion
            setupSuggestionsMenuListener(updatedTitlesAndAuthors);

            // Similar to the initialize
            switch (status) {
                case TO_BE_READ:
                    onToBeReadButtonClick();
                case FINISHED:
                    onFinishedButtonClick();
                case IN_PROGRESS:
                    onInProgressButtonClick();
            }
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            titleField.clear();
            authorField.clear();
            seriesField.clear();
            readingStatusBox.getSelectionModel().clearSelection();
            mainBorderPane.setRight(null);

            if (added) {
                switch (status) {
                    case TO_BE_READ -> onToBeReadButtonClick();
                    case FINISHED -> onFinishedButtonClick();
                    case IN_PROGRESS -> onInProgressButtonClick();
                }
            }
            added = false;
        });

        delay.play();
    }
    /**
     * Handles the popup for removing a users book
     *
     */
    @FXML
    public void onEditRemoveAddedBookButton() {
        popup.showPopupForDeleting(this);
    }
    /**
     *  Handles removing the users book
     *  It deletes the book from the desired users list depending on the selected status
     *  If the title field is empty or the book doesn't exist, an error alert is shown
     */
    @FXML
    public void removeAddedBook() {
        String title = titleField.getText();
        String statusStr = readingStatusBox.getValue();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (title.isEmpty()) {
            alert.setTitle("Error");
            alert.setContentText("Missing Fields ,Please fill in title, author, and select a status.");
            alert.showAndWait();
            return;
        }

        if (!library.bookTitleExists(title)) {
            alert.setTitle("Error");
            alert.setContentText("Book title does not exist.");
            alert.showAndWait();
            return;
        } else {
            ReadingStatus status;
            switch (statusStr) {
                case "To Be Read" -> status = ReadingStatus.TO_BE_READ;
                case "In Progress" -> status = ReadingStatus.IN_PROGRESS;
                case "Finished" -> status = ReadingStatus.FINISHED;
                default -> status = ReadingStatus.NONE;
            }

            library.removeBook(title, status);

            resetDisplay();
            switch (status) {
                case TO_BE_READ -> onToBeReadButtonClick();
                case IN_PROGRESS -> onInProgressButtonClick();
                case FINISHED -> onFinishedButtonClick();
            }
            // Added all recent info so it is accounted for if we need to search right after
            List<String> updatedTitlesAndAuthors = new ArrayList<>();
            for (Book b : library.getAllBooks()) {
                if (!updatedTitlesAndAuthors.contains(b.getTitle().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getTitle().toUpperCase());
                }
                if (b.getAuthor() != null && !updatedTitlesAndAuthors.contains(b.getAuthor().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getAuthor().toUpperCase());
                }
            }

            // Most recent info should show in search suggestion
            setupSuggestionsMenuListener(updatedTitlesAndAuthors);
        }

    }
    /**
     * Handles the popup for recommendation
     */
    @FXML
    public void onRecommendationBookButton() {
        popup.showRecommendationPopup(this);
    }
    /**
     * Handles the recommendation for a book that the user should read
     * It gets a random book from the users to be read list if to be read list isn't empty
     * If the list is empty, it shows an alert indicating that there are no books to recommend.
     */
    @FXML
    public void onRecommendationBookButtons() {
        List<Book> tobereadcollection = library.getToBeRead();
        Collections.shuffle(tobereadcollection);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (tobereadcollection.isEmpty()) {
            alert.setTitle("Error");
            alert.setContentText("You have no books to recommend.");
            alert.showAndWait();
        }else {
            Book booktitlebook = tobereadcollection.get(0);
            String titletoshuffle = booktitlebook.getTitle();
            showBookInfoPopup(titletoshuffle);
        }
    }
    /**
     * Handles the purchase recommendation button click.
     * If there are 2 or fewer books, it suggests purchasing more.
     *  Otherwise, it advises against buying more books.
     */
    @FXML
    public void onPurchaseRecommendationBookButton(){

        List<Book> tobereadcollection = library.getToBeRead();

           if (tobereadcollection.size() <= 2) {
               Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
               infoAlert.setTitle("Purchase");
               infoAlert.setContentText("You should consider purchasing more books. You currently have: " + tobereadcollection.size() + " books in your TBR.");
               infoAlert.showAndWait();
           }else {
               Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
               infoAlert.setTitle("Purchase");
               infoAlert.setContentText("You shouldn't consider purchasing more books. You currently have: " + tobereadcollection.size() + " books in your TBR.");
               infoAlert.showAndWait();
           }
    }

    /**
     * Handles logic for moving a book between statuses (TBR, finished, in progress)
     * Retrieves title and status from user
     * Ensures book exists and is not already in the desired list
     * Shows error message f book is not found/is already in desired list
     */

    @FXML
    public void moveBook(){
        //Gets title and status from user
        String title = titleField.getText();
        String statusStr = readingStatusBox.getValue();
        ReadingStatus status;
        String author = "";
        String series = "";

        if (title.isEmpty() || statusStr == null) {
            //Shows error alert if either input is empty/null
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please enter the title and new reading status." );
            alert.showAndWait();
            return;
        }
        //Changes status of book based on user inputted status
        switch (statusStr) {
            case "To Be Read" -> status = ReadingStatus.TO_BE_READ;
            case "In Progress" -> status = ReadingStatus.IN_PROGRESS;
            case "Finished" -> status = ReadingStatus.FINISHED;
            default -> status = ReadingStatus.NONE;
        }
        //Checks if book exists
        boolean found = false;
        for (Book b : library.getAllBooks()) {
            if (!found) {
                if (b.getTitle().equalsIgnoreCase(title)) {
                    author = b.getAuthor();
                    series = b.getSeries();
                    found = true;
                }
            }
        }
        if (found) {
            Book bookToMove = new Book(title, series, author, status);
            //Error alerts if book is already in desired list
            if (status == ReadingStatus.TO_BE_READ && toBeReadNames.contains(bookToMove.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Book is already in TBR list.");
                alert.showAndWait();
                return;

            } else if (status == ReadingStatus.IN_PROGRESS && inProgressNames.contains(bookToMove.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Book is already in progress.");
                alert.showAndWait();
                return;
            } else if (status == ReadingStatus.FINISHED && finishedNames.contains(bookToMove.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Book is already in finished.");
                alert.showAndWait();
                return;
            } else {
                //Changes status based on user input and removes from other lists
                if (status == ReadingStatus.TO_BE_READ) {
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.IN_PROGRESS);
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.FINISHED);
                    bookToMove.setStatus(ReadingStatus.TO_BE_READ);
                    library.addBookToTbr(bookToMove);
                } else if (status == ReadingStatus.IN_PROGRESS) {
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.TO_BE_READ);
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.FINISHED);
                    bookToMove.setStatus(ReadingStatus.IN_PROGRESS);
                    library.addBookToInProgress(bookToMove);
                } else if (status == ReadingStatus.FINISHED) {
                    // Update date to allow for search or year
                    bookToMove.setDate(new Date());
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.TO_BE_READ);
                    library.removeBook(bookToMove.getTitle(), ReadingStatus.IN_PROGRESS);
                    bookToMove.setStatus(ReadingStatus.FINISHED);
                    library.addBookToFinished(bookToMove);

                }
            }
            // Added all recent info so it is accounted for if we need to search right after
            List<String> updatedTitlesAndAuthors = new ArrayList<>();
            for (Book b : library.getAllBooks()) {
                if (!updatedTitlesAndAuthors.contains(b.getTitle().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getTitle().toUpperCase());
                }
                if (b.getAuthor() != null && !updatedTitlesAndAuthors.contains(b.getAuthor().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getAuthor().toUpperCase());
                }
            }
            setupSuggestionsMenuListener(updatedTitlesAndAuthors);
        } else {
            //Error alert if book is not found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Book does not exist.");
            alert.showAndWait();
        }
    }
    /**
     * Sets up a listener to monitor changes in the search box
     * Changes the ContextMenu suggestionsMenu to match the user input
     * (if the string a user has inputted is part of one of the book titles or author names then the book title/author name will be displayed in the suggestionMenu)
     * @param data is a list of all the author names and book titles the user has entered
     * Whenever the user makes changes (ie adds a book, moves a book or deletes a book, the contents of this list is updated and the suggestionsMenuListener is called again
     */
    @FXML
    public void setupSuggestionsMenuListener(List<String> data) {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                suggestionsMenu.hide();
            } else {
                var filtered = data.stream()
                        .filter(item -> item.toUpperCase().contains(newVal.toUpperCase()))
                        .toList();

                if (!filtered.isEmpty()) {
                    var items = filtered.stream().map(item -> {
                        var menuItem = new MenuItem(item);

                        menuItem.setOnAction(e -> {
                            searchField.setText(item);
                            displayBookDetails(item);
                            suggestionsMenu.hide();
                        });

                        return menuItem;
                    }).toList();

                    suggestionsMenu.getItems().setAll(items);
                    suggestionsMenu.show(searchField, Side.BOTTOM, 0, 0);
                } else {
                    suggestionsMenu.hide();
                }
            }
        });

        searchField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                // When the enter key is pressed
                case ENTER -> {
                    // (consider we still have to hover over the option or press tab/enter to choose without clicking the mouse
                    String input = searchField.getText().trim();
                    if (!input.isEmpty()) {
                        displayBookDetails(input);
                        suggestionsMenu.hide();
                    }
                }
            }
        });
    }
    /**
     * Checks if the user inputted an author name or book title then displays the information accordingly
     * If the user searched for a book title, the details of that book will be displayed
     * If the user searched for an author name, all the books by that author will be displayed
     * When displaying the books by a certain author, each of the books will be clickable and you can click on them to get specific book details
     * @param input is the string the user inputted into the search box (this could be a book title or author name)
     */
    @FXML
    public void displayBookDetails(String input) {
        resetDisplay();
        List<String> bookDetails = library.getBookDetails(input);
        if (bookDetails != null ) {
            if (bookDetails.get(5).equalsIgnoreCase("author")) {
                List<Book> authorsBooksRaw = library.getAuthorsBooks(input);
                List<Book> authorsBooks = new ArrayList<>();

                // Since we use a hashset, it automatically doesn't allow for doubles
                HashSet<String> seenTitles = new HashSet<>();
                for (Book book : authorsBooksRaw) {
                    if (seenTitles.add(book.getTitle())) {
                        authorsBooks.add(book);
                    }
                }

                int listSize = authorsBooks.size();

                int numberOfRows = (int) Math.ceil((double) listSize / NUMBER_OF_COLUMNS);

                //Load each of the cells with an empty label
                for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                    for (int j = 0; j < numberOfRows; j++) {
                        gridPane.add(new Label(""), i, j);
                    }
                }
                //Load the names of each of the books into the label in each cell
                int index = 0;
                for (int row = 0; row < numberOfRows; row++) {
                    for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
                        if (index < listSize) {
                            Book currentBook = authorsBooks.get(index);
                            // Calls the make book clickable method instead
                           //TextArea area =
                            VBox card = makeBookClickable(currentBook);
                            gridPane.add(card, col, row);
                            index++;
                        }
                    }
                }
                if (centerNode.getChildren().isEmpty()) {
                 //   scrollPane = new ScrollPane(gridPane);
                    centerNode.getChildren().add(scrollPane);
                }
            } else if (bookDetails.get(5).equalsIgnoreCase("title")) {
                setBookInformationView();
                String title = bookDetails.get(0);
                String author = bookDetails.get(1);
                String series = bookDetails.get(2);
                String date = bookDetails.get(3);
                String readingStatus = bookDetails.get(4).strip().toLowerCase();

                // Added breaks the values assigned to date text in the switch statement are used
                String dateText;
                switch (readingStatus) {
                    case "in progress" :
                        dateText = "\n- You started reading this book on " + date;
                        break;
                    case "to be read":
                        dateText = "\n- You added this book to your TBR on " + date;
                        break;
                    case "finished":
                        dateText = "\n- You finished this book on " + date;
                        break;
                    default:
                        dateText = "\n- You added this book to your library on " + date;
                }
                String displayText = "This book is in your " + readingStatus + " list\n" +
                        "- Title: " + title +
                        "\n- Author: " + author +
                        "\n- Series: " + series +
                        dateText;
                centerTextArea.setText(displayText);
                centerTextArea.setEditable(false);
                if (centerNode.getChildren().isEmpty()) {
                    centerNode.getChildren().add(centerTextArea);
                }
            }
            // Cleared the search box that way we don't have to delete each time manually
            searchField.clear();
        } else {
                //TODO: Possibly make this an alert instead?
                centerTextArea.setText("This book is not in your library");
                centerTextArea.setEditable(false);
            if (centerNode.getChildren().isEmpty()) {
                centerNode.getChildren().add(centerTextArea);
            }
        }
    }
    /**
     * Initializes the display with a grid pane containing empty labels
     * @param listType refers to the type of list the user will be viewing (eg. all, in progress, TBR or finished)
     * Accesses the values in the list corresponding to the parameter listType so that the grid pane can be set to the same size as the number of books in that list
     */
    @FXML
    public void setBookListView(ListType listType) {
        int size;
        int numberOfRows;

        // Add some content based on the contents of the list (which is chosen based on the button we have selected)
        switch (listType) {
            case ALL:
                //If we have selected the all button and want to see the entire library
                // updated getting the size of all the books in the library
                size = library.getNumBooksInTBR() + library.getTotalFinishedBooks() + library.getBooksInProgress();
                numberOfRows = size / NUMBER_OF_COLUMNS;
                //Load each of the cells with an empty label
                for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                    for (int j = 0; j < numberOfRows; j++) {
                        gridPane.add(new Label(""), i, j);
                    }
                }
                if (centerNode.getChildren().isEmpty()) {
                  //  scrollPane = new ScrollPane(gridPane);
                    centerNode.getChildren().add(scrollPane);
                }
                break;
            case IN_PROGRESS:
                //If we have selected the in progress button and want to see all the books that are in progress
                size = library.getBooksInProgress();
                numberOfRows = size / NUMBER_OF_COLUMNS;
                //Load each of the cells with an empty label
                for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                    for (int j = 0; j < numberOfRows; j++) {
                        gridPane.add(new Label(""), i, j);
                    }
                }
                if (centerNode.getChildren().isEmpty()) {
                   // scrollPane = new ScrollPane(gridPane);
                    centerNode.getChildren().add(scrollPane);
                }
                break;
            case TO_BE_READ:
                //If we have selected the to be read button and want to see all the books in the TBR list
                size = library.getNumBooksInTBR();
                numberOfRows = size / NUMBER_OF_COLUMNS;
                //Load each of the cells with an empty label
                for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                    for (int j = 0; j < numberOfRows; j++) {
                        gridPane.add(new Label(""), i, j);
                    }
                }
                if (centerNode.getChildren().isEmpty()) {
                   // scrollPane = new ScrollPane(gridPane);
                    centerNode.getChildren().add(scrollPane);
                }
                break;
            case FINISHED:
                //If we have selected the finished button and want to see all the books we have finished
                size = library.getTotalFinishedBooks();
                numberOfRows = size / NUMBER_OF_COLUMNS;
                //Load each of the cells with an empty label
                for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                    for (int j = 0; j < numberOfRows; j++) {
                        gridPane.add(new Label(""), i, j);
                    }
                }
                if (centerNode.getChildren().isEmpty()) {
                 //  scrollPane = new ScrollPane(gridPane);
                    centerNode.getChildren().add(scrollPane);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid button choice. Please choose a different button");
        }
    }
    /**
     * Clears the existing center display and sets it to a text box
     * The text box fills the entire center space and is not editable as it is meant to show the details of specific books
     */
    @FXML
    public void setBookInformationView() {
        gridPane.getChildren().clear();
        // Make it grow to fill the center space
        centerTextArea.setMaxWidth(Double.MAX_VALUE);
        centerTextArea.setMaxHeight(Double.MAX_VALUE);

        // Optional: make it prettier with padding or styling
        centerTextArea.setStyle("-fx-font-size: 14px;");
        centerTextArea.setEditable(false);

        // Set it in the center of the BorderPane
        if (centerNode.getChildren().isEmpty()) {
            centerNode.getChildren().add(centerTextArea);
        };

        // Allow it to resize inside the BorderPane
        BorderPane.setAlignment(centerTextArea, Pos.CENTER);
        BorderPane.setMargin(centerTextArea, new Insets(10)); // optional margin

    }
    /**
     * Similar to the setBookListView method except now we are loading specific titles into each of the labels
     * @param listType refers to the type of list we would like to load into the grid pane (eg. all, TBR, in progress or finished)
     * Each of the books displayed are clickable and can be clicked to view the details of that specific book
     */
    @FXML
    public void loadList(ListType listType) {
        //TODO: check if we need to create a deep copy of each list when we copy it into bookList
        int listSize = 0;
        List<String> bookList;

        switch (listType) {
            case ALL:
                allNames.clear();
                for (Book book : library.getAllBooks()) {
                    if (!allNames.contains(book.getTitle())) {
                        allNames.add(book.getTitle());
                    }
                }
                listSize = allNames.size();
                bookList = allNames;
                break;
            case IN_PROGRESS:
                inProgressNames.clear();
                for (Book book : library.getInProgress()) {
                    if (!inProgressNames.contains(book.getTitle())) {
                        inProgressNames.add(book.getTitle());
                    }
                }
                listSize = inProgressNames.size();
                bookList = inProgressNames;
                break;
            case TO_BE_READ:
                toBeReadNames.clear();
                for (Book book : library.getToBeRead()) {
                    if (!toBeReadNames.contains(book.getTitle())) {
                        toBeReadNames.add(book.getTitle());
                    }
                }
                listSize = toBeReadNames.size();
                bookList = toBeReadNames;
                break;
            case FINISHED:
                finishedNames.clear();
                for (Book book : library.getFinished()) {
                    if (!finishedNames.contains(book.getTitle())) {
                        finishedNames.add(book.getTitle());
                    }
                }
                listSize = finishedNames.size();
                bookList = finishedNames;
                break;
            default:
                throw new IllegalArgumentException("Invalid button choice. Please choose a different button");
        }

        int numRows = (int) Math.ceil((double) listSize / NUMBER_OF_COLUMNS);

        //Load the names of each of the books into the label in each cell
        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
                if (index < listSize) {
                    Book matchingBook = library.getBookByTitle(bookList.get(index));
                    // Calls the make book clickable method instead
                   // TextArea area = makeBookClickable(matchingBook);
                    VBox card = makeBookClickable(matchingBook);
                    gridPane.add(card, col, row);
                    index++;
                }
            }
        }
        centerNode.getChildren().clear();
        centerNode.getChildren().add(scrollPane);
    }
    /**
     * Resets all the aspects of the display to their original state to avoid stacking different display elements
     */
    @FXML
    public void resetDisplay() {
        centerNode.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        if (titleField != null) {
            titleField.clear();
        }
        if (authorField != null) {
            authorField.clear();
        }
        if (searchField != null) {
            searchField.clear();
        }
        if (readingStatusBox != null) {
            readingStatusBox.getSelectionModel().clearSelection();
        }
        mainBorderPane.setRight(null);
        numberOfFinishedBooksLabel.setText("");

        toBeReadButton.setStyle(buttonStyle);
        finishedButton.setStyle(buttonStyle);
        inProgressButton.setStyle(buttonStyle);
        allBooksButton.setStyle(buttonStyle);

    }
    /**
     * Initializes the display and sets attributes for some of the important display variables
     * Sets styling for the buttons
     * Starts running the suggestions menu listener so if the user immediately chooses to search a book the search bar is functioning
     * Sets a fixed height for the top region of the border pane and the left region of the border pane
     * Creates an anchor pane in the center region of the border pane and anchors both the scroll pane and center text area to the corners of the anchor pane
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize (URL location, ResourceBundle resources) {

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));
        gridPane.setStyle("-fx-background-color: #FAFAFA;");


        toBeReadButton.setStyle(buttonStyle);
        finishedButton.setStyle(buttonStyle);
        inProgressButton.setStyle(buttonStyle);
        allBooksButton.setStyle(buttonStyle);

        recommendationBookButton.setStyle(buttonStyle + "-fx-background-color: #16425B;" + "-fx-background-radius: 0;" + "-fx-border-color: transparent white transparent  transparent;");

        for (Book book : library.getAllBooks()) {
            if (!titlesAndAuthors.contains(book.getTitle().toUpperCase())) {
                titlesAndAuthors.add(book.getTitle().toUpperCase());
            }
            if (!titlesAndAuthors.contains(book.getAuthor().toUpperCase())) {
                titlesAndAuthors.add(book.getAuthor().toUpperCase());
            }
        }
        setupSuggestionsMenuListener(titlesAndAuthors);

        topNode.setPrefHeight(50);   // Set a fixed height for the top region
        leftNode.setPrefWidth(200);  // Set a fixed width for the left region

        //Anchor the scroll pane to all four sides of the AnchorPane
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);

        // Anchor the center text area to all four sides of the AnchorPane
        AnchorPane.setTopAnchor(centerTextArea, 0.0);
        AnchorPane.setLeftAnchor(centerTextArea, 0.0);
        AnchorPane.setRightAnchor(centerTextArea, 0.0);
        AnchorPane.setBottomAnchor(centerTextArea, 0.0);


        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
    }
    @FXML
    public void showBookInfoPopup(String bookTitle) {
        // Calls method to get singular book info
        Book book = library.getBookByTitle(bookTitle);

        // Just in-case there's some error (although shouldn't happen since we are clicking on the actual column
        if (book == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No details found for this book.");
            alert.showAndWait();
            return;
        }

        // Displays info in a simple way compared to when searched for
        // convert the status to string to make output "prettier"
        String listInfo;
        switch (book.getStatus()) {
            case IN_PROGRESS:
                listInfo = "\nYou started reading this book on: ";
                break;
            case TO_BE_READ:
                listInfo = "\nYou added this book to your TBR on: ";
                break;
            case FINISHED:
                listInfo = "\nYou finished this book on: ";
                break;
            default:
                listInfo = "";
                break;
        }
        // Outputs
        String content = "Title: " + book.getTitle() +
                "\nAuthor: " + book.getAuthor() +
                "\nSeries: " + (book.getSeries().isEmpty() ? "N/A" : book.getSeries()) +
                "\nStatus: " + book.getStatus() +
                listInfo + book.getDate();

        // Shows info in a pop-up as an info alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Info");
        alert.setHeaderText(book.getTitle());
        alert.setContentText(content);
        alert.showAndWait();
    }
    /**
     * Handles the popup for searching all the books finished in a specific year
     */
    @FXML
    public void onAllBooksYearClick() {
        AddBookPopup popup = new AddBookPopup();
        popup.showBooksYearPopup(this);
    }
    /**
     * Sorts all the books in the library to determine which ones were finished in a specific year
     * Displays all the books finished in a specific year and makes the clickable so you can see the details of a specific book
     */
    @FXML
    public void showBooksYear() {
        resetDisplay();
        List<String> booksInYear = new ArrayList<>();

        try {
            int year = Integer.parseInt(yearToSeeField.getText());
            for (Book b : library.getAllBooks()) {
                Date bookDate = b.getDate();
                // Use calendar to make it less error prone
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(bookDate);
                int bookYear = calendar.get(Calendar.YEAR);

                // adds books
                if (bookYear == year && b.getStatus() == ReadingStatus.FINISHED) {
                    booksInYear.add(b.getTitle());
                }
            }
            // If no books were found show pop up
            if (booksInYear.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Books Found");
                alert.setContentText("You did not finish any books in the year " + year);
                alert.showAndWait();
                return;
            }

            int size = booksInYear.size();
            //Set the display to the list display
            int numberOfRows = (int) Math.ceil((double) size / NUMBER_OF_COLUMNS);
            gridPane.getChildren().clear();
            //Load each of the cells with an empty label
            for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                for (int j = 0; j < numberOfRows; j++) {
                    gridPane.add(new Label(""), i, j);
                }
            }

            //Load the names of each of the books into the label in each cell
            int index = 0;
            for (int row = 0; row < numberOfRows; row++) {
                for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
                    if (index < booksInYear.size()) {
                        String title = booksInYear.get(index);
                        Book matchingBook = library.getBookByTitle(title);
                        // Calls the make book clickable method instead
                        // TextArea area = makeBookClickable(matchingBook);
                        VBox card = makeBookClickable(matchingBook);
                        gridPane.add(card, col, row);
                        index++;
                    }
                }
            }
            if (centerNode.getChildren().isEmpty()) {
                centerNode.getChildren().add(scrollPane);
            }

            centerNode.getChildren().clear();
            centerNode.getChildren().add(scrollPane);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Handled the design of the user added book class
     * @param book is the book to display on the UI card
     * @return the card which is a styled Vbox.
     */
    private VBox makeBookClickable(Book book) {
        VBox card = new VBox(5); // spacing between title and author
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(140, 160);
        card.setMinWidth(140);
        card.setMaxWidth(140);

        card.setPrefHeight(160);
        card.setMinHeight(160);
        card.setMaxHeight(160);


        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        );

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-wrap-text: true; -fx-text-alignment: center;");
        titleLabel.setMaxWidth(110);
        titleLabel.setWrapText(true);

        Label authorLabel = new Label(book.getAuthor());
        authorLabel.setWrapText(true);
        authorLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 12px; -fx-wrap-text: true; -fx-text-alignment: center;");
        authorLabel.setMaxWidth(110);

        String labelText;
        if (book.getSeries().isEmpty()) {
            labelText = "";
        } else {
            labelText = "Series: " + book.getSeries();
        }
        Label seriesLabel = new Label(labelText);

        seriesLabel.setStyle("-fx-text-fill: #777; -fx-font-size: 11px;");
        seriesLabel.setMaxWidth(110);

        card.getChildren().addAll(titleLabel, authorLabel);
        if (!book.getSeries().isEmpty()) {
            card.getChildren().add(seriesLabel);
        }

        card.setOnMouseClicked(e -> showBookInfoPopup(book.getTitle()));


        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #f0f8ff;" +
                        "-fx-border-color: #3A7CA5;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;"
        ));
        return card;
    }

    // Getters and Setters

    /**
     * Returns the value in the text field, which is the title of the book entered
     * @return - TextField, the title of the book
     */
    public TextField getTitleField() {
        return titleField;
    }

    /**
     * Returns the value in the text field, which is the author of the book entered
     * @return - TextField, the author of the book
     */
    public TextField getAuthorField() {
        return authorField;
    }

    /**
     * Returns the value in the text field, which is the series of the book entered
     * @return - TextField, the series of the book
     */
    public TextField getSeriesField() {
        return seriesField;
    }

    /**
     * Returns the value in the text field, which is the milestone entered
     * @return - TextField, the milestone
     */
    public TextField getMilestoneField() {
        return milestoneField;
    }

    /**
     * Returns the value in the text field, which is the milestone entered that the user wants to see
     * @return - TextField, the milestone
     */
    public TextField getMilestoneToSeeField() {
        return milestoneToSeeField;
    }

    /**
     * Returns the value in the text field, which is the year that the user wants to see
     * @return - TextField, the year
     */
    public TextField getYearToSeeField() {
        return yearToSeeField;
    }

    /**
     * Returns the reading status of the selected book
     * @return - ComboBox<String>, the reading status of the book
     */
    public ComboBox<String> getReadingStatusBox() {
        return readingStatusBox;
    }

    /**
     * Returns the submit button
     * @return - Button, the submit button
     */
    public Button getSubmitButton() {
        return submitButton;
    }

    /**
     * Returns the cancel button
     * @return - Button, the cancel button
     */
    public Button getCancelButton() {
        return cancelButton;
    }

    /**
     * Returns the purchase recommendation button
     * @return - Button, the purchase recommendation button
     */
    public Button getPurchaseRecommendationButton() {
        return purchaseRecommendationButton;
    }

    /**
     * Returns the book recommendation button
     * @return - Button, the book recommendation button
     */
    public Button getBookRecommendationButton() {
        return bookRecommendationButton;
    }

    /**
     * Sets the title of the book
     * @param titleField - TextField, the title of the book
     */
    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    /**
     * Sets the author of the book
     * @param authorField - TextField, the author of the book
     */
    public void setAuthorField(TextField authorField) {
        this.authorField = authorField;
    }

    /**
     * Sets the series of the book
     * @param seriesField - TextField, the series of the book
     */
    public void setSeriesField(TextField seriesField) {
        this.seriesField = seriesField;
    }

    /**
     * Sets the submit button's name
     * @param submitButton - Button, the submit button
     */
    public void setSubmitButton(Button submitButton) {
        this.submitButton = submitButton;
    }

    /**
     * Sets the cancel button's name
     * @param cancelButton - Button, the cancel button
     */
    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    /**
     * Sets the reading status of the book
     * @param readingStatusBox - ComboBox<String>, the reading status
     */
    public void setReadingStatusBox(ComboBox<String> readingStatusBox) {
        this.readingStatusBox = readingStatusBox;
    }

    /**
     * Sets a new milestone
     * @param milestoneField - TextField, the milestone
     */
    public void setMilestoneField(TextField milestoneField) {
        this.milestoneField = milestoneField;
    }

    /**
     * Sets the milestone that the user wants to see
     * @param milestoneToSeeField - TextField, the milestone
     */
    public void setMilestoneToSeeField(TextField milestoneToSeeField) {
        this.milestoneToSeeField = milestoneToSeeField;
    }

    /**
     * Sets the year that the user wants to see
     * @param yearToSeeField - TextField, the year
     */
    public void setYearToSeeField(TextField yearToSeeField) {
        this.yearToSeeField = yearToSeeField;
    }

    /**
     * Sets the name of the purchase recommendation button
     * @param purchaseRecommendationButton - Button, the purchase recommendation button
     */
    public void setPurchaseRecommendationButton(Button purchaseRecommendationButton) {
        this.purchaseRecommendationButton = purchaseRecommendationButton;
    }

    /**
     * Sets the name of the book recommendation button
     * @param bookRecommendationButton - Button, the book recommendation button
     */
    public void setBookRecommendationButton(Button bookRecommendationButton) {
        this.bookRecommendationButton = bookRecommendationButton;
    }

    public Library getLibrary() {
        return library;
    }

    public HashSet<Integer> getGoalsList() {
        return goalsList;
    }

    public void saveThenExit() {
        // Create a temporary file saver with default location
        Path dataPath = new File("data").toPath();
        Path filePath = dataPath.resolve("SavedData.txt");
        File fileSave = filePath.toFile();

        FileSaver fileSaver = new FileSaver(fileSave);
        fileSaver.setController(this);
        boolean wasSaveSuccessful = fileSaver.save();

        if (wasSaveSuccessful) {
            System.out.println("Data saved successfully");
        } else {
            System.out.println("Error saving data");
        }

        // Exit after save attempt
        System.exit(0);
    }

    /**
     * Exits the application without saving
     * Called when user clicks "Exit" in the exit confirmation dialog
     */
    public void exitWithoutSaving() {
        System.exit(0);
    }

    @FXML
    public void onAPISearchBookButton() {
        popup.showAPISearchPopup(this);
    }

}