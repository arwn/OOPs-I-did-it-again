package swingy;

import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;

import java.awt.*;

public class Hero
{
    public final String name;
    public final String profession;
    public int experience;
    public Point position = new Point(-1, -1);
    private final int baseHealth = 10;
    private final int baseBower = 10;
    private final int baseArmor = 10;
    private Armor armor;
    private Weapon weapon;
    private Helm helm;

    public Hero(final String name, final String profession)
    {
        this.name = name;
        this.profession = profession;
        this.experience = 0x0;
        this.armor = new Armor("bare chest", 0);
        this.weapon = new Weapon("bare fists", 0);
        this.helm = new Helm("bare head", 0);
    }

    public int level()
    {
        boolean found = false;
        int lvl = 1;
        do {
            int needed = lvl * 1000 + (int)Math.pow(lvl - 1, 2) * 450;
            if (experience >= needed) {
                lvl++;
            } else {
                found = true;
            }
        } while (!found);
        return lvl;
    }

    // moves hero to starting position
    public void start(Board b)
    {
        this.position = new Point(b.size / 2, b.size / 2);
    }
}
