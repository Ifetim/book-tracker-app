package ca.ucalcary.cpsc219.cpsc219projectfinal;

public class ExitWrapper {
    private ExitHandler exitHandler = new DefaultExitHandler();

    // Optionally allow setting a custom ExitHandler
    /**
     * Set custom ExitHandler
     * @param handler - ExitHandler
     */
    public void setExitHandler(ExitHandler handler) {
        this.exitHandler = handler;
    }

    // Exit method uses the instance's exitHandler
    /**
     * Exit method using the instance's exitHandler
     * @param status - Int
     */
    public void exit(int status) {
        exitHandler.exit(status);
    }

    /**
     * Handles the exiting
     */
    public interface ExitHandler {
        void exit(int status);
    }

    /**
     * The default exiting used
     */
    public class DefaultExitHandler implements ExitHandler {
        @Override
        public void exit(int status) {
            System.exit(status);
        }
    }
}
