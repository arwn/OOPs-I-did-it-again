package swingy;

import swingy.command.*;
import swingy.command.GameCommand;
import swingy.view.GameView;

import java.awt.*;
import java.util.Scanner;


import java.util.HashMap;

public class Game
{
    private GameView view;
    private Hero hero;
    private Board board;
    private boolean gameOver = false;
    private static HashMap<String, GameCommand> commands;
    static {
        commands = new HashMap<String, GameCommand>();
        commands.put("clear", new Clear());
        commands.put("n", new MoveNorth());
        commands.put("s", new MoveSouth());
        commands.put("e", new MoveEast());
        commands.put("w", new MoveWest());
        commands.put("p", new Print());
        commands.put("exit", new Exit());
    }

    public Game(GameView view)
    {
        this.view = view;

        view.init();
        this.hero = makeHero();
        this.board = new Board(this.hero);
        hero.start(this.board);
        mainLoop();
    }

    private void mainLoop()
    {
        while (!gameOver) {
            view.updateBoardData(board, hero);
            view.updateHeroData(hero);
            String input = view.promptUser("Enter a command");
            if (commands.containsKey(input)) {
                commands.get(input).run(this);
            } else {
                view.messageUser("Invalid command: " + input);
            }
        }
    }

    public void movePlayer(Point p)
    {
        this.board.visit(this.hero.position);
        this.hero.position.translate(p.x, p.y);
    }

    private Hero makeHero()
    {
        Scanner s = new Scanner(System.in);

        System.out.printf("Enter a name: ");
        String name = s.nextLine();
        System.out.printf("Enter a profession: ");
        String profession = s.nextLine();
        return new Hero(name, profession);
    }

    public Hero hero()
    {
        return this.hero;
    }

    public Board board()
    {
        return this.board;
    }

    public GameView view()
    {
        return this.view;
    }

    public void restart()
    {
        this.board = new Board(hero);
        hero.start(board);
        hero.experience += this.board.size / 2 * 100;
    }
}
