package game.obj;


import game.AsteroidsGame;
import game.AsteroidsGame.State;
import game.Obj;
import java.awt.Polygon;


public class Saucer extends Obj {

    //set private variables for the Saucer
    public Ship ship;
    int size;
    private double targetX, targetY, velX, velY;

    //arrays for shape
    private int[] alienX = {2, 1, -1, -2, -4, -2, 2, 4, 2, -2, -4, 4};
	private int[] alienY = {-1, -2, -2, -1, 0, 1, 1, 0, -1, -1, 0, 0};

    private long hideStartTime;
    private long nextShowTime;
    
    private long nextStartShotTime;
    private long nextShotTime;

    //constructor
    public Saucer(AsteroidsGame game, Ship ship) {
        
        super(game);
        this.ship = ship;
        setupNew();
    }
    
    //creates shape of alien ship using arrays above
    private void generateShape() {

        Polygon alienShape = new Polygon();
        
        for (int i = 0; i < alienX.length; i++) {
            int xPoint = getSize() * alienX[i];
            int yPoint = getSize() * alienY[i];

            alienShape.addPoint(xPoint, yPoint);
        }

        shape = alienShape;
    }

    // getter
    public int getSize(){
        return size;
    }

    // setter
    public void setSize(int size){
        this.size = size;
    }


     //do nothing if game is at title screen
     @Override
     public void update() {
         if (game.getState() == State.TITLE) {
             return;
         }
         
         long currentTime = System.currentTimeMillis();
 
         //if alien ship is displayed
         if (visible) {
 
             // distance between alien ship location and target location
             double dist = Math.sqrt((targetX - x) * (targetX - x) + (targetY - y) * (targetY - y));
 
             // set new random target location if distance is less than 50
             if (dist < 50) {
                 targetX = game.getWidth() * Math.random();
                 targetY = game.getHeight() * Math.random();
             }
             
             // set speed
             velX += ((targetX - x) / dist) * 0.1;
             velY += ((targetY - y) / dist) * 0.1;
             
            //  limit speed
             velX = velX > 2 ? 2 : velX < -2 ? -2 : velX;
             velY = velY > 2 ? 2 : velY < -2 ? -2 : velY;
             
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
 
            //  if more time has passed than the random nextShotTime, shoot and reset times
             if (currentTime - nextStartShotTime > nextShotTime) {

                 game.alienShoot(x, y);
                 nextStartShotTime = System.currentTimeMillis();
                 nextShotTime = 5000 + (long) (5000 * Math.random());
             }
         }
         else if (currentTime - hideStartTime > nextShowTime) {
             visible = true;
         }
     }
 
     public void hit() {
         setupNew();
     }
     
     //Initialises alien ship
     private void setupNew() {
 
         //starts alien ship at left hand side, random height
         x = 0;
         y = game.getHeight() * Math.random();
 
         //sets speed of movement
         velX = 2;
         velY = 0;
 
         // set alien ship to move towards a random direction
         targetX = game.getWidth() * Math.random();
         targetY = game.getHeight() * Math.random();
             
         visible = false; //dont display
         setSize(4 + (int) (2 * Math.random())); //random size
 
         generateShape();//set shape
 
         hideStartTime = System.currentTimeMillis(); //time at construction
         nextShowTime = 10000 + (long) (10000 * Math.random()); //random time
         nextStartShotTime = hideStartTime; //time at construction
         nextShotTime = 5000 + (long) (5000 * Math.random()); //random time
     }
 
     //if not displayed and game is playing, create a new ship
     @Override
     public void StateChanged(State newState) {
         if (!visible && newState == State.PLAYING) {
             setupNew();
         }
         else if (newState == State.TITLE) {
             visible = false;
         }
     }
     
 }
 