package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.hook.HookWorldEdit;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectSchematic extends Effect {

    private String name;
    private String symbol;
    private Location location;

    @Override
    public String pattern() {
        return "schematic (?<symbol>-a)?[ ]?(?<name>\\S+) (?<location>.+)";
    }

    @Override
    public String getExample() {
        return "schematic <-a> [name] [location]";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        symbol = matcher.group("symbol");
        location = BukkitParser.toLocation(matcher.group("location"));
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram && location.isBukkit()) {
            HookWorldEdit.pasteSchematic(((QuestProgram) program).getPlayer(), name, location.toBukkit(), symbol != null);
        }
    }

    @Override
    public String toString() {
        return "EffectSchematic{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", location=" + location +
                '}';
    }
}
