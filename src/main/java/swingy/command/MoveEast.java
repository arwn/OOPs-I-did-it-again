package swingy.command;

import swingy.Game;

import java.awt.*;

public class MoveEast implements GameCommand
{
    @Override
    public void run(Game game)
    {
        if (game.hero().position.x < game.board().size - 1)
            game.movePlayer(new Point(1, 0));
        else
            game.restart();
    }
}

