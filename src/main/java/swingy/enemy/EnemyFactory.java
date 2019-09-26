package swingy.enemy;

import swingy.Hero;

import java.util.Random;

public class EnemyFactory
{
    private static String[] names = {
            "Jellyfish",
            "Succubus",
            "Saucy Sorcerer",
            "Princess",
            "Wire Shark",
            "GNU/Gnome",
            "Hipster",
            "Quentin",
    };

    public Enemy enemy(Hero h)
    {
        Random r = new Random();
        String name = names[r.nextInt(names.length)];
        int power = r.nextInt(h.level()) + 1;
        int health = r.nextInt(h.level()) + 1;

        return new Enemy(name, power, health);
    }
}
