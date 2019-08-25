package ink.ptms.cronus.internal;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.event.CronusQuestInitEvent;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.Actionable;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:00
 */
public class Quest extends Actionable {

    protected ConfigurationSection config;
    protected String id;
    protected String label;
    protected String display;
    protected Time timeout;
    protected long cooldown;
    protected Condition conditionAccept;
    protected Condition conditionFailure;
    protected List<String> bookTag;
    protected List<QuestStage> stage = Lists.newArrayList();

    public Quest(ConfigurationSection conf) {
        this.config = conf;
        this.id = conf.getName();
        this.label = conf.getString("label");
        this.display = conf.getString("display", id);
        this.bookTag = conf.getStringList("booktag");
        this.timeout = Time.parse(conf.getString("timeout"));
        this.cooldown = Utils.toTime(conf.getString("cooldown"));
        this.conditionAccept = ConditionParser.fromObject(conf.get("condition.accept"));
        this.conditionFailure = ConditionParser.fromObject(conf.get("condition.failure"));
        CronusQuestInitEvent.call(this);
    }

    @Override
    public void eval(QuestProgram program, Action action) {
        if (hasAction(action)) {
            super.eval(program, action);
            return;
        }
        switch (action) {
            case ACCEPT:
                TLocale.sendTo(program.getPlayer(), "action-quest-accept", display);
                break;
            case ACCEPT_FAIL:
                TLocale.sendTo(program.getPlayer(), "action-quest-accept-fail", display);
                break;
            case SUCCESS:
                TLocale.sendTo(program.getPlayer(), "action-quest-success", display);
                break;
            case FAILURE:
                TLocale.sendTo(program.getPlayer(), "action-quest-failure", display);
                break;
            case COOLDOWN:
                TLocale.sendTo(program.getPlayer(), "action-quest-cooldown", display);
                break;
        }
    }

    public String getFirstStage() {
        return stage.size() > 0 ? stage.get(0).getId() : null;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getBookTag() {
        return bookTag;
    }

    public String getLabel() {
        return label;
    }

    public Time getTimeout() {
        return timeout;
    }

    public long getCooldown() {
        return cooldown;
    }

    public Condition getConditionAccept() {
        return conditionAccept;
    }

    public Condition getConditionFailure() {
        return conditionFailure;
    }

    public List<QuestStage> getStage() {
        return stage;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "config=" + config +
                ", id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", display='" + display + '\'' +
                ", timeout=" + timeout +
                ", cooldown=" + cooldown +
                ", conditionAccept=" + conditionAccept +
                ", conditionFailure=" + conditionFailure +
                ", bookTag=" + bookTag +
                ", stage=" + stage +
                ", action=" + action +
                '}';
    }
}
