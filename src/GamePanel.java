import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int ballX = 250, ballY = 250;
    private int ballDX = 2, ballDY = 3;
    private int paddleX = 200;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 10;
    private final int BALL_SIZE = 20;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private boolean gameStarted = false;

    public GamePanel() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Bola
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Barra
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, getHeight() - 50, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Mensaje de espera
        if (!gameStarted) {
            g.setColor(Color.YELLOW);
            g.drawString("Pulsa flecha izquierda o derecha para empezar", 180, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Mover barra
        if (leftPressed && paddleX > 0) {
            paddleX -= 5;
            gameStarted = true;
        }
        if (rightPressed && paddleX < getWidth() - PADDLE_WIDTH) {
            paddleX += 5;
            gameStarted = true;
        }

        // esto es una prueba

        // Solo mover la bola si el juego está iniciado
        if (gameStarted) {
            ballX += ballDX;
            ballY += ballDY;

            // Rebote en paredes
            if (ballX <= 0 || ballX >= getWidth() - BALL_SIZE) {
                ballDX *= -1;
            }

            if (ballY <= 0) {
                ballDY *= -1;
            }

            // Rebote en barra
            if (ballY + BALL_SIZE >= getHeight() - 50 &&
                    ballX + BALL_SIZE >= paddleX &&
                    ballX <= paddleX + PADDLE_WIDTH) {
                ballDY *= -1;
            }

            // Si cae al fondo: reinicia bola y espera movimiento
            if (ballY > getHeight()) {
                resetGame();
            }
        }

        repaint();
    }

    private void resetGame() {
        ballX = 250;
        ballY = 250;
        gameStarted = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
}