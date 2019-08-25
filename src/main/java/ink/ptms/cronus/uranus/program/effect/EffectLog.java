package ink.ptms.cronus.uranus.program.effect;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectLog extends Effect {

    @TInject
    private static TLogger logger;
    private String type;
    private String value;

    @Override
    public String pattern() {
        return "log\\.(?<type>\\S+) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "log.[action] [content]";
    }

    @Override
    public void match(Matcher matcher) {
        type = matcher.group("type");
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        String parsed = TLocale.Translate.setColored(FunctionParser.parseAll(program, value));
        switch (type.toLowerCase()) {
            case "info":
                logger.info(parsed);
                break;
            case "error":
                logger.error(parsed);
                break;
            case "warn":
                logger.warn(parsed);
                break;
            case "fatal":
                logger.fatal(parsed);
                break;
            case "fine":
                logger.fine(parsed);
                break;
            case "verbose":
                logger.verbose(parsed);
                break;
            case "finest":
                logger.finest(parsed);
                break;
            default:
                logger.info(parsed);
                break;
        }
    }

    @Override
    public String toString() {
        return "EffectLog{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
