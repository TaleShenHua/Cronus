package ink.ptms.cronus.internal.variable;

import com.google.common.collect.Lists;
import ink.ptms.cronus.util.Strumber;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @Author sky
 * @Since 2019-08-17 18:41
 */
public class VariableExecutor {

    @TInject
    private static TLogger logger;

    public static void update(VariableEngine engine, String key, String symbol, String value) {
        switch (symbol) {
            case "=": {
                if (value == null || value.equalsIgnoreCase("null")) {
                    engine.reset(key);
                } else if (value.equals("[]")) {
                    engine.modify(key, Lists.newArrayList(), VariableType.LIST);
                } else {
                    Strumber stringNumber = new Strumber(value);
                    engine.modify(key, stringNumber.get(), stringNumber.getType().toVariableType());
                }
                break;
            }
            case "+": {
                Strumber stringNumber = new Strumber(value);
                if (!engine.increase(key, stringNumber.get(), stringNumber.getType().toVariableType())) {
                    VariableResult result = engine.select(key);
                    logger.warn("Expression \"" + key + " " + symbol + " " + value + "\" unable to support existing variable: " + result.getValue() + " (" + result.getType() + ")");
                }
                break;
            }
            case "-": {
                Strumber stringNumber = new Strumber(value);
                if (!engine.decrease(key, stringNumber.get(), stringNumber.getType().toVariableType())) {
                    VariableResult result = engine.select(key);
                    logger.warn("Expression \"" + key + " " + symbol + " " + value + "\" unable to support existing variable: " + result.getValue() + " (" + result.getType() + ")");
                }
                break;
            }
            default: {
                logger.warn("Cannot format symbol in \"" + key + " " + symbol + " " + value);
                break;
            }
        }
    }
}
