package swingy.command;

import swingy.Game;

public class Exit implements GameCommand
{
    @Override
    public void run(Game game)
    {
        System.exit(0);
    }
}
