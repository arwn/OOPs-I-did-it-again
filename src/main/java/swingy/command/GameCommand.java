package swingy.command;

import swingy.Game;

public interface GameCommand
{
    /**
     * I use a custom runnable clone as I need to supply an argument to the method call.
     *
     * @param game
     */
    void run(Game game);
}
