package game.obj;

import game.AsteroidsGame;
import game.Obj;
import java.awt.Rectangle;


public class Shot extends Obj {
//set private variables for the Shot
    private long startTime;
    private double vx, vy;
    private static int smallAsteroidHitCount;

    public Shot(AsteroidsGame game, double x, double y, double angle) {//constructor for Shot
        super(game);
        this.x = x;
        this.y = y;
        this.angle = angle;
        shape = new Rectangle(-1, -1, 2, 2);
        vx = 5 * Math.cos(angle);
        vy = 5 * Math.sin(angle);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {//update the Shot
        x += vx;
        y += vy;

        x = x < -1 ? game.getWidth() : x;
        x = x > game.getWidth() + 1 ? -1 : x;
        y = y < -1 ? game.getHeight() : y;
        y = y > game.getHeight() + 1 ? -1 : y;

        Asteroid hittedAsteroid = (Asteroid) game.checkCollision(this, Asteroid.class);
        if (hittedAsteroid != null) {//if hit the asteroid,add score and destroy the asteroid
            game.showExplosion(x, y);
            game.addScore(AsteroidsGame.ASTEROID_SCORE_TABLE[hittedAsteroid.getSize()]);
            game.addLives(3);    
            // new large asteroid is created every time
            // 9 small asteroids are destroyed！！！！！！！！！！
            if (hittedAsteroid.getSize() == 1) {
                smallAsteroidHitCount++;
                if ((smallAsteroidHitCount % 4) == 0) {
                    for (int i = 0; i < (smallAsteroidHitCount /9) ; i++) {
                        game.createOneAsteroid();
                    }
                }

            }

            hittedAsteroid.hit();
        }

        Saucer hittedSaucer = (Saucer) game.checkCollision(this, Saucer.class);//hit saucer add score
        if (hittedSaucer != null) {
            game.showExplosion(x, y);
            game.addScore(AsteroidsGame.SAUCER_SCORE_TABLE[hittedSaucer.getSize()]);
            hittedSaucer.hit();
        }

        destroyed = System.currentTimeMillis() - startTime > 3500 || hittedAsteroid != null || hittedSaucer != null;
    }

}
