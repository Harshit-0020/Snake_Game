import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.setResizable(false);
        this.setTitle("Snake Game");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
