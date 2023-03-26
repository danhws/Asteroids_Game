package game.obj;

import game.AsteroidsGame;


import game.AsteroidsGame.State;
import game.Keyboard;
import game.Obj;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.Random;

public class Ship extends Obj {

    public double vx, vy;
    public boolean isAccelerating;
    private long no_die_start_time;
    private long hit_start_time;
    private long shot_time;
    public boolean Jumping = false;
    public boolean check_bool = true;

    public Ship(AsteroidsGame game) {
        super(game);
        set_shape();
    }

    private void set_shape() {
        Polygon shipShape = new Polygon();
        shipShape.addPoint(15, 0);
        shipShape.addPoint(-16, -11);
        shipShape.addPoint(-16, 11);
        shape = shipShape;
    }
    /*generate the random coordinates to be sent to be checked if free for hyper space jump */
    public double[] rand_xy() {
    	double[] return_list_xy = new double[2];
    	Random randW = new Random();
    	int low = 20;
    	// width bigger because the x,y point of the ship is at the back
    	int highW = game.getWidth() - 20 ;
    	
    	Random randH = new Random();
    	int highH = game.getHeight() -15 ;
    	
    	return_list_xy[0] = randW.nextInt(highW-low) + low;
    	return_list_xy[1] = randH.nextInt(highH-low) + 30;
    	return return_list_xy;
    }
    /* checks if the space is empty for the hyper space jump */
    public boolean checkEmpty(Obj shipObj, Class OtherObjType) {
    	//MUST CHANGE OBJS TO PUBLIC IN GAME FILE//////////////////////////////////////////////////////////////////////////////////////
        for (Obj obj2 : game.objs) {
            if (shipObj == obj2 || !OtherObjType.isInstance(obj2)
                    || shipObj.shape == null || obj2.shape == null
                    || shipObj.destroyed || obj2.destroyed
                    || !shipObj.visible || !obj2.visible) {
                continue;
            }
            /* doesnt account for when its just about to hit an object */
            Area area1 = new Area(shipObj.shape);
            Area area2 = new Area(obj2.shape);
            area1.intersect(area2);
            if (area1.isEmpty() == true) {
            	this.check_bool = true ;
            }
            else {
            	this.check_bool = false;
            	break;
            }
        }
        if (check_bool == true) {
        	return true;
        }
        else {
        	this.check_bool = true;
        	return false;
        }
    }

    @Override
    public void updatePlaying() {
        long currentTime = System.currentTimeMillis();
        /*acceleration */
        if (isAccelerating = Keyboard.keyDown[KeyEvent.VK_UP]) { 
            vx += 0.1 * Math.cos(angle);
            vy += 0.1 * Math.sin(angle);
        }
        /*turn left or right */
        if (Keyboard.keyDown[KeyEvent.VK_LEFT]) {
            angle -= 0.16;
        } else if (Keyboard.keyDown[KeyEvent.VK_RIGHT]) {
            angle += 0.16;
        }

        boolean shoot_pressed = Keyboard.keyDown[KeyEvent.VK_F];
        if (shoot_pressed && (currentTime - shot_time > 80)) {
            game.shot(x, y, angle);
            shot_time = currentTime;
        }
        /* kind of acceleration but need cooperate with momentum person to get working better. basically the lag from dead start */
        if(vx > 2) {
        		vx =2;
        	} else {
        	}
        if(vy > 2) {
    		vy = 2;
    	} else {
    	}
        x = x + vx;
        y = y + vy;
        
        /* when ship crosses boundaries. Allows for it to cross a small bit */
        if(x < -5) {
    		x = game.getWidth();
    	} 
        else {
    	}
        
        if(x > game.getWidth() + 5) {
    		x = -5;
    	} 
        else {
    	}
        if(y < -5) {
    		y = game.getHeight();
    	} 
        else {
    	}

        if(y > game.getHeight() + 5) {
    		y = -5;
    	} 
        else { 
    	}
  
        /* hyper space jump */
        if (Keyboard.keyDown[KeyEvent.VK_J]) {
        	// to stop the flash on the screen (doesn't really work for some reason)
        	this.visible = false;
        	this.Jumping = false;
        	while (Jumping == false) {
	        	double[] xy_arr = rand_xy();
	        	x = xy_arr[0];
	        	y = xy_arr[1];
	        	boolean checkFreeAst = checkEmpty(this, Asteroid.class);
	        	boolean checkFreeSaucer = checkEmpty(this, Saucer.class);
	        	boolean checkFreeBullet = checkEmpty(this, SaucerShot.class);
	        	
	        	if (checkFreeAst == true && checkFreeSaucer == true && checkFreeBullet == true ) {
	            	//slowing down velocity so have more control when jumping
	        		vx = vx * 0.9;
	            	vy = vy * 0.9;
	            	this.visible = true;
	            	this.Jumping = true;	
	        	}
        	}
                

        }

        if (currentTime - no_die_start_time < 1000) {
            visible = !visible;
        } else {
            visible = true;
            Asteroid hittedAsteroid = (Asteroid) game.checkCollision(this, Asteroid.class);
            if (hittedAsteroid != null) {
                game.showExplosion(x, y);
                game.addScore(AsteroidsGame.ASTEROID_SCORE_TABLE[hittedAsteroid.size]);
                game.hit();
                hittedAsteroid.hit();
                return;
            }

            Saucer hittedSaucer = (Saucer) game.checkCollision(this, Saucer.class);
            if (hittedSaucer != null) {
                game.showExplosion(x, y);
                game.addScore(AsteroidsGame.SAUCER_SCORE_TABLE[hittedSaucer.size]);
                game.hit();
                hittedSaucer.hit();
                return;
            }

            SaucerShot hittedSaucerShot = (SaucerShot) game.checkCollision(this, SaucerShot.class);
            if (hittedSaucerShot != null) {
                game.showExplosion(x, y);
                game.hit();
                hittedSaucerShot.hit();
                return;
            }
        }

        // if all asteroids is destroyed, create again
        if (game.checkAllObjectsDestroyed(Asteroid.class)) {
        	no_die_start_time = System.currentTimeMillis();
            game.createOneAsteroid();
        }
        
    }
    
    /* when ship is hit regenerates for next life if has one left */
    @Override
    public void updateHitted() {
        if (System.currentTimeMillis() - hit_start_time > 3000) {
            game.playNextLife();
        }
    }
    /* alter state of ship  */
    @Override
    public void StateChanged(State newState) {
        if (newState == State.PLAYING) {
            x = game.getWidth() * 0.5 ;
            y = game.getHeight() * 0.5 ;
            vx = 0;
            vy = 0;
            no_die_start_time = System.currentTimeMillis();
            shot_time = no_die_start_time;
        } else if (newState == State.HITTED) {
        	hit_start_time = System.currentTimeMillis();
            visible = false;
        } else if (newState == State.GAME_OVER) {
            visible = false;
        }
    }

}
