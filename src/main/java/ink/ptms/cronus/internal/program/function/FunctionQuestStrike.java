package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionQuestStrike extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "quest.strike";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            QuestStage stage = ((QuestProgram) program).getDataQuest().getStage();
            if (stage == null) {
                return "<No-State>";
            }
            QuestTask task = stage.getTask().stream().filter(t -> t.getId().equals(args[0])).findFirst().orElse(null);
            if (task == null) {
                return "<No-Task>";
            }
            return task.isCompleted(((QuestProgram) program).getDataQuest()) ? "§m" : "";
        }
        return "<Non-Quest>";
    }
}
