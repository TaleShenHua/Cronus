package ink.ptms.cronus.uranus.function;

import com.google.common.collect.Lists;
import ink.ptms.cronus.uranus.Uranus;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TFunction;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:12
 */
@TFunction(enable = "init")
public class FunctionLoader {

    private static List<Function> functions = Lists.newArrayList();

    private static void init() {
        TabooLibLoader.getPluginClasses(Uranus.getInst()).ifPresent(classes -> {
            classes.stream().filter(pluginClass -> Function.class.isAssignableFrom(pluginClass) && pluginClass.isAnnotationPresent(Auto.class)).forEach(pluginClass -> {
                try {
                    functions.add((Function) pluginClass.newInstance());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }

    public static Function getFunction(String name) {
        return FunctionLoader.getFunctions().stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void registerFunction(Function function) throws IllegalAccessException {
        if (getFunction(function.getName()) == null) {
            functions.add(function);
        }
        throw new IllegalAccessException("函数已被注册: " + function.getName());
    }

    public static List<Function> getFunctions() {
        return functions;
    }
}
