// Main class to launch the Library Management System GUI

public class LibraryManagementSystem {

    public static void main(String[] args) {
        // Launch GUI on Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            LibraryService libraryService = new LibraryService();
            LibraryGUI gui = new LibraryGUI(libraryService);
            gui.setVisible(true);
        });
    }
}
