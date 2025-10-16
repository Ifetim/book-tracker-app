package ca.ucalcary.cpsc219.cpsc219projectfinal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class GoogleBooksAPI {

    private static final String API_BASE_URL = "https://ww.googleapis.com/books/v1/volumes";

    private static final String API_KEY = "";
    private final OkHttpClient client;

    public GoogleBooksAPI() {
        this.client = new OkHttpClient();
    }


    /**
     * Search for books by title or author
     *
     * @param query - Search term (title, author, or ISBN)
     * @return List of BookSearchResult objects
     */

    public List<BookSearchResult> searchBooks(String query) {
        List<BookSearchResult> results = new ArrayList<>();

        try {

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // Build URL with or without API key
            String url = API_KEY.equals("YOUR_API_KEY_HERE")
                    ? API_BASE_URL + "?q=" + encodedQuery + "&maxResults=10"
                    : API_BASE_URL + "?q=" + encodedQuery + "&maxResults=10&key=" + API_KEY;

            // Make HTTP request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("API request failed: " + response.code());
                    return results;
                }

                // Parse JSON response
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                if (!jsonObject.has("items")) {
                    return results; // No results found
                }

                JsonArray items = jsonObject.getAsJsonArray("items");

                // Process each book result
                for (int i = 0; i < items.size(); i++) {
                    JsonObject item = items.get(i).getAsJsonObject();
                    JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                    BookSearchResult result = parseBookResult(volumeInfo);
                    if (result != null) {
                        results.add(result);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }

        return results;
    }

    /**
     * Parse a single book result from JSON
     */
    private BookSearchResult parseBookResult(JsonObject volumeInfo) {
        try {
            // Extract title
            String title = volumeInfo.has("title")
                    ? volumeInfo.get("title").getAsString()
                    : "Unknown Title";

            // Extract authors
            String author = "Unknown Author";
            if (volumeInfo.has("authors")) {
                JsonArray authors = volumeInfo.getAsJsonArray("authors");
                if (authors.size() > 0) {
                    author = authors.get(0).getAsString();
                }
            }

            // Extract publisher
            String publisher = volumeInfo.has("publisher")
                    ? volumeInfo.get("publisher").getAsString()
                    : "";

            // Extract description
            String description = volumeInfo.has("description")
                    ? volumeInfo.get("description").getAsString()
                    : "";

            // Truncate long descriptions
            if (description.length() > 500) {
                description = description.substring(0, 497) + "...";
            }

            // Extract page count
            int pageCount = volumeInfo.has("pageCount")
                    ? volumeInfo.get("pageCount").getAsInt()
                    : 0;

            // Extract ISBN
            String isbn = "";
            if (volumeInfo.has("industryIdentifiers")) {
                JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                for (int j = 0; j < identifiers.size(); j++) {
                    JsonObject id = identifiers.get(j).getAsJsonObject();
                    if (id.get("type").getAsString().contains("ISBN")) {
                        isbn = id.get("identifier").getAsString();
                        break;
                    }
                }
            }

            // Extract cover image URL
            String coverUrl = null;
            if (volumeInfo.has("imageLinks")) {
                JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                if (imageLinks.has("thumbnail")) {
                    coverUrl = imageLinks.get("thumbnail").getAsString();
                    // Use HTTPS
                    coverUrl = coverUrl.replace("http:", "https:");
                }
            }

            return new BookSearchResult(title, author, publisher, description,
                    pageCount, isbn, coverUrl);

        } catch (Exception e) {
            System.err.println("Error parsing book result: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inner class to hold search results
     */
    public static class BookSearchResult {
        private final String title;
        private final String author;
        private final String publisher;
        private final String description;
        private final int pageCount;
        private final String isbn;
        private final String coverImageUrl;

        public BookSearchResult(String title, String author, String publisher,
                                String description, int pageCount, String isbn,
                                String coverImageUrl) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.description = description;
            this.pageCount = pageCount;
            this.isbn = isbn;
            this.coverImageUrl = coverImageUrl;
        }

        // Getters
        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDescription() {
            return description;
        }

        public int getPageCount() {
            return pageCount;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getCoverImageUrl() {
            return coverImageUrl;
        }

        @Override
        public String toString() {
            return title + " by " + author +
                    (pageCount > 0 ? " (" + pageCount + " pages)" : "");
        }
    }
}

