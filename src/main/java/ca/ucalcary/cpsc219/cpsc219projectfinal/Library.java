package ca.ucalcary.cpsc219.cpsc219projectfinal;

import javafx.scene.control.Alert;

import java.util.*;

public class Library {

    // Add books to be read to to-be finished list, and we can edit books here

    // Initialize variables
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> toBeRead = new ArrayList<>();
    private List<Book> finished = new ArrayList<>();
    private List<Book> inProgress = new ArrayList<>();
    private HashSet<Integer> goalsList = new HashSet<>();

    private int numBooksInTBR = 0;
    private int totalFinishedBooks = 0;
    private int booksInProgress = 0;
    private final int MAX_BOOKS_IN_TBR = 100;

    private Map<String, Integer> finishedYears = new HashMap<>();
    private Map<String, Integer> authorCount = new HashMap<>();


    /**
     * Checks for duplicates in the TBR list via {@code bookExists(book)}
     * @param book {@link Book} object is added to TBR list
     * @return message whether addition to TBR is successful or not
     */
    public String addBookToTbr(Book book) {
        // Checks if the book is already in the library
        if (bookExists(book)){
            boolean bookInFinished = false;
            for (Book otherBook : finished) {
                // Using override .equals method
                if (book.equals(otherBook)) {
                    bookInFinished = true;
                    break;
                }
            } if (bookInFinished) {
                return "This book is currently in your finished list\n" +
                        "To move the book to your TBR list, use option 4";
            } else {
                return "This book is already in your TBR list";
            }

            // If the book is not already in the library then it can be added, with the status of TBR
        } else if (book.getStatus() == ReadingStatus.TO_BE_READ && toBeRead.size() < MAX_BOOKS_IN_TBR) {
            toBeRead.add(book);
            System.out.println(book);
            numBooksInTBR++;
            return ("Book added to your TBR.");
        } else {
            return ("There are too many books in to be read list");
        }
    }

    /**
     * Checks for duplicates in finished list with {@code bookExists(book)}
     * @param book {@link Book} object is added to finished list
     * @return message whether book was added successfully or input was invalid
     */

    public String addBookToFinished(Book book) {
        // Checks if the book is already in the library
        if (bookExists(book)) {
            boolean bookInTBR = false;
            for (Book otherBook : toBeRead) {
                // Using overide .equals method
                if (book.equals(otherBook)) {
                    bookInTBR = true;
                    break;
                }
            } if (bookInTBR) {
                return "This book is currently in your TBR list\n" +
                        "To move the book to your finished list, use option 4";
            } else {
                return "This book is already in your finished list";
            }

            // If the book is not already in the library then it can be added, with the status of finished
        } else if (book.getStatus() == ReadingStatus.FINISHED ) {
            finished.add(book);
            totalFinishedBooks++;
            return "Book added to your finished reading list.";
        }
        return "Invalid input";
    }

    /**
     * Checks for duplicates in other lists {@code bookExists(book)}
     * @param book {@link Book} object is added to in progress list
     * @return message if input was invalid
     */

    public String addBookToInProgress(Book book) {
        if (bookExists(book)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Book is already in your TBR or finished list");
            alert.showAndWait();
        } else {
            inProgress.add(book);
            booksInProgress++;
        }
        return "Invalid input";
    }

    /**
     * Iterates through toBeRead to find book
     * Book is removed if found via {@code toBeRead.remove}
     * Prints out informative message if book not found
     * @param title is the title of the book to be found
     */

    public void removeBook(String title, ReadingStatus listType) {
        // If the book exists in the library, then the TBR list gets looped through to fins the book that corresponds to the title and remove it
        switch (listType) {
            case TO_BE_READ:
                for (int i = 0; i < toBeRead.size(); i++) {
                    if (toBeRead.get(i).getTitle().equalsIgnoreCase(title)) {
                        toBeRead.remove(i);
                        numBooksInTBR--;
                        System.out.println("Book removed from To Be Read.");
                        return;
                    }
                }
                break;

            case FINISHED:
                for (int i = 0; i < finished.size(); i++) {
                    if (finished.get(i).getTitle().equalsIgnoreCase(title)) {
                        finished.remove(i);
                        totalFinishedBooks--;
                        System.out.println("Book removed from Finished.");
                        return;
                    }
                }
                break;

            case IN_PROGRESS:
                for (int i = 0; i < inProgress.size(); i++) {
                    if (inProgress.get(i).getTitle().equalsIgnoreCase(title)) {
                        inProgress.remove(i);
                        System.out.println("Book removed from In Progress.");
                        return;
                    }
                }
                break;

            default:
                System.out.println("Invalid list type.");
        }

    }


    /**
     * Prints out total number of books read via {@code totalFinishedBooks}
     */

    public String totalFinishedBooks() {
        return "Total books finished: " + totalFinishedBooks;
    }



    /**
     * Prompts user to enter title of book they want to see
     * @link Scanner reads title of book
     * Looks for book title in {@code toBeRead}
     * Looks for book title in {@code finished}
     * If book is found in either list details are printed
     * Prints informative message if book title not found
     */

    public List<String> getBookDetails(String input) {

        List<String> bookDetails = new ArrayList<>();
        boolean bookFound = false;
        for (Book book : allBooks) {
            if (book.getTitle().equalsIgnoreCase(input) || book.getAuthor().equalsIgnoreCase(input)) {
                bookDetails.add(book.getTitle());
                bookDetails.add(book.getAuthor());
                bookDetails.add(book.getSeries());
                bookDetails.add(String.valueOf(book.getDate()));
                switch (book.getStatus()) {
                    case IN_PROGRESS -> bookDetails.add("In Progress");
                    case TO_BE_READ -> bookDetails.add("To Be Read");
                    case FINISHED -> bookDetails.add("Finished");
                    default -> bookDetails.add("None");
                }
                if (book.getAuthor().equalsIgnoreCase(input)) {
                    bookDetails.add("author");
                } else {
                    bookDetails.add("title");
                }
                return bookDetails;
            }
        }

        // Checks each book in TBR to see if the title of the book matches the one the user inputted
        for (Book book : toBeRead) {
            if (book.getTitle().equalsIgnoreCase(input) || book.getAuthor().equalsIgnoreCase(input)) {
                bookDetails.add(book.getTitle());
                bookDetails.add(book.getAuthor());
                bookDetails.add(book.getSeries());
                bookDetails.add(String.valueOf(book.getDate()));
                if (book.getAuthor().equalsIgnoreCase(input)) {
                    bookDetails.add("author");
                } else {
                    bookDetails.add("title");
                }
                return bookDetails;
            }
        }
        // Checks each book in finished to see if the title of the book matches the one the user inputted
            for (Book book : finished) {
                if (book.getTitle().equalsIgnoreCase(input) || book.getAuthor().equalsIgnoreCase(input)) {
                    bookDetails.add(book.getTitle());
                    bookDetails.add(book.getAuthor());
                    bookDetails.add(book.getSeries());
                    bookDetails.add(String.valueOf(book.getDate()));
                    if (book.getAuthor().equalsIgnoreCase(input)) {
                        bookDetails.add("author");
                    } else {
                        bookDetails.add("title");
                    }
                    return bookDetails;
                }
            }
        //Checks in the in progress list to see if the title of the book matches the ones the user inputted
            for (Book book : inProgress) {
                if (book.getTitle().equalsIgnoreCase(input) || book.getAuthor().equalsIgnoreCase(input)) {
                    bookDetails.add(book.getTitle());
                    bookDetails.add(book.getAuthor());
                    bookDetails.add(book.getSeries());
                    bookDetails.add(String.valueOf(book.getDate()));
                    if (book.getAuthor().equalsIgnoreCase(input)) {
                        bookDetails.add("author");
                    } else {
                        bookDetails.add("title");
                    }
                    return bookDetails;
                }
            }
        bookDetails = null;
        return bookDetails;
    }

    public List<Book> getAuthorsBooks(String author) {
        List<Book> authorsBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (author.equalsIgnoreCase(book.getAuthor())) {
                authorsBooks.add(book);
            }
        }
        for (Book book : toBeRead) {
            if (author.equalsIgnoreCase(book.getAuthor())) {
                authorsBooks.add(book);
            }
        }
        for (Book book : inProgress) {
            if (author.equalsIgnoreCase(book.getAuthor())) {
                authorsBooks.add(book);
            }
        }
        for (Book book : finished) {
            if (author.equalsIgnoreCase(book.getAuthor())) {
                authorsBooks.add(book);
            }
        }
        return authorsBooks;
    }

    /**
     * Adds integer to list of milestones
     * @param goal is added to {@code goalsList}
     */
    public void addReadingMilestone(Integer goal) {
        goalsList.add(goal);
    }

    /**
     * Loops though finished books and gets author name
     * Adds a +1 to author count for each book found by that author
     * Formats top five authors in descending order
     * @return String of top five authors + number of books read
     */

    public String topAuthors(){
        authorCount.clear();
        // Looks through every book in the finished list
        for (Book book : finished) {
            if (book.getStatus() == ReadingStatus.FINISHED) {
                String author = book.getAuthor().toUpperCase();
                // Puts the author of the book in a HashMap with the count of how many books the user has read from that author
                authorCount.put(author, authorCount.getOrDefault(author, 0) + 1);
            }
        }
        List<Map.Entry<String, Integer>> descendingAuthors = new ArrayList<>(authorCount.entrySet());
        // Sorts the authors by the number of books the user has read from that author
        descendingAuthors.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        // Prints out the top 5 authors with their rank
        String formattedAuthor = "";
        int authorRank = 1;
        for (Map.Entry<String, Integer> entry : descendingAuthors) {
            formattedAuthor += (authorRank + ". " + entry.getKey() + " - " + entry.getValue() + " book(s) read\n");
            if (authorRank++ == 5) break;
        }
        return formattedAuthor.trim();
    }

    /**
     * Checks if a book title matches any of the books already in the library
     * @param bookTitle - String that is the name the book that the user has entered
     * @return - boolean of whether the book title exists in the library
     */
    public boolean bookTitleExists(String bookTitle) {
        // Loops through TBR and checks if the title entered matches the title of the books already in the list, returns true if it does
        for (Book book : toBeRead) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                return true;
            }
        }
        // Same as above but with the finished list
        for (Book book : finished) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                return true;
            }
        }
        for (Book book : inProgress) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                return true;
            }
        }

        // This code is only reached if the title is not found in either list, meaning the book does not exist and the method returns false
        return false;
    }

    /**
     * Checks if a book already exists in the library
     * @param newBook - Book, the new book that the user has entered
     * @return - boolean of whether the new book already exists in the library
     */
    protected boolean bookExists(Book newBook) {
        // Loops through the TBR list and checks if the new book equals the books in the TBR by using the .equals override
        // If it does equal one of the books, the method returns true
        for (Book otherBook : toBeRead) {
            if (newBook.equals(otherBook)) {
                return true;
            }
        }
        // Same as above but with the finished list
        for (Book otherBook : finished) {
            if (newBook.equals(otherBook)) {
                return true;
            }
        }

        for (Book otherBook : inProgress) {
            if (newBook.equals(otherBook)) {
                return true;
            }
        }

        for (Book otherBook: inProgress) {
            if (newBook.equals(otherBook)) {
                return true;
            }
        }
        // This code is only reached if the book is not found in either list, meaning the book does not exist and the method returns false
        return false;
    }

    /**
     * Returns all objects in TBR list
     * @return {@link List} of {@link Book} objects in the TBR list
     */
    public List<Book> getToBeRead() {
        return toBeRead;
    }

    /**
     * Returns all objects in finished list
     * @return {@link List} of {@link Book} objects in finished list
     */
    public List<Book> getFinished() {
        return finished;
    }

    /**
     * Replaces list of finished books
     * @param finished {@link List} of {@link Book} objects to set to finish
     */
    public void setFinished(List<Book> finished) {
        this.finished = finished;
    }

    /**
     * Returns all integers in the milestone list
     * @return {@code goalsList}
     */
    public HashSet<Integer> getGoalsList() {
        return goalsList;
    }

    public void setGoalsList(HashSet<Integer> goalsList) {
        this.goalsList = goalsList;
    }

    /**
     * Gets integer value of {@code numBooksInTBR}
     * @return numBooksInTBR
     */
    public int getNumBooksInTBR() {
        return numBooksInTBR;
    }

    /**
     * Gets integer value of {@code totalFinishedBooks}
     * @return totalFinishedBooks
     */
    public int getTotalFinishedBooks() {
        return totalFinishedBooks;
    }

    /**
     * Gets map of finished years, associating book titles with the year it was finished
     * @return {@link Map} containing titles {@link String} as keys and year {@link Integer} as values
     */
    public Map<String, Integer> getFinishedYears() {
        return finishedYears;
    }

    /**
     * Gets map of author count, associated with author name
     * @return {@link Map} containing author names {@link String} as keys and count {@link Integer} as values
     */
    public Map<String, Integer> getAuthorCount() {
        return authorCount;
    }

    /**
     * Sets the number of books read in a year
     * @param finishedYears - Hash map of the finished years plus the number of books read that year
     */
    public void setFinishedYears(Map<String, Integer> finishedYears) {
        this.finishedYears = finishedYears;
    }

    /**
     * Sets the author's book count
     * @param authorCount - Hash map of the authors plus the number of books the user has read from that author
     */
    public void setAuthorCount(Map<String, Integer> authorCount) {
        this.authorCount = authorCount;
    }

    public List<Book> getInProgress() {
        return inProgress;
    }

    public int getBooksInProgress() {
        return booksInProgress;
    }

    public List<Book> getAllBooks() {
        allBooks.clear();
        for (Book book : inProgress) {
            allBooks.add(book);
        }
        for (Book book : toBeRead) {
            allBooks.add(book);
        }
        for (Book book : finished) {
            allBooks.add(book);
        }
        return allBooks;
    }

    /**
     * Searches through in progress, to be read, and finished list for book title
     * @param title The title of the book to search for.
     * @return The {@link Book} object if found, otherwise returns null.
     */
    public Book getBookByTitle(String title) {
        for (Book book : toBeRead) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        for (Book book : inProgress) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        for (Book book : finished) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Override of toString() to return the entire library of books
     * @return - String that lists the entire library
     */
    @Override
    public String toString() {
        return "TBR List: " + getToBeRead() + "\nFinished Books: " + getFinished() + "\nTotal Books Read: " + getTotalFinishedBooks();
    }
}
