package ink.ptms.cronus.uranus.function;

import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:11
 */
public abstract class Function {

    abstract public String getName();

    abstract public Object eval(Program program, String... args);

    public boolean allowArguments() {
        return true;
    }

}
