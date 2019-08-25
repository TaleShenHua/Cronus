package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionLocationCheck extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "location.check";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            return BukkitParser.toLocation(args[0]).inSelect(((QuestProgram) program).getPlayer().getLocation());
        }
        return "<Non-Quest>";
    }
}
