package ca.ucalcary.cpsc219.cpsc219projectfinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {
    //Initializing instance variables
    private final File fileSave;
    private BookTrackerController controller;

    /**
     * Constructor for FileSaver
     * @param fileSave - File, the file that we are loading
     */
    public FileSaver(File fileSave) {
        this.fileSave = fileSave;
    }

    /**
     * Sets the controller
     * @param controller - BookTrackerController, the controller that we are setting
     */
    public void setController(BookTrackerController controller) {
        this.controller = controller;
    }

    /**
     * Saves the data from the current program into a csv file
     * @return - Boolean of whether the data was saved successfully or not
     */
    public boolean save() {
        // Tries to open the file the user entered for writing so that we can save their current data
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(this.fileSave))) {
        //try (FileWriter fw = new FileWriter(filePath.toFile())) {
            // Writes a header so that we can organize the data
            fw.write("To Be Read List\n");
            // Loops through the to be read list and writes each details of each book to the file on their own line
            // The details of each book are separated by commas
            for (Book book: controller.getLibrary().getToBeRead()) {
                fw.write(String.format("%s,%s,%s,%s,%s%n", book.getTitle(), book.getSeries(), book.getAuthor(), book.getDate(), book.getStatus()));
            }
            // Same as above but with the finished list
            fw.write("Finished List\n");
            for (Book book: controller.getLibrary().getFinished()) {
                fw.write(String.format("%s,%s,%s,%s,%s%n", book.getTitle(), book.getSeries(), book.getAuthor(), book.getDate(), book.getStatus()));
            }
            fw.write("In Progress List\n");
            for (Book book: controller.getLibrary().getInProgress()) {
                fw.write(String.format("%s,%s,%s,%s,%s%n", book.getTitle(), book.getSeries(), book.getAuthor(), book.getDate(), book.getStatus()));
            }
            // Writes the header for the reading goals to organize the data
            fw.write("Reading Goals\n");
            // Loops through the list of goals and adds them each to their own line in the file
            for (Integer goal: controller.getGoalsList()) {
                fw.write(goal.toString()+"\n");
            }
            // This part of the code is only reached if no errors were made, therefore the file has been successfully saved and the method returns true
            return true;

            // If there is an error with the file, a message is printed and the method returns false
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Overrides toString() to provide information about the file name of the current file
     * @return - String containing the file name
    */
    @Override
    public String toString() {
        return "File: " + this.fileSave.getAbsolutePath();
    }
}