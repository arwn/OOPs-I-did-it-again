package swingy.command;

import swingy.Board;
import swingy.Game;
import swingy.Hero;

import java.awt.*;

public class Print implements GameCommand
{
    @Override
    public void run(Game game)
    {
        Board b = game.board();
        Hero h = game.hero();
        StringBuilder builder = new StringBuilder();
        Point p = new Point();

        for (int j = 0; j < b.size; j++) {
            for (int i = 0; i < b.size; i++) {
                p.x = i;
                p.y = j;
                if (h.position.equals(p))
                    builder.append("X");
                else if (b.visited(p))
                    builder.append("-");
                else
                    builder.append("?");
            }
            builder.append("\n");
        }
        game.view().messageUser(builder.toString());
    }

}
