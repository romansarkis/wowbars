import javax.swing.*;

public class Main {
    public static String VERSION = "v0.1";
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("WoW Bars " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Show the window
        frame.setVisible(true);
    }
}
