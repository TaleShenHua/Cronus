package ink.ptms.cronus.uranus.program.effect;

import ink.ptms.cronus.internal.variable.VariableExecutor;
import ink.ptms.cronus.internal.variable.impl.EngineY;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectVal extends Effect {

    private static YamlConfiguration data = new YamlConfiguration();
    private String name;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "val\\.(?<name>\\S+) (?<symbol>\\S+) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "val.[name] [symbol] [content]";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        value = matcher.group("value");
        symbol = matcher.group("symbol");
    }

    @Override
    public void eval(Program program) {
        VariableExecutor.update(new EngineY(data), name, symbol, FunctionParser.parseAll(program, value));
    }

    @Override
    public String toString() {
        return "EffectVal{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static YamlConfiguration getData() {
        return data;
    }
}
