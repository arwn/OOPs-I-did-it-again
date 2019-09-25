package swingy.command;

import swingy.Game;

import java.awt.*;

public class MoveSouth implements GameCommand
{
    @Override
    public void run(Game game)
    {
        if (game.hero().position.y < game.board().size)
            game.movePlayer(new Point(0, 1));
        else
            game.restart();
    }
}
