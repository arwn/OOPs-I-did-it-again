package swingy.view;

import swingy.Board;
import swingy.Hero;

import javax.swing.*;

public class SwingView implements GameView
{
    private JFrame frame;

    public SwingView()
    {
    }

    @Override
    public void init() {
        frame = new JFrame("Swingy");
    }

    @Override
    public String promptUser(String prompt)
    {
        return "Implement me!";
    }

    @Override
    public void messageUser(String msg)
    {

    }

    @Override
    public void updateHeroData(Hero h)
    {
    }

    @Override
    public void updateBoardData(Board b, Hero h)
    {

    }

    @Override
    public void clearScreen()
    {
    }

    @Override
    public void lock()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void show()
    {
    }
}
