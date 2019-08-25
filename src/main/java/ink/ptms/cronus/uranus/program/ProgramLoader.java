package ink.ptms.cronus.uranus.program;

import com.google.common.collect.Lists;
import ink.ptms.cronus.uranus.Uranus;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.exception.ProgramLoadException;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TFunction;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:19
 */
@TFunction(enable = "init")
public class ProgramLoader {

    @TInject
    private static TLogger logger;
    private static List<Effect> effects = Lists.newArrayList();
    private static List<ProgramFile> programFiles = Lists.newArrayList();

    private static void init() {
        TabooLibLoader.getPluginClasses(Uranus.getInst()).ifPresent(classes -> {
            classes.stream().filter(pluginClass -> pluginClass.isAnnotationPresent(Auto.class)).forEach(pluginClass -> {
                if (Effect.class.isAssignableFrom(pluginClass)) {
                    registerEffect(pluginClass);
                }
            });
        });
    }

    public static ProgramFile load(File file) throws ProgramLoadException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ProgramLine programLine = null;
        int conditionIndex;
        for (Object program : yaml.getList("Program", Lists.newArrayList())) {
            if (program instanceof String) {
                Effect effect = parseEffect(program.toString());
                if (effect == null) {
                    throw new ProgramLoadException("Invalid Effect: " + program.toString());
                }
                programLine = append(programLine, program, effect);
            }
        }
        return new ProgramFile(file.getName().split("\\.")[0], yaml, programLine);
    }

    private static Map.Entry<String, Object> getFirst(Object program) throws ProgramLoadException {
        Map<String, Object> map;
        if (program instanceof ConfigurationSection) {
            map = ((ConfigurationSection) program).getValues(false);
        } else if (program instanceof Map) {
            map = (Map) program;
        } else {
            throw new ProgramLoadException("Invalid Program: " + program);
        }
        Map.Entry<String, Object> firstEntry = map.entrySet().stream().findFirst().orElse(null);
        if (firstEntry == null) {
            throw new ProgramLoadException("Invalid Program: " + program);
        }
        return firstEntry;
    }

    public static ProgramLine append(ProgramLine programLine, Object program, Effect effect) {
        ProgramLine index = programLine;
        while (index != null && index.getNext() != null) {
            index = index.getNext();
        }
        if (index == null) {
            programLine = new ProgramLine(effect.copy(program.toString()));
        } else {
            index.setNext(new ProgramLine(effect.copy(program.toString())));
        }
        return programLine;
    }

    public static Effect parseEffect(String in) {
        return effects.stream().filter(effect -> effect.parse(in)).findFirst().orElse(null);
    }

    public static void registerEffect(Class<? extends Effect> effectClass) {
        try {
            Effect effect = effectClass.newInstance();
            effect.init();
            effects.add(effect);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static List<Effect> getEffects() {
        return effects;
    }

    public static List<ProgramFile> getProgramFiles() {
        return programFiles;
    }
}
