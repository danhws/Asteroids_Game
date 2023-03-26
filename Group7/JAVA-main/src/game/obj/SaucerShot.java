package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.geom.Ellipse2D;

public class SaucerShot extends Obj {
    
    private long startTime;
    private double velX, velY;
    private int size = 1;

    //constructor
    public SaucerShot(AsteroidsGame game, double x, double y, double angle) {

        super(game);
        this.x = x;
        this.y = y;
        this.angle = angle;

        //set new shape and speed of shot
        shape = new Ellipse2D.Double(-3, -3, 6, 6);
        velX = 5 * Math.cos(angle);
        velY = 5 * Math.sin(angle);

        //time at time of construction
        startTime = System.currentTimeMillis();
    }

    //update shot location
    @Override
    public void update() {
        
        x += velX;
        y += velY;

        if (x < -size){
            x = game.getWidth();
        } else if (x > (game.getWidth() + size)){
            x = -size;
        }
        if (y < -size){
            y = game.getHeight();
        } else if (y > (game.getHeight() + size)){
            y = -size;
        }
        
        //destroy if ship is destroyed or more than 2000ms have passed
        destroyed |= System.currentTimeMillis() - startTime > 2000;
    }
    
    //destroy ship if shot hits ship
    public void hit() {
        destroyed = true;
    }
    
}
