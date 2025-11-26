import java.time.LocalDate;

public class Loan {
    private int id;
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean returned;

    public Loan(int id, Book book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void markReturned() {
        this.returned = true;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookId=" + book.getId() +
                ", bookTitle='" + book.getTitle() + '\'' +
                ", memberId=" + member.getId() +
                ", memberName='" + member.getName() + '\'' +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returned=" + (returned ? "Yes" : "No") +
                '}';
    }
}
