package swingy.enemy;

public class Enemy
{
    public final String name;
    public final int power;
    public int health;

    protected Enemy(String name, int power, int health)
    {
        this.name = name;
        this.power = power;
        this.health = health;
    }
}
