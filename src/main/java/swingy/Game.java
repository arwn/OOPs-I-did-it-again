package swingy;

import swingy.command.*;
import swingy.enemy.Enemy;
import swingy.enemy.EnemyFactory;
import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;
import swingy.view.GameView;
import swingy.view.ViewFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.List;

public class Game
{
    private static Validator validator;
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private GameView view;
    public DB db = new DB(DB._dbURL);
    private Hero hero;
    private Board board;
    private EnemyFactory enemyFactory = new EnemyFactory();
    public boolean gameOver = false;
    private static HashMap<String, GameCommand> commands;

    static {
        commands = new HashMap<String, GameCommand>();
        commands.put("clear", new Clear());
        commands.put("n", new MoveNorth());
        commands.put("s", new MoveSouth());
        commands.put("e", new MoveEast());
        commands.put("w", new MoveWest());
        commands.put("p", new Print());
        commands.put("p", new Print());
        commands.put("gui", new GUI());
        commands.put("cli", new CLI());
        commands.put("save", new Save());
        commands.put("q", new Quit());
    }

    public Game(GameView view)
    {
        this.view = view;

        view.init();
        mainLoop();
    }

    private void mainLoop()
    {
        while (true) {
            this.hero = choseHero();
            this.board = new Board(this.hero);
            hero.moveToStart(this.board);
            view.messageUser("Keybinds: [nsew], [p]rint, clear, gui/cli, save, [q]uit");
            view.messageUser("Travel and kill enemies to gain experience");
            view.messageUser("You gain some health and experience when you descend a level");
            gameOver = false;
            gameLoop();
        }
    }

    private void gameLoop()
    {
        while (!gameOver) {
            gameOver = !hero.alive();
            view.updateBoardData(board, hero);
            view.updateHeroData(hero);
            String input = view.promptUser("Enter a command");
            if (commands.containsKey(input)) {
                commands.get(input).run(this);
            } else {
                view.messageUser("Invalid command: " + input);
            }
        }
        if (!hero.alive()) {
            endGame();
            db.deleteHero(hero);
        }
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
                prompt.append(String.format("'%s' of power %d", w.name, w.power));
                break;
            case 1:
                prompt.append(String.format("'%s' of health %d", h.name, h.health));
                break;
            case 2:
                prompt.append(String.format("'%s' of armor %d", a.name, a.armor));
                break;
        }
        prompt.append(". Do you want to equip it? [y/N]");
        String ans = view.promptUser(prompt.toString()).toLowerCase();
        if (ans.equals("y")) {
            switch (which) {
                case 0:
                    hero.weapon = w;
                    break;
                case 1:
                    hero.helm = h;
                    break;
                case 2:
                    hero.armor = a;
                    break;
            }
        }
    }

    private Hero choseHero()
    {
        String option = null;
        do {
            view.messageUser("Would you like to create a new hero, or load a previous one?");
            option = view.promptUser("Choices: (C)reate new or (S)elect existing");
            option = option.toLowerCase();
            if (option.equals("c") || option.equals("create")) {
                return makeHero();
            } else if (option.equals("s") || option.equals("select")) {
                if (db.listHeroes().size() == 0) {
                    view.messageUser("No heroes saved. Creating instead...");
                    return makeHero();
                }
                Hero hero = loadHero();
                return hero;
            }
            option = null;
        } while (option == null);
        return null;
    }

    private Hero loadHero() {
        List<String> list = db.listHeroes();
        StringBuilder b = new StringBuilder();
        for (String s : list) {
            b.append(s);
            b.append("\n");
        }
        view.messageUser(b.toString());
        String res = view.promptUser("Please select the id of a hero");
        int id = 0;
        try {
            id = Integer.parseInt(res);
        } catch (NumberFormatException e) {
            view.messageUser("Not a valid hero id.");
            return loadHero();
        }
        Hero h = db.loadHero(id);
        if (h == null) {
            view.messageUser("Not a valid hero id.");
            return loadHero();
        }
        return h;
    }

    private Hero makeHero()
    {
        String name = view.promptUser("Enter a name");
        String profession = view.promptUser("enter a profession");

        // validate
        Hero h = new Hero(name, profession);
        Set<ConstraintViolation<Hero>> violationSet = validator.validate(h);
        if (violationSet.size() == 0) {
            return h;
        } else {
            for (ConstraintViolation<Hero> cv : violationSet) {
                view.messageUser(cv.getMessage());
            }
            view.messageUser("Please try again.");
            return makeHero();
        }
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
        hero.heal(r.nextInt(hero.level()));
        this.board = new Board(hero);
        hero.moveToStart(board);
    }

    private void endGame()
    {

        StringBuilder b = new StringBuilder();
        b.append(String.format(
                "You are %s the level %d %s and you are dead\n" +
                        "On your head you wield a %s with a health of %d\n" +
                        "On your person you wield a %s with an armor of %d\n" +
                        "In your hands you wield a %s with a power of %d\n",
                hero.name, hero.level(), hero.profession,
                hero.helm.name, hero.helm.health,
                hero.armor.name, hero.armor.armor,
                hero.weapon.name, hero.weapon.power
        ));
        b.append("You have met your unfortunate demise in a dark corner of " +
                "a forgotten dungeon. Remembered by nobody.\n");
        b.append("To say that you were a failure is an understatement.\n");
        b.append("Maybe the next adventurer will do better.\n");
        b.append(String.format("GAME OVER\nSCORE: %d", hero.experience));
        view.messageUser(b.toString());
    }
}
