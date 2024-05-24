import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameState gameState = new GameState();
            JFrame frame = new JFrame("Solitaire");
            JPanel panel = new AppPanel(gameState);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}