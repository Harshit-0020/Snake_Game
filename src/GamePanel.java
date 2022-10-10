import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;    // How big we want each component of the game to be
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;    // The higher the delay the slower the game is and vice-versa

    final int[] x = new int[GAME_UNITS];    // Holds the x coordinates of body parts of snake
    final int[] y = new int[GAME_UNITS];    // Holds the y coordinates of body parts of snake

    int bodyParts = 6;  // Initial amount of body parts of the snake
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';   // Direction of motion of snake, initially going right
    boolean running;
    Timer timer;
    Random random;


    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        addNewApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        /*paintComponent(g) is an error. This method is called automatically
         when the panel is created. The paintComponent() method can also be called
          explicitly by the repaint() method defined in Component class.
         */
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
            if (running){
                // Creating a grid to get an idea of unit size of each element in the game
                // Turn on the grid by removing the comment
//                for (int i = 0; i < (SCREEN_HEIGHT); i += UNIT_SIZE) {
//                    g.drawLine(0, i, SCREEN_WIDTH, i);
//                    g.drawLine(i, 0, i, SCREEN_HEIGHT);
//                }
                // Draw the apple
                g.setColor(new Color(0xd40a00));
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                // Draw the snake
                // Iterating through all the body parts of the snake to draw them individually
                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(new Color(0x207567));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(0x4E9C81));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
                //Displaying the score at the top
                g.setColor(new Color(0xFFA071));
                g.setFont(new Font("PixelMplus10 Regular", Font.PLAIN, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString(
                        "Score: "+applesEaten,
                        (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,
                        g.getFont().getSize()
                );
            }
            else {
                gameOver(g);
            }
    }

    public void move(){
        // Move the snake
        // this for loop sets (copies) the location of all body parts on the basis of head
        // Wherever the head was before the next body part will be there next
        // and this continues for all body parts
        for (int i = bodyParts; i >0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // this switch case changes the value of location of head,
        // so we only move the head in the given direction
        // and rest of the body parts change accordingly
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void addNewApple(){
        // Generate the coordinates of a new apple, whenever the method is called
        appleX = random.nextInt((int) SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;

    }
    public void checkApple(){
        if ((x[0]==appleX) && (y[0]==appleY)){
            bodyParts++;
            applesEaten++;
            addNewApple();
        }
    }
    public void checkCollisions(){
        //Checking if head collides with any of the body part
        for (int i = bodyParts; i > 0 ; i--) {
            if ((x[0]==x[i]) && (y[0]==y[i])){
                running = false;
            }
        }
        // Check if head touches left  border
        if (x[0]<0)
            running = false;
        // Check if head touches right border
        if (x[0] > SCREEN_WIDTH)
            running = false;
        //Check if head touches top border
        if (y[0] < 0)
            running = false;
        // Check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT)
            running = false;
        if (!running)
            timer.stop();


    }
    public void gameOver(Graphics g){
        //Displaying the score at the top
        g.setColor(new Color(0xFFA071));
        g.setFont(new Font("PixelMplus10 Regular", Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(
                "Score: "+applesEaten,
                (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2,
                g.getFont().getSize()
        );
        //Setting up the game over text
        g.setColor(new Color(0xB19CD8));
        g.setFont(new Font("PixelMplus10 Regular", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2,SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction!='R'){
                        direction = 'L';
                        break;
                    }
                case KeyEvent.VK_RIGHT:
                    if (direction!='L'){
                        direction = 'R';
                        break;
                    }
                case KeyEvent.VK_UP:
                    if (direction!='D'){
                        direction = 'U';
                        break;
                    }
                case KeyEvent.VK_DOWN:
                    if (direction!='U'){
                        direction = 'D';
                        break;
                    }
            }
        }
    }
}
