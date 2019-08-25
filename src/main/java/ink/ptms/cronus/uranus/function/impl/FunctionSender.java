package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionSender extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "sender";
    }

    @Override
    public Object eval(Program program, String... args) {
        switch (args[0].toLowerCase()) {
            case "name":
                return program.getSender().getName();
            default:
                return "<invalid>";
        }
    }
}
