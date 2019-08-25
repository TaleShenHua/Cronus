package ink.ptms.cronus.internal.program.effect;

import ink.ptms.cronus.uranus.program.ProgramLoader;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @Author 坏黑
 * @Since 2019-05-29 13:07
 */
public class EffectParser {

    @TInject
    static TLogger logger;

    public static Effect parse(String in) {
        Effect effect = ProgramLoader.parseEffect(in);
        if (effect == null) {
            logger.error("Effect \"" + in + "\" parsing failed.");
            return new EffectNull(in);
        }
        return effect.copy(in);
    }
}
