package ink.ptms.cronus.internal.program;

import com.google.common.collect.Maps;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:16
 */
public abstract class Actionable {

    @TInject
    protected static TLogger logger;
    protected Map<Action, QuestEffect> action = Maps.newHashMap();

    public void load(Action action, List<String> effect) {
        this.action.put(action, new QuestEffect(effect));
    }

    public void eval(QuestProgram program, Action action) {
        Optional.ofNullable(this.action.get(action)).ifPresent(effect -> effect.eval(program.getPlayer(), program.getDataQuest()));
    }

    public boolean hasAction(Action action) {
        return this.action.containsKey(action);
    }
}
