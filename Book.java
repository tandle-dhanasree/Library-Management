public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean available;

    public Book(int id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = true; // by default
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", available=" + (available ? "Yes" : "No") +
                '}';
    }
}
