package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.Graphics2D;
import java.awt.Polygon;


/*Mingming Zhang java group*/
public class ShipPropulsion extends Obj {
    //set private variables for the shipPropulsion
    private Ship ship;
    private boolean show;
    private Polygon propulsionShape = new Polygon();
    
    public ShipPropulsion(AsteroidsGame game, Ship ship) {//constructor for shipPropulsion
        super(game);
        this.ship = ship;
        setShape();
    }

    private void setShape() {//set the shape of the shipPropulsion
        propulsionShape.addPoint(-25, 0);
        propulsionShape.addPoint(-14,6);
        propulsionShape.addPoint(-14, -6);
        shape = propulsionShape;
    }
    
    @Override
    public void draw(Graphics2D g) {//draw the shipPropulsion
        if (ship.isAccelerating && show) {
            super.draw(g);
        }
    }

    @Override
    public void update() {//update the shipPropulsion
        x = ship.x;
        y = ship.y;
        angle = ship.angle;
        visible = ship.visible;
        show = !show;
        propulsionShape.xpoints[0] = -25 + (int) (7 * Math.random());
    }

    
    
}
