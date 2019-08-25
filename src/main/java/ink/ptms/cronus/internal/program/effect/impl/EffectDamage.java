package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectDamage extends Effect {

    private double count;

    @Override
    public String pattern() {
        return "damage (?<count>.+)";
    }

    @Override
    public String getExample() {
        return "damage [number]";
    }

    @Override
    public void match(Matcher matcher) {
        count = NumberConversions.toDouble(matcher.group("count"));
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            ((QuestProgram) program).getPlayer().damage(count);
        }
    }

    @Override
    public String toString() {
        return "EffectDamage{" +
                "count=" + count +
                '}';
    }
}
