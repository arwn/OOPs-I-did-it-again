package swingy.command;

        import swingy.Game;

public class Quit implements GameCommand
{
    @Override
    public void run(Game game)
    {
        game.gameOver = true;
    }
}
