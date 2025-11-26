import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private List<Book> books;
    private List<Member> members;
    private List<Loan> loans;

    private int bookIdCounter = 1;
    private int memberIdCounter = 1;
    private int loanIdCounter = 1;

    public LibraryService() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        loans = new ArrayList<>();
        seedData(); // optional sample data
    }

    private void seedData() {
        addBook("The Alchemist", "Paulo Coelho", "Fiction");
        addBook("Clean Code", "Robert C. Martin", "Programming");
        addBook("Introduction to Algorithms", "Cormen", "Education");

        addMember("Rahul", "rahul@example.com", "9876543210");
        addMember("Priya", "priya@example.com", "9123456780");
    }

    // Getter methods for GUI
    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    // ---------- BOOK OPERATIONS ----------

    public void addBook(String title, String author, String category) {
        Book book = new Book(bookIdCounter++, title, author, category);
        books.add(book);
        System.out.println("Book added successfully with ID: " + book.getId());
    }

    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    public Book findBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    public void searchBooksByTitle(String keyword) {
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found with title containing: " + keyword);
        }
    }

    public void searchBooksByAuthor(String keyword) {
        boolean found = false;
        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found with author containing: " + keyword);
        }
    }

    // ---------- MEMBER OPERATIONS ----------

    public void addMember(String name, String email, String phone) {
        Member m = new Member(memberIdCounter++, name, email, phone);
        members.add(m);
        System.out.println("Member added successfully with ID: " + m.getId());
    }

    public void listAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        for (Member m : members) {
            System.out.println(m);
        }
    }

    public Member findMemberById(int id) {
        for (Member m : members) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    // ---------- LOAN OPERATIONS ----------

    public void issueBook(int bookId, int memberId) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already issued to someone else.");
            return;
        }

        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14); // 2 weeks
        Loan loan = new Loan(loanIdCounter++, book, member, issueDate, dueDate);
        loans.add(loan);
        book.setAvailable(false);

        System.out.println("Book issued successfully.");
        System.out.println(loan);
    }

    public void returnBook(int loanId) {
        Loan loan = null;
        for (Loan l : loans) {
            if (l.getId() == loanId) {
                loan = l;
                break;
            }
        }

        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        if (loan.isReturned()) {
            System.out.println("This loan is already marked as returned.");
            return;
        }

        loan.markReturned();
        loan.getBook().setAvailable(true);
        System.out.println("Book returned successfully.");
        System.out.println(loan);
    }

    public void listAllLoans() {
        if (loans.isEmpty()) {
            System.out.println("No loan records found.");
            return;
        }
        for (Loan l : loans) {
            System.out.println(l);
        }
    }

    public void listActiveLoans() {
        boolean found = false;
        for (Loan l : loans) {
            if (!l.isReturned()) {
                System.out.println(l);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No active loans found.");
        }
    }
}
