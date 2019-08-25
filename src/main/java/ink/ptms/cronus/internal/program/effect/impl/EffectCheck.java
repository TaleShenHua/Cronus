package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.internal.program.effect.EffectParser;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectCheck extends Effect {

    private Condition condition;
    private Effect effect1;
    private Effect effect2;

    @Override
    public String pattern() {
        return "check \\[(?<condition>.+?)] if \\[(?<effect1>.+?)] else \\[(?<effect2>.+?)]";
    }

    @Override
    public String getExample() {
        return "check [condition] if [effect1] else [effect2]";
    }

    @Override
    public void match(Matcher matcher) {
        condition = ConditionParser.parse(matcher.group("condition"));
        effect1 = EffectParser.parse(matcher.group("effect1"));
        effect2 = EffectParser.parse(matcher.group("effect2"));
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            if (condition.check(((QuestProgram) program).getPlayer(), ((QuestProgram) program).getDataQuest(), null)) {
                effect1.eval(program);
            } else {
                effect2.eval(program);
            }
        }
    }

    @Override
    public String toString() {
        return "EffectCheck{" +
                "condition=" + condition +
                ", effect1=" + effect1 +
                ", effect2=" + effect2 +
                '}';
    }
}
