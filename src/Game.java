import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends JFrame implements KeyListener {

    //window vars
    private final int MAX_FPS; //maximum refresh rate
    private final int WIDTH; //window width
    private final int HEIGHT; //window height

    //double buffer strategy
    private BufferStrategy strategy;
    private ArrayList<Integer> keys = new ArrayList<>();

    //loop variables
    private boolean isRunning = true; //is the window running
    private long rest = 0; //how long to sleep the main thread

    //timing variables
    private float dt; //delta time
    private long lastFrame; //time since last frame
    private long startFrame; //time since start of frame
    private int fps; //current fps

    private Vector p; //position
    private Vector v; //velocity
    private Vector a; //acceleration

    private Vector sz; //size

    private exampleclass myclass;


    public Game(int width, int height, int fps){
        super("My Game");
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    /*
     * init()
     * initializes all variables needed before the window opens and refreshes
     */
    void init(){
        //initializes window size
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);

        //set jframe visible
        setVisible(true);

        //set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create double buffer strategy
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        addKeyListener(this);
        setFocusable(true);

        //set initial lastFrame var
        lastFrame = System.currentTimeMillis();

        p = new Vector(WIDTH/2, HEIGHT/2);
        v = new Vector(500, 500);
        a = new Vector(0, 0);

        sz = new Vector(100, 100);

        myclass = new exampleclass(40, 50);

        //set background window color
        setBackground(Color.BLACK);
    }

    /*
     * update()
     * updates all relevant game variables before the frame draws
     */
    private void update(){
        //update current fps
        fps = (int)(1f/dt);

        handleKeys();

        //update game logic here
        v.add(Vector.mult(a, dt));

        //if(p.x < 0) p.setX(p.x + 5);
        //if(p.x + sz.x > WIDTH) p.setX(p.x - 5);

        if(p.x < 0 || p.x + sz.x > WIDTH) {
            v.setX(-v.x);
            p.add(Vector.mult(v, dt));
        }

        //if(p.y < 0) p.setY(p.y + 5);
        //if(p.y + sz.y > HEIGHT) p.setY(p.y - 5);

        if(p.y < 0 || p.y + sz.y > HEIGHT) {
            v.setY(-v.y);
            p.add(Vector.mult(v, dt));
        }

        p.add(Vector.mult(v, dt));
    }

    /*
     * draw()
     * gets the canvas (Graphics2D) and draws all elements
     * disposes canvas and then flips the buffer
     */
    private void draw(){
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.clearRect(0,0,WIDTH, HEIGHT);

        g.setColor(new Color(0, 209, 255));
        g.fillRect(p.ix, p.iy, sz.ix, sz.iy);

        myclass.draw(g);
        myclass.x++;

        //draw fps
        g.setColor(Color.GREEN);
        g.drawString(Long.toString(fps), 10, 40);

        //release resources, show the buffer
        g.dispose();
        strategy.show();
    }

    private void handleKeys(){
        for(int i = 0; i < keys.size(); i++){
            switch(keys.get(i)){
                case KeyEvent.VK_UP:
                    //p.setY(p.y - 5);
                    a = new Vector(0, -5000);
                    break;
                case KeyEvent.VK_DOWN:
                    //p.setY(p.y + 5);
                    a = new Vector(0, 5000);
                    break;
                case KeyEvent.VK_LEFT:
                    a = new Vector(-5000, 0);
                    //p.setX(p.x - 5);
                    break;
                case KeyEvent.VK_RIGHT:
                    a = new Vector(5000, 0);
                    //p.setX(p.x + 5);
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(!keys.contains(keyEvent.getKeyCode()))
            keys.add(keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for(int i = keys.size() - 1; i >= 0; i--)
            if(keys.get(i) == keyEvent.getKeyCode())
                keys.remove(i);
    }

    /*
         * run()
         * calls init() to initialize variables
         * loops using isRunning
            * updates all timing variables and then calls update() and draw()
            * dynamically sleeps the main thread to maintain a framerate close to target fps
         */
    public void run(){
        init();

        while(isRunning){

            //new loop, clock the start
            startFrame = System.currentTimeMillis();

            //calculate delta time
            dt = (float)(startFrame - lastFrame)/1000;

            //update lastFrame for next dt
            lastFrame = startFrame;

            //call update and draw methods
            update();
            draw();

            //dynamic thread sleep, only sleep the time we need to cap the framerate
            //rest = (max fps sleep time) - (time it took to execute this frame)
            rest = (1000/MAX_FPS) - (System.currentTimeMillis() - startFrame);
            if(rest > 0){ //if we stayed within frame "budget", sleep away the rest of it
                try{ Thread.sleep(rest); }
                catch (InterruptedException e){ e.printStackTrace(); }
            }
        }

    }

    //entry point for application
    public static void main(String[] args){
        Game game = new Game(800, 600, 60);
        game.run();
    }

}
