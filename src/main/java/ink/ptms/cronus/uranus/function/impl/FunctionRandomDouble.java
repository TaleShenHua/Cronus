package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionRandomDouble extends Function {

    @Override
    public String getName() {
        return "random.double";
    }

    @Override
    public Object eval(Program program, String... args) {
        return Numbers.getRandomDouble(NumberConversions.toDouble(args[0]), NumberConversions.toDouble(args[args.length > 1 ? 1 : 0]));
    }
}
