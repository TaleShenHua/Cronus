package ink.ptms.cronus.uranus.function.impl;

import com.google.common.collect.Maps;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.util.NumberConversions;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionFormatDate extends Function {

    private Map<String, SimpleDateFormat> formats = Maps.newHashMap();

    @Override
    public String getName() {
        return "format.date";
    }

    @Override
    public Object eval(Program program, String... args) {
        try {
            String format = args.length > 1 ? args[1] : "yyyy-MM-dd HH:mm:ss";
            return formats.computeIfAbsent(format, n -> new SimpleDateFormat(format)).format(NumberConversions.toLong(args[0]));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "<Invalid-Format>";
    }
}
