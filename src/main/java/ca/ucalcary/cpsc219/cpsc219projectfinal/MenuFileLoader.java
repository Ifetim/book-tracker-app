package ca.ucalcary.cpsc219.cpsc219projectfinal;

import java.io.File;

// Extends the abstract class file loader, and therefore takes uses its instance variables and methods
public class MenuFileLoader extends FileLoader {

    /**
     * Constructor used for loading a file from user input in the menu options
     * @param fileLoad - File, the file we want to load data from
     */
    public MenuFileLoader(File fileLoad) {
        super(fileLoad);
    }
}
