package swingy.view;

public class ViewFactory
{

    public static final int SWING_VIEW = 1;
    public static final int CONSOLE_VIEW = 2;

    private ViewFactory()
    {
    }

    public static GameView getView(int view)
    {
        if (view == SWING_VIEW) {
            return new SwingView();
        } else if (view == CONSOLE_VIEW) {
            return new ConsoleView();
        }
        return null;
    }
}
