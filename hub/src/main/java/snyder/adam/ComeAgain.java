/*
 * Copyright (c) 2015 Adam Snyder. All rights reserved.
 */

package snyder.adam;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import snyder.adam.states.*;

import java.awt.*;

/**
 * @author Adam Snyder
 */
public class ComeAgain extends StateBasedGame {

    /** Screen title */
    private static final String TITLE = "Come With Me, Again";

    /** Frame rate */
    private static final int FPS = 60;

    /** Logic update interval */
    private static final int LOGIC_UPDATE_INTERVAL = FPS;

    /** Debug mode enable */
    public static boolean debug = false;

    /** VSync enable */
    private static final boolean V_SYNC = true;

    public ComeAgain() {
        super(TITLE);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        container.setMaximumLogicUpdateInterval(LOGIC_UPDATE_INTERVAL);
        container.setAlwaysRender(true);
        container.setTargetFrameRate(FPS);
        container.setShowFPS(debug);
        container.setVSync(V_SYNC);

        Images.load();
        Soundtrack.load();

        this.addState(new IntroCreditsState());
        this.addState(new StartMenuState());
        this.addState(new CellLobbyState());
        this.addState(new FooState());
        this.addState(new DialogueState());

        this.enterState(IntroCreditsState.ID);
    }

    public static void main(String[] args) throws SlickException {
        boolean windowed = false;
        boolean isStatic = false;
        for (String a : args) {
            if (a.equalsIgnoreCase("windowed")) windowed = true;
            if (a.equalsIgnoreCase("window")) windowed = true;
            if (a.equalsIgnoreCase("static")) isStatic = true;
            if (a.equalsIgnoreCase("debug")) debug = true;
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int displayWidth = (int) screenSize.getWidth(); // Actual width of physical display
        int displayHeight = (int) screenSize.getHeight(); // Actual height of physical display
        Resolution defaultResolution = windowed ?
                Resolution.getMatchingWindowedResolution(displayWidth, displayHeight) :
                Resolution.getMatchingFullScreenResolution(displayWidth, displayHeight);

        Resolution.select(defaultResolution);
        Game game = isStatic ? new ComeAgain() :
                new ScalableGame(new ComeAgain(), defaultResolution.WIDTH, defaultResolution.HEIGHT);
        AppGameContainer app = new AppGameContainer(game, defaultResolution.WIDTH, defaultResolution.HEIGHT, false);
        if (windowed) {
            app.setDisplayMode(defaultResolution.WIDTH, defaultResolution.HEIGHT, false);
        } else {
            app.setDisplayMode(displayWidth, displayHeight, true);
        }
        app.setForceExit(true);
        app.start();
    }
}
