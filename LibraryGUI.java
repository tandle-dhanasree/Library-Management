import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LibraryGUI extends JFrame {

    private LibraryService libraryService;
    private JTabbedPane tabbedPane;
    private boolean isDarkMode = false;

    // Color schemes
    private Color lightBg = new Color(255, 255, 255);
    private Color lightFg = new Color(30, 30, 30);
    private Color lightAccent = new Color(0, 51, 102); // Navy blue
    private Color lightSecondary = new Color(240, 245, 250);

    private Color darkBg = new Color(30, 30, 30);
    private Color darkFg = new Color(240, 240, 240);
    private Color darkAccent = new Color(70, 130, 180); // Steel blue
    private Color darkSecondary = new Color(45, 45, 45);

    // Dashboard components
    private JLabel totalBooksLabel;
    private JLabel totalMembersLabel;
    private JLabel activeLoansLabel;
    private JLabel availableBooksLabel;

    // Table models
    private DefaultTableModel booksTableModel;
    private DefaultTableModel membersTableModel;
    private DefaultTableModel loansTableModel;

    public LibraryGUI(LibraryService service) {
        this.libraryService = service;

        setTitle("Library Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        applyTheme();
        refreshAllData();
    }

    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Inter", Font.PLAIN, 14));

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Loans", createLoansPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));

        JButton themeToggle = new JButton("Dark Mode");
        themeToggle.setFont(new Font("Inter", Font.PLAIN, 12));
        themeToggle.setFocusPainted(false);
        themeToggle.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            themeToggle.setText(isDarkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(themeToggle, BorderLayout.EAST);

        return header;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        totalBooksLabel = new JLabel("0");
        totalMembersLabel = new JLabel("0");
        activeLoansLabel = new JLabel("0");
        availableBooksLabel = new JLabel("0");

        statsPanel.add(createStatCard("Total Books", totalBooksLabel));
        statsPanel.add(createStatCard("Total Members", totalMembersLabel));
        statsPanel.add(createStatCard("Active Loans", activeLoansLabel));
        statsPanel.add(createStatCard("Available Books", availableBooksLabel));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Recent activity
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Recent Activity"));

        JTextArea activityArea = new JTextArea(10, 40);
        activityArea.setEditable(false);
        activityArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        activityArea.setText(
                "Welcome to Library Management System!\n\nUse the tabs above to:\n• Manage Books\n• Manage Members\n• Handle Book Loans");

        JScrollPane scrollPane = new JScrollPane(activityArea);
        activityPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(activityPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        valueLabel.setFont(new Font("Inter", Font.BOLD, 36));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("+ Add Book");
        addButton.setFont(new Font("Inter", Font.PLAIN, 14));
        addButton.addActionListener(e -> showAddBookDialog());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Inter", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> refreshBooksTable());

        topPanel.add(addButton);
        topPanel.add(refreshButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Title", "Author", "Category", "Available" };
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable booksTable = new JTable(booksTableModel);
        booksTable.setFont(new Font("Inter", Font.PLAIN, 12));
        booksTable.setRowHeight(30);
        booksTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("+ Add Member");
        addButton.setFont(new Font("Inter", Font.PLAIN, 14));
        addButton.addActionListener(e -> showAddMemberDialog());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Inter", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> refreshMembersTable());

        topPanel.add(addButton);
        topPanel.add(refreshButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Name", "Email", "Phone" };
        membersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable membersTable = new JTable(membersTableModel);
        membersTable.setFont(new Font("Inter", Font.PLAIN, 12));
        membersTable.setRowHeight(30);
        membersTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(membersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton issueButton = new JButton("+ Issue Book");
        issueButton.setFont(new Font("Inter", Font.PLAIN, 14));
        issueButton.addActionListener(e -> showIssueBookDialog());

        JButton returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("Inter", Font.PLAIN, 14));
        returnButton.addActionListener(e -> showReturnBookDialog());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Inter", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> refreshLoansTable());

        topPanel.add(issueButton);
        topPanel.add(returnButton);
        topPanel.add(refreshButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Loan ID", "Book Title", "Member Name", "Issue Date", "Due Date", "Status" };
        loansTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable loansTable = new JTable(loansTableModel);
        loansTable.setFont(new Font("Inter", Font.PLAIN, 12));
        loansTable.setRowHeight(30);
        loansTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(loansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Add Book");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            libraryService.addBook(title, author, category);
            refreshAllData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAddMemberDialog() {
        JDialog dialog = new JDialog(this, "Add New Member", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Add Member");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            libraryService.addMember(name, email, phone);
            refreshAllData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Member added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showIssueBookDialog() {
        JDialog dialog = new JDialog(this, "Issue Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> bookCombo = new JComboBox<>();
        JComboBox<String> memberCombo = new JComboBox<>();

        // Populate books
        for (Book book : libraryService.getBooks()) {
            if (book.isAvailable()) {
                bookCombo.addItem(book.getId() + " - " + book.getTitle());
            }
        }

        // Populate members
        for (Member member : libraryService.getMembers()) {
            memberCombo.addItem(member.getId() + " - " + member.getName());
        }

        formPanel.add(new JLabel("Select Book:"));
        formPanel.add(bookCombo);
        formPanel.add(new JLabel("Select Member:"));
        formPanel.add(memberCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton issueButton = new JButton("Issue Book");
        JButton cancelButton = new JButton("Cancel");

        issueButton.addActionListener(e -> {
            if (bookCombo.getSelectedItem() == null || memberCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(dialog, "Please select both book and member!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String bookStr = (String) bookCombo.getSelectedItem();
            String memberStr = (String) memberCombo.getSelectedItem();

            int bookId = Integer.parseInt(bookStr.split(" - ")[0]);
            int memberId = Integer.parseInt(memberStr.split(" - ")[0]);

            libraryService.issueBook(bookId, memberId);
            refreshAllData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Book issued successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(issueButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showReturnBookDialog() {
        JDialog dialog = new JDialog(this, "Return Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> loanCombo = new JComboBox<>();

        // Populate active loans
        for (Loan loan : libraryService.getLoans()) {
            if (!loan.isReturned()) {
                loanCombo.addItem(
                        loan.getId() + " - " + loan.getBook().getTitle() + " (" + loan.getMember().getName() + ")");
            }
        }

        formPanel.add(new JLabel("Select Loan:"));
        formPanel.add(loanCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton returnButton = new JButton("Return Book");
        JButton cancelButton = new JButton("Cancel");

        returnButton.addActionListener(e -> {
            if (loanCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a loan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String loanStr = (String) loanCombo.getSelectedItem();
            int loanId = Integer.parseInt(loanStr.split(" - ")[0]);

            libraryService.returnBook(loanId);
            refreshAllData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(returnButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void refreshBooksTable() {
        booksTableModel.setRowCount(0);
        for (Book book : libraryService.getBooks()) {
            booksTableModel.addRow(new Object[] {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void refreshMembersTable() {
        membersTableModel.setRowCount(0);
        for (Member member : libraryService.getMembers()) {
            membersTableModel.addRow(new Object[] {
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhone()
            });
        }
    }

    private void refreshLoansTable() {
        loansTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Loan loan : libraryService.getLoans()) {
            loansTableModel.addRow(new Object[] {
                    loan.getId(),
                    loan.getBook().getTitle(),
                    loan.getMember().getName(),
                    loan.getIssueDate().format(formatter),
                    loan.getDueDate().format(formatter),
                    loan.isReturned() ? "Returned" : "Active"
            });
        }
    }

    private void refreshDashboard() {
        List<Book> books = libraryService.getBooks();
        List<Member> members = libraryService.getMembers();
        List<Loan> loans = libraryService.getLoans();

        totalBooksLabel.setText(String.valueOf(books.size()));
        totalMembersLabel.setText(String.valueOf(members.size()));

        long activeLoans = loans.stream().filter(l -> !l.isReturned()).count();
        activeLoansLabel.setText(String.valueOf(activeLoans));

        long availableBooks = books.stream().filter(Book::isAvailable).count();
        availableBooksLabel.setText(String.valueOf(availableBooks));
    }

    private void refreshAllData() {
        refreshBooksTable();
        refreshMembersTable();
        refreshLoansTable();
        refreshDashboard();
    }

    private void applyTheme() {
        Color bg = isDarkMode ? darkBg : lightBg;
        Color fg = isDarkMode ? darkFg : lightFg;
        Color accent = isDarkMode ? darkAccent : lightAccent;
        Color secondary = isDarkMode ? darkSecondary : lightSecondary;

        // Apply to main components
        getContentPane().setBackground(bg);
        tabbedPane.setBackground(bg);
        tabbedPane.setForeground(fg);

        // Apply recursively to all components
        applyThemeToComponent(getContentPane(), bg, fg, accent, secondary);

        repaint();
    }

    private void applyThemeToComponent(Component comp, Color bg, Color fg, Color accent, Color secondary) {
        if (comp instanceof JPanel) {
            comp.setBackground(bg);
            comp.setForeground(fg);
        } else if (comp instanceof JLabel) {
            comp.setForeground(fg);
        } else if (comp instanceof JButton) {
            comp.setBackground(accent);
            comp.setForeground(Color.WHITE);
        } else if (comp instanceof JTable) {
            comp.setBackground(bg);
            comp.setForeground(fg);
            ((JTable) comp).getTableHeader().setBackground(secondary);
            ((JTable) comp).getTableHeader().setForeground(fg);
        } else if (comp instanceof JTextArea) {
            comp.setBackground(secondary);
            comp.setForeground(fg);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyThemeToComponent(child, bg, fg, accent, secondary);
            }
        }
    }
}
