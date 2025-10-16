package ca.ucalcary.cpsc219.cpsc219projectfinal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class AddBookPopup {

    public void showPopup(BookTrackerController controller) {
        // Create a new Stage (popup)
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
        popupStage.setTitle("Add a New Book");

        // Create input fields
        controller.setTitleField(new TextField());
        controller.getTitleField().setPromptText("Title");

        controller.setAuthorField(new TextField());
        controller.getAuthorField().setPromptText("Author");

        controller.setSeriesField(new TextField());
        controller.getSeriesField().setPromptText("Series (optional)");

        controller.setReadingStatusBox(new ComboBox<>());
        controller.getReadingStatusBox().getItems().addAll("To Be Read", "In Progress", "Finished");
        controller.getReadingStatusBox().setPromptText("Reading Status");

        controller.setSubmitButton(new Button("Add"));
        controller.setCancelButton(new Button("Cancel"));

        controller.getSubmitButton().setOnAction(e -> {
            controller.submitAddBook();
            controller.getReadingStatusBox().getItems().clear();
            popupStage.close();
        });

        controller.getCancelButton().setOnAction(e -> {
            controller.getTitleField().clear();
            controller.getAuthorField().clear();
            controller.getSeriesField().clear();
            controller.getReadingStatusBox().getSelectionModel().clearSelection();
            controller.getReadingStatusBox().getItems().clear();
            popupStage.close();
        });

        // Layout
        VBox layout = new VBox(10, controller.getTitleField(), controller.getAuthorField(), controller.getSeriesField(), controller.getReadingStatusBox(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    /**
     * Displays a popup window that allows the user to remove a book from their library.
     *
     * The user enters the book title and selects its reading status. When submitted, the book is removed.
     * Cancelling clears the form and closes the popup.
     *
     */
    public void showPopupForDeleting(BookTrackerController controller) {
        // Create a new Stage (popup)
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
        popupStage.setTitle("Remove a Book");

        // Create input fields
        controller.setTitleField(new TextField());
        controller.getTitleField().setPromptText("Title");

          controller.setReadingStatusBox(new ComboBox<>());

          controller.getReadingStatusBox().getItems().addAll("To Be Read", "In Progress", "Finished");
          controller.getReadingStatusBox().setPromptText("Reading Status");

        controller.setSubmitButton(new Button("Remove"));
        controller.setCancelButton(new Button("Cancel"));

        controller.getSubmitButton().setOnAction(e -> {
            controller.removeAddedBook();
            controller.getReadingStatusBox().getItems().clear();
            popupStage.close();
        });

        controller.getCancelButton().setOnAction(e -> {
            controller.getTitleField().clear();
            controller.getReadingStatusBox().getSelectionModel().clearSelection();
            controller.getReadingStatusBox().getItems().clear();
            popupStage.close();
        });

        // Layout
        VBox layout = new VBox(10, controller.getTitleField(), controller.getReadingStatusBox(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20,20,20,20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    /**
     * Displays move pop-up for user to move a book between lists
     * Text field for title and drop down for reading status selection
     * Move button uses {@code moveBook()}
     * Cancel button clears all fields
     * @param controller {@link BookTrackerController} responsible for handling moving logic
     */

    public void showMovePopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
        popupStage.setTitle("Move A Book");

        // Create input fields
        controller.setTitleField(new TextField());
        controller.getTitleField().setPromptText("Title");

        controller.setAuthorField(new TextField());
        controller.getAuthorField().setPromptText("Author");

        controller.setReadingStatusBox(new ComboBox<>());
        controller.getReadingStatusBox().getItems().addAll("To Be Read", "In Progress", "Finished");
        controller.getReadingStatusBox().setPromptText("Reading Status");

        controller.setSubmitButton(new Button("Move"));
        controller.setCancelButton(new Button("Cancel"));

        controller.getSubmitButton().setOnAction(e -> {
            controller.moveBook();
            controller.getTitleField().clear();
            controller.getReadingStatusBox().getSelectionModel().clearSelection();
            popupStage.close();
        });

        controller.getCancelButton().setOnAction(e -> {
            controller.getTitleField().clear();
            controller.getAuthorField().clear();
            controller.getSeriesField().clear();
            controller.getReadingStatusBox().getSelectionModel().clearSelection();
            popupStage.close();
        });

        // Layout
        // Asks for title of book only now
        VBox layout = new VBox(10, controller.getTitleField(), controller.getReadingStatusBox(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    /**
     * Displays a popup window with book recommendation options.
     *
     * The popup includes two buttons:
     * Recommend a book: Picks a random book from the ToBeRead list.
     * Purchase recommendation: Advises whether the user should buy more books based on TBR size.
     */
    public void showRecommendationPopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
        popupStage.setTitle("Recommendation");

        controller.setBookRecommendationButton(new Button("Recommend a book"));
        controller.setPurchaseRecommendationButton(new Button("Purchase Recommendation"));

        controller.getBookRecommendationButton().setOnAction(e -> {
            controller.onRecommendationBookButtons();
           // popupStage.close();
        });

        controller.getPurchaseRecommendationButton().setOnAction(e -> {
            controller.onPurchaseRecommendationBookButton();
            popupStage.close();
        });

        // Layout
        VBox layout = new VBox(10, controller.getBookRecommendationButton(), controller.getPurchaseRecommendationButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    /**
     * Displays pop-up for setting new milestones
     * Includes text field for entering new milestone
     * Input processed by {@code updateMilestoneList()}
     * Cancel clears all fields
     * @param controller {link BookTrackerController} handles logic for milestone field and buttons
     */
    public void showSetMileStonePopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Set Milestone");
        //Create input fields
        controller.setMilestoneField(new TextField());
        controller.getMilestoneField().setPromptText("Add a new Milestone");
        controller.setSubmitButton(new Button("Add"));
        controller.setCancelButton(new Button("Cancel"));
        controller.getSubmitButton().setOnAction(e -> {
            controller.updateMilestoneList();
            popupStage.close();
        });
        controller.getCancelButton().setOnAction(e -> {
            controller.getMilestoneField().clear();
            popupStage.close();
        });
        // Layout
        VBox layout = new VBox(10, controller.getMilestoneField(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    /**
     * Displays pop-up to show milestone progress
     * Includes text field for entering which milestone user would like to see
     * Input processed by {@code onSeeProgressButtonClick()}
     * Cancel button clears all fields
     * @param controller {link BookTrackerController} handles input and progress calculations
     */
    public void showMilestoneProgressPopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("See Milestone Progress");
        //Create input fields
        controller.setMilestoneToSeeField(new TextField());
        controller.getMilestoneToSeeField().setPromptText("Which milestone?");
        controller.setSubmitButton(new Button("See progress"));
        controller.setCancelButton(new Button("Cancel"));
        controller.getSubmitButton().setOnAction(e -> {
            controller.onSeeProgressButtonClick();
            popupStage.close();
        });
        controller.getCancelButton().setOnAction(e -> {
            controller.getMilestoneToSeeField().clear();
            popupStage.close();
        });
        // Layout
        VBox layout = new VBox(10, controller.getMilestoneToSeeField(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    public void showBooksYearPopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("See all the books in your library from a specific year");
        controller.setYearToSeeField(new TextField());
        controller.getYearToSeeField().setPromptText("Enter the year you would like to see");
        controller.setSubmitButton(new Button("See books?"));
        controller.setCancelButton(new Button("Cancel"));
        controller.getSubmitButton().setOnAction(e -> {
            controller.showBooksYear();
            controller.getYearToSeeField().clear();
            popupStage.close();
        });
        controller.getCancelButton().setOnAction(e -> {
            controller.getYearToSeeField().clear();
            popupStage.close();
        });
        //Layout
        // make sure we call the yearToSee
        VBox layout = new VBox(10, controller.getYearToSeeField(), controller.getSubmitButton(), controller.getCancelButton());
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Wait for popup to close before continuing
    }

    public void showAPISearchPopup(BookTrackerController controller) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Search Google Books");

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Enter book title, author, or ISBN");
        searchField.setPrefWidth(400);

        // Search button
        Button searchButton = new Button("🔍 Search");
        searchButton.setStyle("-fx-background-color: #3A7CA5; -fx-text-fill: white;");

        // Results list
        ListView<GoogleBooksAPI.BookSearchResult> resultsList = new ListView<>();
        resultsList.setPrefHeight(300);
        resultsList.setPrefWidth(400);

        // Detail area
        TextArea detailArea = new TextArea();
        detailArea.setPrefHeight(150);
        detailArea.setPrefWidth(400);
        detailArea.setEditable(false);
        detailArea.setWrapText(true);

        // Buttons
        Button importButton = new Button("Import Selected Book");
        importButton.setDisable(true);
        importButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");

        Button cancelButton = new Button("Cancel");

        GoogleBooksAPI api = new GoogleBooksAPI();

        // Search action
        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Search");
                alert.setContentText("Please enter a search term");
                alert.showAndWait();
                return;
            }

            // Show loading message
            resultsList.getItems().clear();
            detailArea.setText("Searching Google Books...");

            // Search in background thread
            new Thread(() -> {
                List<GoogleBooksAPI.BookSearchResult> results = api.searchBooks(query);

                // Update UI on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    if (results.isEmpty()) {
                        detailArea.setText("No results found. Try a different search term.");
                    } else {
                        resultsList.getItems().addAll(results);
                        detailArea.setText("Found " + results.size() + " books. Select one to see details.");
                    }
                });
            }).start();
        });

        // Selection action
        resultsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String details = "Title: " + newVal.getTitle() + "\n" +
                        "Author: " + newVal.getAuthor() + "\n" +
                        "Publisher: " + newVal.getPublisher() + "\n" +
                        "Pages: " + (newVal.getPageCount() > 0 ? newVal.getPageCount() : "N/A") + "\n" +
                        "ISBN: " + (newVal.getIsbn().isEmpty() ? "N/A" : newVal.getIsbn()) + "\n\n" +
                        "Description:\n" + newVal.getDescription();
                detailArea.setText(details);
                importButton.setDisable(false);
            }
        });

        // Import action
        importButton.setOnAction(e -> {
            GoogleBooksAPI.BookSearchResult selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Create book with API data
                Book book = new Book(
                        selected.getTitle(),
                        "", // Series - user can add later
                        selected.getAuthor(),
                        ReadingStatus.TO_BE_READ,
                        selected.getCoverImageUrl(),
                        selected.getDescription(),
                        selected.getPageCount(),
                        selected.getPublisher(),
                        selected.getIsbn()
                );

                // Add to library
                String result = controller.getLibrary().addBookToTbr(book);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Imported");
                alert.setContentText(result);
                alert.showAndWait();

                // Refresh view
                controller.onToBeReadButtonClick();
                popupStage.close();
            }
        });

        cancelButton.setOnAction(e -> popupStage.close());

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10, importButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
                new Label("Search for books on Google Books:"),
                searchBox,
                new Label("Search Results:"),
                resultsList,
                new Label("Book Details:"),
                detailArea,
                buttonBox
        );

        Scene scene = new Scene(layout, 450, 700);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}