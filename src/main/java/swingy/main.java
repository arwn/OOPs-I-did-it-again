package swingy;

import swingy.view.ConsoleView;
import swingy.view.SwingView;

public class main
{
    private static void die(String msg)
    {
        System.err.println(msg);
        System.exit(1);
    }

    public static void main(String []args)
    {
        if (args.length != 1) {
            new Game(new ConsoleView());
        } else {
            if (args[0].equals("gui")) {
                new Game(new SwingView());
            } else if (args[0].equals("cli")) {
                new Game(new ConsoleView());
            } else {
                die("Invalid args");
            }
        }
    }
}
