package swingy.command;

import swingy.Game;
import swingy.view.ViewFactory;

public class GUI implements GameCommand
{
    @Override
    public void run(Game game)
    {
        game.setView(ViewFactory.SWING_VIEW);
    }
}
