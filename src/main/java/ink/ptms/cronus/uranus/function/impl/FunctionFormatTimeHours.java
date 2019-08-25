package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.util.Times;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionFormatTimeHours extends Function {

    @Override
    public String getName() {
        return "format.time.hours";
    }

    @Override
    public Object eval(Program program, String... args) {
        return new Times(NumberConversions.toLong(args[0])).getHours();
    }
}
