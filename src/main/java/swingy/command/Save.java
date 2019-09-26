package swingy.command;

import swingy.Game;

public class Save implements GameCommand
{
    @Override
    public void run(Game game)
    {
        game.db.saveHero(game.view(), game.hero());
    }
}
