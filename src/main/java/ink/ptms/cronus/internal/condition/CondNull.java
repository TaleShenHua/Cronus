package ink.ptms.cronus.internal.condition;

import ink.ptms.cronus.database.data.DataQuest;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 13:27
 */
public class CondNull extends Condition {

    @TInject
    private static TLogger logger;
    private String value;

    public CondNull(String value) {
        this.value = value;
    }

    @Override
    public void init(Matcher matcher, String text) {
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        logger.error("Condition \"" + value + "\" not available.");
        return false;
    }

    @Override
    public String toString() {
        return "CondNull{" +
                "value='" + value + '\'' +
                '}';
    }
}
