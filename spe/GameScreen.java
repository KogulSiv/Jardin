import java.awt.*;
import javax.swing.*;


public class GameScreen extends JFrame {
    private Game game;
    private GamePanel panel;

    public GameScreen(Game game) {
        this.game = game;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Jardin");

        panel = new GamePanel(game);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void display() {
        this.setVisible(true);
        panel.startGameThread();
    }
}