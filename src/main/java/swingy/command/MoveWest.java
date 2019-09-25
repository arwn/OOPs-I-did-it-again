package swingy.command;

import swingy.Game;

import java.awt.*;

public class MoveWest implements GameCommand
{
    @Override
    public void run(Game game)
    {
        if (game.hero().position.x > 0)
            game.movePlayer(new Point(-1, 0));
        else
            game.restart();
    }
}

