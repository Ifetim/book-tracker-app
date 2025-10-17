# 📚 Book Tracker

A JavaFX desktop application for managing personal book collections with Google Books API integration for automated retrieval.


## ✨ Features

### 📖 Book Management
- **Multiple Reading Lists** - Organize books across To Be Read, In Progress, and Finished
- **Smart Search** - Real-time search with autocomplete for titles and authors
- **Book Cards** - Visual grid display with hover effects and easy navigation
- **CRUD Operations** - Add, move, remove, and update books seamlessly

### 🌐 Google Books API Integration
- **Automated Metadata** - Fetch book details, descriptions, and cover images
- **Search by Title/Author/ISBN** - Find books instantly from Google's vast library
- **One-Click Import** - Add books with all details pre-filled

### 🎯 Reading Goals & Analytics
- **Milestone Tracking** - Set reading goals and visualize progress with progress bars
- **Top Authors** - View your most-read authors with statistics
- **Yearly Analytics** - Filter books completed in specific years
- **Reading Recommendations** - Get random book suggestions from your TBR list

### 💾 Data Persistence
- **Save/Load** - Export and import your library as text files
- **Auto-save Prompt** - Never lose your data with exit confirmation dialogs
- **Command-line Loading** - Quick load saved data with `load` argument

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java 23** | Core programming language |
| **JavaFX 23.0.2** | User interface framework |
| **Maven** | Build automation and dependency management |
| **OkHttp 4.12.0** | HTTP client for API requests |
| **Gson 2.10.1** | JSON parsing and serialization |
| **Google Books API** | Book metadata |

---

## 🚀 Getting Started

### Prerequisites

- **Java 23 or newer** - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **JavaFX SDK 23.0.2** - [Download from OpenJFX](https://openjfx.io/)
- **Maven 3.6+** (for building from source)

### Run Pre-built JAR

1. **Download** `book-tracker-app-1.0.0-shaded.jar` from releases

2. **Download JavaFX SDK** from https://openjfx.io/ and unzip it

3. **Run the application:**

   **macOS/Linux:**
```bash
   java --module-path "/path/to/javafx-sdk-23.0.2/lib" \
        --add-modules javafx.controls,javafx.fxml \
        -jar book-tracker-app-1.0.0-shaded.jar
```

   **Windows:**
```cmd
   java --module-path "C:\path\to\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar book-tracker-app-1.0.0-shaded.jar
```

4. **Load saved data (optional):**
```bash
   java --module-path "..." --add-modules javafx.controls,javafx.fxml -jar book-tracker-app-1.0.0-shaded.jar load
```


## 📖 How to Use

### Adding Books

**Option 1: Search Google Books API**
1. Click `Edit` → `Search Google Books`
2. Enter book title, author, or ISBN
3. Select from results
4. Click `Import Selected Book`

**Option 2: Manual Entry**
1. Click `Edit` → `Add Book Manually`
2. Fill in title, author, and series (optional)
3. Select reading status
4. Click `Add`

### Managing Your Library

- **View Lists:** Click sidebar buttons (All, In Progress, To Be Read, Finished)
- **Search Books:** Type in search bar for instant autocomplete suggestions
- **Move Books:** `Edit` → `Move Book` to change reading status
- **Delete Books:** `Edit` → `Delete Book` to remove from library
- **Book Details:** Click any book card to see full information

### Setting Reading Goals

1. `Milestones` → `Set Milestone`
2. Enter target number of books
3. `Milestones` → `See Progress` to track completion

### Saving & Loading Data

- **Save:** `File` → `Save` (exports to `.txt` file)
- **Load:** `File` → `Load` (imports from `.txt` file)
- **Auto-load:** Run with `load` argument to load `data/SavedData.txt`

---

## 🏗️ Project Structure
```
book-tracker/
├── src/
│   └── main/
│       ├── java/booktracker/
│       │   ├── BookTrackerApplication.java    # Main application entry
│       │   ├── BookTrackerController.java     # UI controller (MVC)
│       │   ├── Book.java                      # Book model
│       │   ├── Library.java                   # Book collection manager
│       │   ├── GoogleBooksAPI.java            # API integration
│       │   ├── AddBookPopup.java              # Popup dialogs
│       │   ├── FileSaver.java                 # Data persistence
│       │   ├── FileLoader.java                # Data loading
│       │   ├── ReadingStatus.java             # Enum for book status
│       │   └── ListType.java                  # Enum for list types
│       └── resources/
│           └── booktracker/
│               └── book-tracker-view.fxml     # UI layout
├── data/                                       # Saved data directory
├── pom.xml                                     # Maven configuration
└── README.md
```

---

## 👥 Project History

This application was originally developed as a team project for **CPSC 219** (Object-Oriented Programming) at the University of Calgary in **Winter 2025**.

### Original Team Members
- Amelia Custodio
- Sarah Giesbrecht
- Gray Wilson
- Jessie Estrada
- Ife Timothy

### Post-Course Enhancements

After completing the course, I significantly enhanced the project with:

**✨ Google Books API Integration**
- Implemented HTTP client using OkHttp
- Added JSON parsing with Gson
- Created search interface with live results
- Handled API errors and edge cases

**🎨 UI/UX Improvements**
- Redesigned book cards with modern styling
- Added hover effects and smooth transitions
- Improved color scheme and typography
- Enhanced error messaging and user feedback
- Polished overall user experience

**🔧 Code Quality**
- Refactored for better encapsulation
- Added comprehensive error handling
- Improved documentation with Javadocs
- Enhanced data validation
- Fixed window sizing issues


## 🤝 Contributing

This is a personal learning project, but suggestions and feedback are welcome!

---

## 👤 Contact

**Ife Timothy**
- LinkedIn: www.linkedin.com/in/ifeoluwatim
- Email: ifetimo1@gmail.com

---

## 📚 Learning Outcomes

Through this project, I gained hands-on experience with:

- ✅ RESTful API integration and consumption
- ✅ HTTP client implementation with OkHttp
- ✅ JSON parsing and data transformation
- ✅ JavaFX UI development and event handling
- ✅ MVC architecture implementation
- ✅ File I/O and data persistence
- ✅ Error handling and data validation
- ✅ Maven dependency management
- ✅ Object-oriented design principles
- ✅ Collaborative software development
- ✅ Git version control and code management

---

**Built with ❤️ using Java and JavaFX**
