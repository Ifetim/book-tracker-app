package ca.ucalcary.cpsc219.cpsc219projectfinal;

import java.util.Date;

public class Book {
    // Initializing instance variables
    private final String title;
    private final String series;
    private final String author;
    private ReadingStatus status;
    private Date date = new Date();


    private String coverImageUrl;
    private String description;
    private int pageCount;
    private String publisher;
    private String isbn;
    private double rating;

    /**
     * Book constructor
     * @param title - String that is set to be the book's title
     * @param series - String that is set to be the book's series. If a series was not entered, the series is set as N/A in the book
     * @param author - String that is set to be the book's author
     * @param status - Enum that is set to be the book's status (whether the book is in the TBR or finished)
     */
    public Book(String title, String series, String author, ReadingStatus status) {
        this.title = title;
        // If a series was not entered, the series is set as N/A to be clearer to the user
        if (series == null || series.isEmpty()) {
            this.series = "N/A";
        } else {
            this.series = series;
        }
        this.author = author;
        this.status = status;

        this.coverImageUrl = null;
        this.description = "";
        this.pageCount = 0;
        this.publisher = "";
        this.isbn = "";
        this.rating = 0.0;
    }


    public Book(String title, String series, String author, ReadingStatus status,
                String coverImageUrl, String description, int pageCount,
                String publisher, String isbn) {
        this(title, series, author, status);
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.isbn = isbn;
    }


    public String getCoverImageUrl() { return coverImageUrl; }
    public String getDescription() { return description; }
    public int getPageCount() { return pageCount; }
    public String getPublisher() { return publisher; }
    public String getIsbn() { return isbn; }
    public double getRating() { return rating; }


    /**
     * Returns the book's title
     * @return - String that is the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the book's series
     * @return - String that is the book's series
     */
    public String getSeries() {
        return series;
    }

    /**
     * Returns the book's author
     * @return - String that is the book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the book's date
     * @return - Date that is the book's date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the book's status
     * @return - Enum that is the book's status
     */
    public ReadingStatus getStatus() {
        return status;
    }

    // Added setters for the only two things that should change so then I can update status' without having to ask user

    /**
     * Provides a way for the status of a book to be updated
     * @param status - Enum that is the new status of the book
     */
    public void setStatus(ReadingStatus status) {
        this.status = status; }

    /**
     * Provides a way for the date of a book to be updated
     * @param newDate - Date that os the new date of the book
     */
    public void setDate(Date newDate) {
        this.date = newDate; }

    /**
     * toString Override
     * @return - String that contains all the books details
     */
    @Override
    public String toString() {
        return "Title: " + title + ", Series: " + series + ", Author: " + author + ", Date: " + date + ", Status: " + status;
    }

    /**
     * Checks if bookToCompare has the same title, series and author as the current book
     * @param bookToCompare - Object (book) that we are comparing to the current book
     * @return - boolean of whether the two books have the same title, series and author or not
     */
    @Override
    public boolean equals(Object bookToCompare) {
        // Compares if they are the same book --> returns true
        if (this == bookToCompare) return true;
        // Checks if the book we are comparing is null or that the classes of the two books are not equal --> returns false
        if (bookToCompare == null || this.getClass() != bookToCompare.getClass()) return false;
        // Makes a new book out of the book we are comparing
        Book otherBook = (Book) bookToCompare;
        // Compares the title, series and author of the two books. If they are all equal, returns true
        return this.title.equalsIgnoreCase(otherBook.title) && this.series.equalsIgnoreCase(otherBook.series) && this.author.equalsIgnoreCase(otherBook.author);
    }
}