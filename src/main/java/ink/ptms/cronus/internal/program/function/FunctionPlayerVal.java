package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionPlayerVal extends Function {

    @Override
    public String getName() {
        return "player.val";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            return ((QuestProgram) program).getDataPlayer().getDataGlobal().get(args[0], args.length > 1 ? args[1] : "<Null.Var>");
        }
        return "<Non-Quest>";
    }
}
