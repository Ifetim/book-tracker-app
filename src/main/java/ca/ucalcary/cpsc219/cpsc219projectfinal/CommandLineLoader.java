package ca.ucalcary.cpsc219.cpsc219projectfinal;

import java.io.File;

// Extends the abstract class file loader, and therefore takes uses its instance variables and methods
public class CommandLineLoader extends FileLoader {

    /**
     * Constructor for loading a file from the command line
     * @param args - String array of the arguments from the command line
     */
    public CommandLineLoader(String[] args, ExitWrapper exitWrapper, File fileLoad) {
        super(args, exitWrapper, fileLoad);
    }
}
