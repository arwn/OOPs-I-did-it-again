package swingy;

import swingy.equipment.Armor;
import swingy.equipment.Helm;
import swingy.equipment.Weapon;
import swingy.view.GameView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB
{
    private static Connection conn;
    public static final String _dbURL = "jdbc:sqlite:"
            + System.getProperty("user.dir")
            + "/swingy.db";

    public DB(String DBURL)
    {
        try {
            conn = DriverManager.getConnection(DBURL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to SQLite driver, aborting...");
        }

        try {

            String sql = "CREATE TABLE IF NOT EXISTS heroes (\n" +
                    "id integer PRIMARY KEY AUTOINCREMENT,\n" +
                    "name text NOT NULL,\n" +
                    "profession text NOT NULL,\n" +
                    "experience integer,\n" +
                    "healthmod integer,\n" +
                    "armorname text,\n" +
                    "armor integer,\n" +
                    "helmname text,\n" +
                    "health integer,\n" +
                    "weaponname text,\n" +
                    "power integer" +
                    ");";
            Statement stmt = conn.createStatement();
            //stmt.execute("DROP TABLE heroes;");
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean heroExists(Hero h)
    {
        boolean ret;
        String sql = "SELECT * FROM heroes WHERE name = ? and profession = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.name);
            stmt.setString(2, h.profession);
            ResultSet res = stmt.executeQuery();
            ret = !res.isClosed();
            res.close();
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveHero(GameView view, Hero h) {
        String sql;
        if (!heroExists(h)) {
            sql = "INSERT INTO heroes (name, profession," +
                    " experience, healthmod, armorname," +
                    " armor, helmname, health, weaponname, power)" +
                    " values (\n" +
                    "    \"%s\",\n" + // name
                    "    \"%s\",\n" + // profession
                    "    %d,\n" + // experince
                    "    %d,\n" + // healthmod
                    "    \"%s\",\n" + // armorname
                    "    %d,\n" + // armor
                    "    \"%s\",\n" + // helmname
                    "    %d,\n" + // health
                    "    \"%s\",\n" + // weaponname
                    "    %d);"; // power
            sql = String.format(sql,
                    h.name, h.profession, h.experience, h.healthMod,
                    h.armor.name, h.armor.armor,
                    h.helm.name, h.helm.health,
                    h.weapon.name, h.weapon.power);
        } else {
            sql = "UPDATE heroes SET \n" +
                    "experience = %d," +
                    "healthmod = %d," +
                    "armorname = \"%s\"," +
                    "armor = %d," +
                    "helmname = \"%s\"," +
                    "health = %d," +
                    "weaponname = \"%s\"," +
                    "power = %d\n " +
                    "where name = \"%s\" and " +
                    "profession = \"%s\"";
            sql = String.format(sql, h.experience, h.healthMod,
                    h.armor.name, h.armor.armor,
                    h.helm.name, h.helm.health,
                    h.weapon.name, h.weapon.power,
                    h.name, h.profession);
        }
        try (Statement stmt = conn.createStatement()) {
            if (heroExists(h)) {
                stmt.executeUpdate(sql);
            } else {
                stmt.execute(sql);
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Hero loadHero(int id)
    {
        try (Statement st = conn.createStatement()) {
            String sql = "SELECT * FROM heroes where id = " + id;
            ResultSet res = st.executeQuery(sql);
            if (res.next()) {
                Hero h = new Hero(
                        res.getString("name"),
                        res.getString("profession"));
                h.experience = res.getInt("experience");
                h.armor = new Armor(
                        res.getString("armorname"),
                        res.getInt("armor"));
                h.helm = new Helm(
                        res.getString("helmname"),
                        res.getInt("health"));
                h.weapon = new Weapon(
                        res.getString("weaponname"),
                        res.getInt("power"));
                res.close();
                return h;
            } else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> listHeroes()
    {
        List<String> list = new ArrayList<String>();
        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM heroes";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                String s = "| ID: %d | Name: %s | Prefession: %s | Experience: %d |";
                s = String.format(s, res.getInt("id"),
                        res.getString("name"),
                        res.getString("profession"),
                        res.getInt("experience"));
                list.add(s);
            }
            res.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void deleteHero(Hero h)
    {
        String sql = "DELETE FROM heroes WHERE name = ? and profession = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, h.name);
            stmt.setString(2, h.profession);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
