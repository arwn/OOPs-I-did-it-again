package swingy.command;

import swingy.Game;

import java.awt.*;

public class MoveNorth implements GameCommand
{
    @Override
    public void run(Game game)
    {
        if (game.hero().position.y > 0)
            game.movePlayer(new Point(0, -1));
        else
            game.restart();
    }
}
