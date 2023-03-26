package game;


import game.AsteroidsGame.State;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;


public class Obj {
    
    public AsteroidsGame game;
    public double x, y;
    public double angle;
    public Shape shape;
    public Color color = Color.GREEN;
    public boolean visible = true;
    public boolean destroyed;
    public AffineTransform transform = new AffineTransform();

    public Obj(AsteroidsGame game) {//constructor
        this.game = game;
    }

    public AffineTransform getTranform() {//method to get the transform
        transform.setToIdentity();
        transform.translate(x, y);
        transform.rotate(angle);
        return transform;
    }
    
    public void update() {// update 5 states
        switch(game.getState()) {
            case INITIALIZING : updateInitializing(); break;
            case TITLE : updateTitle(); break;
            case PLAYING : updatePlaying(); break;
            case HITTED : updateHitted(); break;
            case GAME_OVER : updateGameOver(); break;
        }
    }
    
    public void draw(Graphics2D g) {//draw rotating ship
        if (shape == null || !visible) {
            return;
        }
        AffineTransform previousTransform = g.getTransform();
        g.setColor(color);
        g.translate(x, y);
        g.rotate(angle);
        g.draw(shape);
        g.setTransform(previousTransform);
    }
    
    public void StateChanged(State newState)  {
    }

    public void updateInitializing() {
    }

    public void updateTitle() {
    }

    public void updatePlaying() {
    }

    public void updateHitted() {
    }

    public void updateGameOver() {
    }

}
