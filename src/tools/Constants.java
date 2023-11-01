package tools;

import processing.core.PVector;

public final class Constants {
    public static final int SUCCESS = 1;
    public static final int VALID = 1;
    public static final int FAIL = -1;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    public static final int GAMESTATE_WIN = 1;
    public static final int GAMESTATE_LOSE = -1;
    public static final int GAMESTATE_PLAY = 0;

    public static final int PLAYER_RADIUS = 30;

    public static final float MAX_RANGE = 1200;

    public static final double HEADSHOT_MODIFIER = 1.5;

    public static final PVector ORIGIN = new PVector(WIDTH/2, HEIGHT/2);

    private Constants(){}
}
