package ink.ptms.cronus.internal.program.function.hook;

import ch.njol.skript.variables.Variables;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionSkript extends Function {

    @Override
    public String getName() {
        return "skript";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            return Variables.getVariable(args[0], null, false);
        }
        return "<Non-Quest>";
    }
}
