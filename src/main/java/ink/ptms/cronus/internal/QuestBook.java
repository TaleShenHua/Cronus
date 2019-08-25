package ink.ptms.cronus.internal;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.event.CronusInitQuestBookEvent;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.book.BookFormatter;
import io.izzel.taboolib.util.book.builder.BookBuilder;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author 坏黑
 * @Since 2019-06-09 17:54
 */
public class QuestBook {

    @TInject
    private static TLogger logger;
    private ConfigurationSection config;
    private String id;
    private String display;
    private Map<String, Integer> list = Maps.newHashMap();

    public QuestBook(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        this.display = config.getString("display", id);
        ConfigurationSection list = config.getConfigurationSection("list");
        if (list != null) {
            list.getKeys(false).forEach(keyword -> this.list.put(keyword, list.getInt(keyword)));
        }
        CronusInitQuestBookEvent.call(this);
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public Map<String, Integer> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "QuestBook{" +
                "config=" + config +
                ", id='" + id + '\'' +
                ", display=" + display +
                ", list=" + list +
                '}';
    }
}
