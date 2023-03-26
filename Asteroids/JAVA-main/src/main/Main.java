package main;

import java.io.IOException;

import game.AsteroidsGame;
import game.Display;
import game.obj.musicStuff;


public class Main {

    public static void main(String[] args) throws IOException{
        

        AsteroidsGame asteroidsGame = new AsteroidsGame();
        Display display = new Display(asteroidsGame);
        display.start();

        String musicPath = "bgm.wav";
        musicStuff music = new musicStuff();
        music.playMusic(musicPath);
    }

}
