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
        builder.append(String.format(
                "You are %s the level %d %s with a total health of %d\n" +
                        "On your head you wield a %s with a health of %d\n" +
                        "On your person you wield a %s with an armor of %d\n" +
                        "In your hands you wield a %s with a power of %d\n",
                h.name, h.level(), h.profession, h.health(),
                h.helm.name, h.helm.health,
                h.armor.name, h.armor.armor,
                h.weapon.name, h.weapon.power
        ));
        game.view().messageUser(builder.toString());
    }

}
