package swingy;

import java.awt.*;

public class Board
{
    public final int size;
    private boolean visited[][];

    public Board(final Hero hero)
    {
        final int level = hero.level();
        this.size = (level - 1) * 5 + 10 - (level % 2);
        this.visited = new boolean[this.size][this.size];
    }

    public void visit(Point p)
    {
        this.visited[p.y][p.x] = true;
    }

    public boolean visited(Point p)
    {
        return this.visited[p.y][p.x];
    }
}
