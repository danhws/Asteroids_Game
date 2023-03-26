package game;

import game.obj.HUD;
import game.obj.Initializer;
import game.obj.Asteroid;
import game.obj.Saucer;
import game.obj.SaucerShot;
import game.obj.Ship;
import game.obj.ShipPropulsion;
import game.obj.Shot;
import game.obj.Spark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;


public class AsteroidsGame {
//set final variables for the screen size
    public static final String TITLE = "Asteroids Survival";
    public static final int SCREEN_WIDTH = 1200, SCREEN_HEIGHT = 800;

    //set the lists for the objects
    public List<Obj> objs = new ArrayList<Obj>();
    private List<Obj> objsAdd = new ArrayList<Obj>();
    private List<Obj> objsRemove = new ArrayList<Obj>();

//set score table for saucer and asteroid
    public static final int[] ASTEROID_SCORE_TABLE = { 0, 100, 100, 100 };
    public static final int[] SAUCER_SCORE_TABLE = { 0, 1000, 500, 200, 100, 50 };

    //set enum type class of the state of the game
    public static enum State {
        INITIALIZING, TITLE, PLAYING, HITTED, GAME_OVER
    } 

//set private variables for initializing the game
    private State state = State.INITIALIZING;
    private Ship ship;
    public int lives = 3;
    public int score;
    public int hiscore;  

    //constructor for the game
    public AsteroidsGame() { 
        add(new Initializer(this));
        add(new HUD(this));
        add(ship = new Ship(this));
        add(new ShipPropulsion(this, ship));
        add(new Saucer(this, ship));
        createOneAsteroid();
    }

    //getter and setter for the screen size
    public int getWidth() {
        return SCREEN_WIDTH;
    }

    public int getHeight() {
        return SCREEN_HEIGHT;//get the screen size
    }
 
    //getter and setter for the state of the game
    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            for (Obj obj : objs) {
                obj.StateChanged(state); 
            }
        }
    }

    //getter and setter for the lives of the game
    public int getLives() {
        return lives;
    }
    
    // public void addLives(int lives) {//score 10000 add one life
    //     if ( score%10000==0) {
    //         lives +=1;
    //     }
    // }
    
    public int getLevel() {
        return score/700;
    }
    
    //getter and setter for the score 

    public int getScore() {
        return score;
       
    }

    public void addScore(int points) { 
        score += points;
    }

    //getter and setter for the high score
    public int getHiscore() { 
        return hiscore;
    }

    public void updateHiscore() {
        if (score > hiscore) {
            hiscore = score;
        }
        score = 0;
    }

    public void add(Obj obj) {
        objsAdd.add(obj);
    }


    public void createOneAsteroid() { //method to create one asteroid
        int p = (int) (4 * Math.random());
        int x = 0;
        int y = 0;
        if (p == 0) {
            x = 0;
            y = (int) (SCREEN_HEIGHT * Math.random());
        } else if (p == 1) {
            x = SCREEN_WIDTH;
            y = (int) (SCREEN_HEIGHT * Math.random());
        } else if (p == 2) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = 0;
        } else if (p == 3) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = SCREEN_HEIGHT;
        }
        Asteroid asteroid = new Asteroid(this, x, y, 3);
        // while (collides(ship, asteroid)) {
        // asteroid.x = SCREEN_WIDTH * Math.random();
        // asteroid.y = SCREEN_HEIGHT * Math.random();
        // }
        add(asteroid);
    }

    private void removeAllAsteroids() { //method to remove all asteroids
        for (Obj obj : objs) {
            if (obj instanceof Asteroid) {
                obj.destroyed = true;
            }
        }
    }

    public void update() {// upadte all the objs
        for (Obj obj : objs) {
            obj.update();
            if (obj.destroyed) {
                objsRemove.add(obj);
            }
        }
        objs.addAll(objsAdd);
        objsAdd.clear();
        objs.removeAll(objsRemove);
        objsRemove.clear();
    }

    public void draw(Graphics2D g) {//method to draw all the objects
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        for (Obj obj : objs) {
            obj.draw(g);
        }
    }
// final ImageIcon icon = new ImageIcon("background.png");
//     JTextArea text = new JTextArea() 
//     {
//       Image img = icon.getImage();
//       // instance initializer
//       {setOpaque(false);}
//         // paint the background image and scale it to fill the entire space
//       public void paintComponent(Graphics graphics) 
//       {
//         graphics.drawImage(img, 0, 0, this);
//         super.paintComponent(graphics);
//       }
//     };
    
    public Obj checkCollision(Obj o1, Class collidedObjType) {//method to check collision
        for (Obj o2 : objs) {
            if (o1 == o2 || !collidedObjType.isInstance(o2)
                    || o1.shape == null || o2.shape == null
                    || o1.destroyed || o2.destroyed
                    || !o1.visible || !o2.visible) {
                continue;
            }
            Area a1 = new Area(o1.shape);
            Area a2 = new Area(o2.shape);
            a1.transform(o1.getTranform());
            a2.transform(o2.getTranform());
            a1.intersect(a2);
            if (!a1.isEmpty()) {
                return o2;
            }
        }
        return null;
    }

    public boolean collides(Obj o1, Obj o2) {//method to check collision
        Area a1 = new Area(o1.shape);
        Area a2 = new Area(o2.shape);
        a1.transform(o1.getTranform());
        a2.transform(o2.getTranform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    public void shot(double x, double y, double angle) {//add a ship bullet
        objsAdd.add(new Shot(this, x, y, angle));
    }

    public void alienShoot(double x, double y) {//add a alien ship bullet
        double rx = 40 * Math.random() - 20;
        double ry = 40 * Math.random() - 20;
        double dx = (ship.x + rx) - x;
        double dy = (ship.y + ry) - y;
        double angle = Math.atan2(dy, dx);
        objsAdd.add(new SaucerShot(this, x, y, angle));
    }

    public void showExplosion(double x, double y) {//show sparks when explosion happens
        for (int i = 0; i < 30; i++) {
            objsAdd.add(new Spark(this, x, y));
        }
    }

    public boolean checkAllObjectsDestroyed(Class type) {//check if all objects are destroyed
        for (Obj obj : objs) {
            if (type.isInstance(obj)) {
                if (obj.destroyed == false) {
                    return false;
                }
            }
        }
        for (Obj obj : objsAdd) {
            if (type.isInstance(obj)) {
                if (obj.destroyed == false) {
                    return false;
                }
            }
        }
        return true;
    }

    //4 methods for the changing game state
    public void start() {
        removeAllAsteroids();
        createOneAsteroid();
        setState(State.PLAYING);
    }

    public void hit() {
        lives--;
        if (lives <= 0) {
            setState(State.GAME_OVER);
        } else {
            setState(State.HITTED);
        }
    }

    public void playNextLife() {
        setState(State.PLAYING);
    }

    public void backToTitle() {
        removeAllAsteroids();
        createOneAsteroid();
        setState(State.TITLE);
        lives = 3;
    }

    public void addLives(int i) {
    }

}
