package swingy.equipment;

import java.util.Random;

public class Helm
{
    public final int health;
    public final String name;
    private static String[] names = {
            "Distinct Lack of Hair",
            "Demon Horns",
            "Cat Ears",
            "Tentacle Cap of Confusion",
            "Horrific Head of Hare",
            "Shoe of Fleet Feet",
            "Burning Team Captain",
    };

    public Helm(final String name, final int health)
    {
        Random r = new Random();
        this.health = 10 + r.nextInt(health);
        this.name = name;
    }

    public Helm(final int health)
    {
        Random r = new Random();
        this.health = 10 + health * r.nextInt(10);
        this.name = names[r.nextInt(names.length)];
    }
}
