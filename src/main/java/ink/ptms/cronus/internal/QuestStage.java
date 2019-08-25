package ink.ptms.cronus.internal;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusInitQuestStageEvent;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.Actionable;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:25
 */
public class QuestStage extends Actionable {

    protected List<QuestTask> task = Lists.newArrayList();
    protected String id;
    protected List<String> content;
    protected List<String> contentCompleted;
    protected List<String> contentGlobal;
    protected Condition conditionRestart;
    protected ConfigurationSection config;

    public QuestStage(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        this.contentGlobal = TLocale.Translate.setColored(config.getStringList("content-global"));
        this.conditionRestart = ConditionParser.fromObject(config.get("restart"));
        this.content = TLocale.Translate.setColored(config.getStringList("content"));
        this.contentCompleted = TLocale.Translate.setColored(config.getStringList("content-completed"));
        CronusInitQuestStageEvent.call(this);
    }

    public void reset(DataQuest quest) {
        task.forEach(t -> t.reset(quest));
    }

    public void complete(DataQuest quest) {
        task.forEach(t -> t.complete(quest));
    }

    public boolean isCompleted(DataQuest quest) {
        return task.stream().allMatch(t -> t.isCompleted(quest));
    }

    public String getId() {
        return id;
    }

    public List<QuestTask> getTask() {
        return task;
    }

    public List<String> getContent() {
        return content;
    }

    public List<String> getContentCompleted() {
        return contentCompleted;
    }

    public List<String> getContentGlobal() {
        return contentGlobal;
    }

    public Condition getConditionRestart() {
        return conditionRestart;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "QuestStage{" +
                "task=" + task +
                ", id='" + id + '\'' +
                ", content=" + content +
                ", contentCompleted=" + contentCompleted +
                ", contentGlobal=" + contentGlobal +
                ", conditionRestart=" + conditionRestart +
                ", config=" + config +
                ", action=" + action +
                '}';
    }
}
