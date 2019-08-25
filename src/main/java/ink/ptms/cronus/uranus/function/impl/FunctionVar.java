package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionVar extends Function {

    @Override
    public String getName() {
        return "var";
    }

    @Override
    public Object eval(Program program, String... args) {
        return program.getData().get(args[0], args.length > 1 ? args[1] : "<Null.Var>");
    }
}
