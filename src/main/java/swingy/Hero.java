package swingy;

import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;

public class Hero
{
    @Size(min = 3, max = 14, message = "must be a message of length 3-14")
    @NotNull
    public final String name;
    @Size(min = 3, max = 14, message = "must be a profession of length 3-14")
    @NotNull
    public final String profession;
    protected int experience;
    public Point position = new Point(-1, -1);
    public int healthMod = 0;
    public Armor armor;
    public Weapon weapon;
    public Helm helm;

    protected Hero(final String name, final String profession)
    {
        this.name = name;
        this.profession = profession;
        this.experience = 0;
        this.armor = new Armor("bare chest", 1);
        this.weapon = new Weapon("bare fists", 1);
        this.helm = new Helm("bare head", 10);
    }

    protected void takeDamage(int damage)
    {
        healthMod -= calcDamage(damage);
    }

    protected void heal(int health)
    {
        healthMod += health;
    }

    protected int calcDamage(int damage)
    {
        int realDamage = damage - armor.armor;
        if (realDamage <= 0) {
            realDamage = 1;
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
            int needed = lvl * 1000 + (int) Math.pow(lvl - 1, 2) * 450;
            if (experience >= needed) {
                lvl++;
            } else {
                found = true;
            }
        } while (!found);
        return lvl;
    }

    // moves hero to starting position
    protected void moveToStart(Board b)
    {
        this.position = new Point(b.size / 2, b.size / 2);
    }

    public int health()
    {
        return helm.health + healthMod;
    }
}
