package swingy.equipment;

import java.util.Random;

public class Armor
{
    public final int armor;
    public final String name;
    private static String[] names = {
            "Flaming Chest of Sex Appeal",
            "Slime",
            "Little Girl's Dress",
            "Tank Top of Assault",
            "Latex Suit",
            "Chestplate of Parasites",
            "Distinct Lack of Cloth",
    };

    public Armor(final int armor)
    {
        Random r = new Random();
        this.armor = armor + r.nextInt(5);
        this.name = names[r.nextInt(names.length)];
    }

    public Armor(final String name, final int armor)
    {
        Random r = new Random();
        this.armor = armor + r.nextInt(5);
        this.name = name;
    }
}
