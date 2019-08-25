package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionServerVal extends Function {

    @Override
    public String getName() {
        return "server.val";
    }

    @Override
    public Object eval(Program program, String... args) {
        String v = Cronus.getCronusService().getDatabase().getGlobalVariable(args[0]);
        return v == null ? (args.length > 1 ? args[1] : "<Null.Var>") : v;
    }
}
