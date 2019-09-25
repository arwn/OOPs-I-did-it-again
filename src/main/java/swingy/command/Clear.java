package swingy.command;

import swingy.Game;

public class Clear implements GameCommand
{
    @Override
    public void run(Game game)
    {
        game.view().clearScreen();
    }
}
