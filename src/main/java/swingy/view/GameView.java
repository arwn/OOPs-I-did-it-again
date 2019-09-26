package swingy.view;

import swingy.Board;
import swingy.Hero;

public interface GameView
{
    /**
     * swingy.view.GameView.init() shall prepare any necessary housekeeping before anything else is done.
     * Such as creating a window and widgets, clearing the screen, etc.
     */
    void init();

    /**
     * This will be the basic way to prompt the user for input, will return a string when the user
     * sends data. Blocks until the user sends a message
     *
     * @param prompt
     * @return
     */
    String promptUser(String prompt);

    /**
     * Similar to promptUser, however no response is expected
     *
     * @param msg
     */
    void messageUser(String msg);

    /**
     * Updates the hero display, if there is one. May not be implemented on console
     *
     * @param h
     */
    void updateHeroData(Hero h);

    /**
     * Updates the board display, may not be used every time on console.
     *
     * @param m
     */
    void updateBoardData(Board m, Hero h);

    /**
     * Clears the screen
     */
    void clearScreen();

    /**
     * Locks all elements on screen to prevent future input. Game is over.
     */
    void lock();

    /**
     * Needed to hide when changing windows
     */
    void hide();

    void show();
}
