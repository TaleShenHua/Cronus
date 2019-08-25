package ink.ptms.cronus.internal.task;

import ink.ptms.cronus.internal.QuestTask;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:22
 */
public class TaskCache {

    private Class<? extends QuestTask> task;

    public TaskCache(Class<? extends QuestTask> task) {
        this.task = task;
    }

    public Class<? extends QuestTask> getTask() {
        return task;
    }

    public QuestTask newInstance(ConfigurationSection section) throws Exception  {
        return task.getConstructor(ConfigurationSection.class).newInstance(section);
    }

    @Override
    public String toString() {
        return "TaskCache{" +
                "task=" + task +
                '}';
    }
}
