package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectTeleport extends Effect {

    private Location location;

    @Override
    public String pattern() {
        return "teleport (?<location>.+)";
    }

    @Override
    public String getExample() {
        return "teleport [location]";
    }

    @Override
    public void match(Matcher matcher) {
        location = BukkitParser.toLocation(matcher.group("location"));
    }

    @Override
    public void eval(Program program) {
        if (program.getSender() instanceof Player) {
            ((Player) program.getSender()).teleport(location.toBukkit());
        }
    }

    @Override
    public String toString() {
        return "EffectTeleport{" +
                "location=" + location +
                '}';
    }
}
