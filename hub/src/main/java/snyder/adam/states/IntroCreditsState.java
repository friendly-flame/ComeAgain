/*
 * Copyright (c) 2015 Adam Snyder. All rights reserved.
 */

package snyder.adam.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import snyder.adam.Images;
import snyder.adam.Resolution;
import snyder.adam.Soundtrack;


public class IntroCreditsState extends BasicGameState {

    public static final int ID = 2;
    private volatile boolean allLoaded = false;
    private boolean triggerNextState = false;

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        try {
            Images.preload();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        String text = allLoaded ? "Loaded!" : "Loading...";
        Images.text.setSize(5);
        int x = (Resolution.selected.WIDTH - Images.text.getWidth(text))/2;
        int y = (Resolution.selected.HEIGHT - Images.text.getHeight(text))/2;
        Images.text.drawString(x, y, text, Color.white);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if (triggerNextState) {
            stateBasedGame.enterState(StartMenuState.ID);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (allLoaded) {
            triggerNextState = true;
        }
        super.keyPressed(key, c);
    }

    @Override
    public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
        new Thread() {
            public void run() {
                Soundtrack.load();
                Soundtrack.mainTheme.play();
                game.addState(new StartMenuState());
                game.addState(new FooState());
                game.addState(new CellLobbyState());
                // Delay declaring everything loaded by 2 seconds
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    allLoaded = true;
                }
                allLoaded = true;
            }
        }.start();
        super.enter(container, game);
    }
}
