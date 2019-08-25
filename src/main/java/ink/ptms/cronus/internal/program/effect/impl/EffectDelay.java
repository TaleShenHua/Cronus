package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectDelay extends Effect {

    private String value;

    @Override
    public String pattern() {
        return "(delay|wait) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "delay [number]";
    }

    @Override
    public void match(Matcher matcher) {
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            ((QuestProgram) program).setDelay(((QuestProgram) program).getDelay() + NumberConversions.toInt(FunctionParser.parseAll(program, value)));
        }
    }

    @Override
    public String toString() {
        return "EffectDelay{" +
                "value='" + value + '\'' +
                '}';
    }
}
