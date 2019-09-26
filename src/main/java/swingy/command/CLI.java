package swingy.command;

import swingy.Game;
import swingy.view.ViewFactory;

public class CLI implements GameCommand
{
    @Override
    public void run(Game game)
    {
        game.setView(ViewFactory.CONSOLE_VIEW);
    }
}
