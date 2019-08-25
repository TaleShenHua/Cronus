package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.util.lite.Weights;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionRandom extends Function {

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public Object eval(Program program, String... args) {
        Weights collection = new Weights();
        for (String arg : args) {
            String[] split = arg.split("\\^");
            collection.add(split.length > 1 ? NumberConversions.toInt(split[1]) : 1, split[0]);
        }
        return collection.getWeight().getWeightObject();
    }
}
