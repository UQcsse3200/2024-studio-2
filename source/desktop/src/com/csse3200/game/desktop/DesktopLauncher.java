package com.csse3200.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.csse3200.game.GdxGame;

/** This is the launch class for the desktop game. Passes control to libGDX to run GdxGame(). */
public class DesktopLauncher {
  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setResizable(false);
    config.setTitle("AttackOnAnimal");
    config.setWindowIcon("images/cow.png");
    new Lwjgl3Application(new GdxGame(), config);
  }
}
