package swingy;

import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;

import java.awt.*;

public class Hero
{
    public final String name;
    public final String profession;
    protected int experience;
    public Point position = new Point(-1, -1);
    private int healthMod = 0;
    public Armor armor;
    public Weapon weapon;
    public Helm helm;

    protected Hero(final String name, final String profession)
    {
        this.name = name;
        this.profession = profession;
        this.experience = 0x0;
        this.armor = new Armor("bare chest", 1);
        this.weapon = new Weapon("bare fists", 1);
        this.helm = new Helm("bare head", 10);
    }

    protected void takeDamage(int damage)
    {
        int realDamage;
        if(damage - armor.armor <= 0 ) {
            realDamage = 1;
        } else {
            realDamage = damage - armor.armor;
        }
        healthMod -= realDamage;
    }

    protected int calcDamage(int damage)
    {
        int realDamage;
        if(damage - armor.armor <= 0 ) {
            realDamage = 1;
        } else {
            realDamage = damage - armor.armor;
        }
        return realDamage;
    }

    protected boolean alive()
    {
        return helm.health + healthMod > 0;
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
    protected void start(Board b)
    {
        this.position = new Point(b.size / 2, b.size / 2);
    }

    public int health()
    {
        return helm.health + healthMod;
    }
}
