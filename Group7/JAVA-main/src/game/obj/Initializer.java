package game.obj;

import game.AsteroidsGame;
import game.AsteroidsGame.State;
import game.Obj;


public class Initializer extends Obj {

    private long startTime;

    public Initializer(AsteroidsGame game) {//constructor for the initializer
        super(game);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void updateInitializing() {//update the initializer state
        if (System.currentTimeMillis() - startTime > 1) {
            game.setState(State.TITLE);
        }
    }

}
