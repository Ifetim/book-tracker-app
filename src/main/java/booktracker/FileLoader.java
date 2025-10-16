package booktracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class FileLoader {
    // Initializing instance variables
    protected Path dataPath;
    protected Path filePath;
    protected File fileLoad;
    protected ExitWrapper exitWrapper;
    protected BookTrackerController controller;

    /**
     * Constructor used when a file is loaded from the command line
     * @param args - String array of the arguments passed into the command line
     */
    public FileLoader(String[] args, ExitWrapper exitWrapper, File fileLoad) {

        this.exitWrapper = exitWrapper;
         if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
             this.fileLoad = fileLoad;

        // If the user passes in arguments, the system exits to avoid crashing
        } else if (args.length != 0) {
            this.dataPath = null;
            this.filePath = null;
            exitWrapper.exit(0);
        }
    }

    /**
     * Constructor used when the user chooses the option to load a file
     * @param fileLoad - File, the file that the user wants to open
     */
    public FileLoader(File fileLoad){
        this.fileLoad = fileLoad;
    }

    /**
     * Sets the controller
     * @param controller - BookTrackerController, the controller that we are setting
     */
    public void setController(BookTrackerController controller) {
        this.controller = controller;
    }

    /**
     * Loads the file that the user has entered
     * @return - Boolean that indicates if the file was loaded successfully or not
     */
    public boolean loadFile() {
        // Initializes variables and reads the first line
        try (BufferedReader reader = new BufferedReader(new FileReader(this.fileLoad))) {
            String line = reader.readLine();
            String[] parts;
            // This block continues to execute while there is lines with content
            while (line != null && !line.isEmpty()) {
                line = line.trim();
                // The line is split into a String array based on where the commas are in the file
                parts = line.trim().split(",");
                // If the line is the header Reading Goals, then we have reached the reading goals portion of the file and will add the next lines as reading goals
                if (line.equalsIgnoreCase("Reading Goals")){
                    line = reader.readLine();
                    while (line != null && !line.isEmpty()) {
                        parts = line.trim().split(",");
                        // Continues to add goals as long as the lines contains content, and the line only contains one element, and that that element is only 1 character long
                        if (parts.length == 1 && parts[0].length() == 1) {
                            int goal = Integer.parseInt(line);
                            controller.addToMilestoneList(goal);
                        }
                        line = reader.readLine();
                    }


                    // If the length of parts is equals to 5, that means that the line corresponds to book info, so each part will be assigned to an attribute of the book
                } else if (parts.length == 5 && parts[4] != null) {
                    String title = parts[0];
                    String series = parts[1];
                    String author = parts[2];
                    String dateString = parts[3];
                    Date date = new Date(dateString);
                    ReadingStatus status = ReadingStatus.valueOf(parts[4].trim());
                    // Checks what the status of the book is and adds it to the correct list in library
                    if (status.equals(ReadingStatus.TO_BE_READ)) {
                        Book book = new Book(title, series, author, ReadingStatus.TO_BE_READ);
                        book.setDate(date);
                        controller.getLibrary().addBookToTbr(book);
                    } else if (status.equals(ReadingStatus.FINISHED)) {
                        Book book = new Book(title, series, author, ReadingStatus.FINISHED);
                        book.setDate(date);
                        controller.getLibrary().addBookToFinished(book);
                    } else if (status.equals(ReadingStatus.IN_PROGRESS)) {
                        Book book = new Book(title, series, author, ReadingStatus.IN_PROGRESS);
                        book.setDate(date);
                        controller.getLibrary().addBookToInProgress(book);
                    }
                }
                // Reads the next line to analyze
                line = reader.readLine();
            }

            controller.onAllBooksButtonClick();
            // Update the listener so the search bar will work properly
            List<String> updatedTitlesAndAuthors = new ArrayList<>();
            for (Book b : controller.getLibrary().getAllBooks()) {
                if (!updatedTitlesAndAuthors.contains(b.getTitle().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getTitle().toUpperCase());
                }
                if (b.getAuthor() != null && !updatedTitlesAndAuthors.contains(b.getAuthor().toUpperCase())) {
                    updatedTitlesAndAuthors.add(b.getAuthor().toUpperCase());
                }
            }

            // Most recent info should show in search suggestion
            controller.setupSuggestionsMenuListener(updatedTitlesAndAuthors);

            // This part of the code is only reached if the code successfully runs without errors, meaning the file was loaded and the method returns true
            return true;
            // If the system encounters an IO error, a message is printed out and false is returned since the data did not load successfully
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the file path
     * @return - String that is the file path of the file being read
     */
    public String getFilePath() {
        if (filePath != null) {
            return this.filePath.toString();
        } else {
            return null;
        }
    }

    /**
     * Override of toString to display filename information
     * @return - String that is the information about the file name of the current file
     */
    @Override
    public String toString() {
        return "File: " + this.fileLoad;
    }
}