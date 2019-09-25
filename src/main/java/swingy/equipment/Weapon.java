package swingy.equipment;

import java.util.Random;

public class Weapon
{
    public final int power;
    public final String name;
    private static String[] names = {
            "Holy Mackerel",
            "Flaming Demon Fist of Sexual Tension",
            "Baby Face's Blaster",
            "Cow Mangler 5000",
            "Amputator",
            "Frying Pan",
            "Turkey Leg"
    };

    public Weapon(final int power)
    {
        Random r = new Random();
        this.power = power * r.nextInt(5);
        this.name = names[r.nextInt(names.length)];
    }
    public Weapon(String name, final int power)
    {
        Random r = new Random();
        this.power = power * r.nextInt(5);
        this.name = name;
    }
}
