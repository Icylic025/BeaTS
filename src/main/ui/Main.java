package ui;


/**
 * Start the program in this Main class
 */
public class Main {

    public static void main(String[] args) {
        try {
            MainUI mainUI = new MainUI();
            mainUI.setVisible(true);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

}
