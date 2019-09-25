package swingy;

import swingy.command.*;
import swingy.command.GameCommand;
import swingy.enemy.Enemy;
import swingy.enemy.EnemyFactory;
import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;
import swingy.view.GameView;
import swingy.view.ViewFactory;

import java.awt.*;
import java.util.Random;
import java.util.Scanner;


import java.util.HashMap;

public class Game
{
    private GameView view;
    private Hero hero;
    private Board board;
    private EnemyFactory enemyFactory = new EnemyFactory();
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
        commands.put("gui", new GUI());
        commands.put("exit", new Exit());
    }

    public Game(GameView view)
    {
        this.view = view;

        view.init();
        this.hero = makeHero();
        this.board = new Board(this.hero);
        hero.start(this.board);
        view.messageUser("Keybinds: [nsew], exit, p, clear, gui");
        view.messageUser("Travel and kill enemies to gain experience");
        view.messageUser("You gain some health and experience when you descend a level");
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
            if (hero.alive() == false) {
                gameOver = true;
            }
        }
        endGame();
    }

    public void movePlayer(Point p)
    {
        Random r = new Random();
        if (board.visited(hero.position) == false && r.nextInt(3) == 1) {
            combat();
        }
        this.board.visit(this.hero.position);
        this.hero.position.translate(p.x, p.y);
    }

    private void combat()
    {
        Enemy e = this.enemyFactory.enemy(hero);
        Random r = new Random();
        while (hero.alive()) {
            view.messageUser(String.format("You encounter a %s!", e.name));
            view.messageUser(
                    String.format("The %s will take %d damage and has %d health",
                    e.name, hero.weapon.power, e.health));
            view.messageUser(
                    String.format("%s will take %d damage and has %d health",
                            hero.name, hero.calcDamage(e.power), hero.health()));
            String s = String.format("%s has %d health, [f]ight or [R]un?", e.name, e.health);
            s = view.promptUser(s).toLowerCase();


            if (s.equals("f")) {
                this.hero.takeDamage(e.power);
                if (e.health <= hero.weapon.power) {
                    view.messageUser("The enemy has died");
                    hero.experience += r.nextInt(e.power) * 10;
                    acquireLoot();
                    return;
                } else {
                    view.messageUser("Enemy takes damage");
                }
            } else {
                if (r.nextInt(2) == 1) {
                    view.messageUser("You run away..");
                    break;
                } else {
                    view.messageUser("You fail to flee");
                    hero.takeDamage(e.power);
                }
            }
        }
    }

    private void acquireLoot()
    {
        Random r = new Random();
        StringBuilder prompt = new StringBuilder();
        int which = r.nextInt(3);
        Weapon w = new Weapon(hero.level());
        Helm h = new Helm(hero.level());
        Armor a = new Armor(hero.level());

        prompt.append("You found a ");
        switch (which) {
            case 0:
                prompt.append(String.format("'%s' of power %d" ,w.name, w.power));
                break;
            case 1:
                prompt.append(String.format("'%s' of health %d" ,h.name, h.health));
                break;
            case 2:
                prompt.append(String.format("'%s' of armor %d" ,a.name, a.armor));
                break;
        }
        prompt.append(". Do you want to equip it? [y/N]");
        String ans = view.promptUser(prompt.toString()).toLowerCase();
        if (ans.equals("y")) {
            switch (which) {
                case 0:
                    hero.weapon = w; break;
                case 1:
                    hero.helm = h; break;
                case 2:
                    hero.armor = a; break;
            }
        }
    }

    private Hero makeHero()
    {
        String name = view.promptUser("Enter a name");
        String profession = view.promptUser("enter a profession");
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

    public void setView(int viewid)
    {
        this.view.hide();
        this.view = ViewFactory.getView(viewid);
        view.init();
        view.clearScreen();
    }

    public void restart()
    {
        Random r = new Random();
        view.messageUser("You descend a level lower into the dungeon");
        hero.experience += this.board.size / 2 * 100;
        hero.takeDamage(r.nextInt(hero.level()));
        this.board = new Board(hero);
        hero.start(board);
    }

    private void endGame()
    {
        StringBuilder b = new StringBuilder();
        b.append(String.format(
                "You are %s the level %d %s and you are dead\n" +
                        "On your head you wield a %s with a health of %d\n" +
                        "On your person you wield a %s with an armor of %d\n" +
                        "In your hands you wield a %s with a power of %d\n",
                hero.name, hero.level(), hero.profession, hero.health(),
                hero.helm.name, hero.helm.health,
                hero.armor.name, hero.armor.armor,
                hero.weapon.name, hero.weapon.power
        ));
        b.append("You have met your unfortunate demise in a dark corner of "+
                "a forgotten dungeon. Remembered by nobody.\n");
        b.append("To say that you were a failure is an understatement.\n");
        b.append("Maybe the next adventurer will do better.\n");
        b.append(String.format("GAME OVER\nSCORE: %d", hero.experience));
        view.messageUser(b.toString());
    }
}
