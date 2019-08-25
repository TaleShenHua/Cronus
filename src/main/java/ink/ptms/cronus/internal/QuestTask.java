package ink.ptms.cronus.internal;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusInitQuestTaskEvent;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.Actionable;
import ink.ptms.cronus.service.guide.GuideWayCache;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:24
 */
public abstract class QuestTask<E extends Event> extends Actionable {

    protected String id;
    protected ConfigurationSection config;
    protected Condition condition;
    protected Condition conditionRestart;
    protected GuideWayCache guide;
    protected String status;

    public QuestTask(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        this.condition = ConditionParser.fromObject(config.get("condition"));
        this.conditionRestart = ConditionParser.fromObject(config.get("restart"));
        this.status = TLocale.Translate.setColored(config.getString("status", "<no-status>"));
        if (config.contains("data")) {
            init(config.getConfigurationSection("data").getValues(false));
        }
        if (config.contains("guide")) {
            Location target = BukkitParser.toLocation(config.getString("guide.target"));
            if (target.isBukkit()) {
                guide = new GuideWayCache(config.getDouble("guide.distance", 2), target, TLocale.Translate.setColored(config.getStringList("guide.text")));
            } else {
                logger.error("Guide Target \"" + config.getString("guide.target") + "\" parsing failed.");
            }
        }
        CronusInitQuestTaskEvent.call(this);
    }

    abstract public void init(Map<String, Object> data);

    abstract public boolean isCompleted(DataQuest dataQuest);

    abstract public boolean check(Player player, DataQuest dataQuest, E event);

    abstract public void next(Player player, DataQuest dataQuest, E event);

    public boolean check(Player player, E event) {
        try {
            return check(player, new DataQuest(), event);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    public void complete(DataQuest dataQuest) {
    }

    public void reset(DataQuest dataQuest) {
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public String getId() {
        return id;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public Condition getCondition() {
        return condition;
    }

    public Condition getConditionRestart() {
        return conditionRestart;
    }

    public GuideWayCache getGuide() {
        return guide;
    }

    public String getStatus() {
        return status;
    }
}
