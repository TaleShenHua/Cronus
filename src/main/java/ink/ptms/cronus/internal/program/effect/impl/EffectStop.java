package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectStop extends Effect {

    @Override
    public String pattern() {
        return "^stop";
    }

    @Override
    public String getExample() {
        return "stop";
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            ((QuestProgram) program).getDataPlayer().stopQuest(((QuestProgram) program).getDataQuest().getQuest());
        }
    }
}
