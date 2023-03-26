package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.Polygon;

public class Asteroid extends Obj {
    
    private double velX, velY, velRotation;
    int size;
    private double v = 0.5 + 2 * Math.random(); //velocity factor

    //arrays for asteroid shape
    private int[] astX = {-25, -5, 5, 20, 15, 20, 12, 2, -10, -22, -16, -30};
	private int[] astY = {25, 35, 22, 28, 24, -20, -22, -14, -17, -20, -5, 14};

	//	getter
	public int getSize() {
		return size;
	}
	
    //	setter
	public void setSize(int size) {
		this.size = size;
	}

    //	getter
	public double getVelX() {
		return velX;
	}
	
    //	setter
	public void setVelX(int size) {
		velX = (4 - size) * Math.cos((Math.PI) * Math.random()) * v;
	}

    //	getter
	public double getVelY() {
		return velY;
	}
	
    //	setter
	public void setVelY(int size) {
		velY = (4 - size) * Math.sin((Math.PI) * Math.random()) * v;
	}

    // asteroidLevel = 1 small, 2 medium, 3 large
    public Asteroid(AsteroidsGame game, double x, double y, int size) {

        super(game); //calls parent class constructor
        this.x = x;
        this.y = y;
        setSize(size);
        setVelX(size);
        setVelY(size);
        velRotation = 0.01 + 0.05 * Math.random(); //spin speed

        generateShape();
    }
    
    //creates asteroid shape using arrays above and size
    private void generateShape() {

        Polygon asteroidShape = new Polygon();

        for (int i = 0; i < astX.length; i++) {

            double xPoint = (2 * Math.random() + size) * astX[i];
            double yPoint = size * astY[i];

            asteroidShape.addPoint((int)xPoint, (int)yPoint);
        }

        shape = asteroidShape;
    }
    
    @Override
    public void update() {

        //increment point positions
        angle += velRotation;
        x += velX;
        y += velY;
        
        //if asteroid leaves the bounds of the screen, position it at the opposite end of the screen
        if (x < 0){
            x = game.getWidth();
        } else if (x > (game.getWidth() + size)){
            x = 0;
        }
        if (y < 0){
            y = game.getHeight();
        } else if (y > (game.getHeight() + size)){
            y = 0;
        }
    }

    public void hit() {
        //if the asteroid is the largest size, create two medium size asteroids
        if (size == 3) {
            game.add(new Asteroid(game, x, y, size - 1));
            game.add(new Asteroid(game, x, y, size - 1));
        //if the asteroid is medium size, create two small size asteroids
        } else if (size == 2) {
            game.add(new Asteroid(game, x, y, size - 1));
            game.add(new Asteroid(game, x, y, size - 1));
        }
        //destroy current asteroid
        destroyed = true;
    }
    
}
