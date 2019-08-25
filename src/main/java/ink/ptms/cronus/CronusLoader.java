package ink.ptms.cronus;

import com.google.common.collect.Lists;
import ink.ptms.cronus.event.CronusReloadEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestBook;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.ConditionCache;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.Actionable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.TaskCache;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-26 23:19
 */
public class CronusLoader {

    @TInject
    private static TLogger logger;
    private File folder;

    void init() {
        TabooLibLoader.getPluginClasses(Cronus.getInst()).ifPresent(classes -> {
            for (Class pClass : classes) {
                // task
                if (pClass.isAnnotationPresent(Task.class)) {
                    Task task = (Task) pClass.getAnnotation(Task.class);
                    Cronus.getCronusService().getRegisteredTask().put(task.name(), new TaskCache(pClass));
                }
                // condition
                else if (pClass.isAnnotationPresent(Cond.class)) {
                    Cond cond = (Cond) pClass.getAnnotation(Cond.class);
                    Cronus.getCronusService().getRegisteredCondition().put(cond.name(), new ConditionCache(cond.pattern(), pClass));
                }
            }
        });
        logger.info(Cronus.getCronusService().getRegisteredCondition().size() + " Condition Registered.");
        logger.info(Cronus.getCronusService().getRegisteredTask().size() + " Task Registered.");
    }

    public void start() {
        long time = System.currentTimeMillis();
        folder = new File(Cronus.getInst().getDataFolder(), "quests");
        if (!folder.exists()){
            Cronus.getInst().saveResource("quests/def.yml", true);
        }
        Cronus.getCronusService().getRegisteredQuest().clear();
        Cronus.getCronusService().getRegisteredQuestBook().clear();
        loadQuest(folder);
        loadQuestBook();
        logger.info(Cronus.getCronusService().getRegisteredQuest().size() + " Quest Loaded. (" + (System.currentTimeMillis() - time + "ms)"));
        CronusReloadEvent.call();
    }

    public void loadQuestBook() {
        ConfigurationSection questBook = Cronus.getConf().getConfigurationSection("QuestBook");
        if (questBook == null) {
            return;
        }
        for (String id : questBook.getKeys(false)) {
            try {
                Cronus.getCronusService().getRegisteredQuestBook().put(id, new QuestBook(questBook.getConfigurationSection(id)));
            } catch (Throwable t) {
                logger.error("QuestBook " + id + " failed to load.");
                t.printStackTrace();
            }
        }
    }

    public void loadQuest(File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(this::loadQuest);
        } else {
            YamlConfiguration yaml = Files.loadYaml(file);
            for (String id : yaml.getKeys(false)) {
                ConfigurationSection questConfig = yaml.getConfigurationSection(id);
                try {
                    Quest quest = new Quest(questConfig);
                    if (questConfig.contains("action")) {
                        try {
                            loadAction(quest, questConfig.getConfigurationSection("action"));
                        } catch (Throwable t) {
                            logger.error("Quest " + id + " failed to load. (Action)");
                            t.printStackTrace();
                            continue;
                        }
                    }
                    if (questConfig.contains("stage")) {
                        try {
                            loadStage(quest, questConfig.getConfigurationSection("stage"));
                        } catch (Throwable t) {
                            logger.error("Quest " + id + " failed to load. (Stage)");
                            t.printStackTrace();
                            continue;
                        }
                    }
                    Cronus.getCronusService().getRegisteredQuest().put(id, quest);
                } catch (Throwable t) {
                    logger.error("Quest " + id + " failed to load.");
                    t.printStackTrace();
                }
            }
        }
    }

    public void loadAction(Actionable actionable, ConfigurationSection actionConfig) {
        for (String type : actionConfig.getKeys(false)) {
            Action actionType = Action.fromName(type);
            if (actionType != null) {
                actionable.load(actionType, actionConfig.isList(type) ? (List) actionConfig.get(type) : Lists.newArrayList(actionConfig.getString(type)));
            }
        }
    }

    public void loadStage(Quest quest, ConfigurationSection stageConfig) {
        for (String id : stageConfig.getKeys(false)) {
            QuestStage questStage = new QuestStage(stageConfig.getConfigurationSection(id));
            if (questStage.getConfig().contains("action")) {
                try {
                    loadAction(questStage, questStage.getConfig().getConfigurationSection("action"));
                } catch (Throwable t) {
                    logger.error("Stage " + id + " failed to load. (Action)");
                    t.printStackTrace();
                    return;
                }
            }
            if (questStage.getConfig().contains("task")) {
                try {
                    loadTask(questStage, questStage.getConfig().getConfigurationSection("task"));
                } catch (Throwable t) {
                    logger.error("Stage " + id + " failed to load. (Task)");
                    t.printStackTrace();
                    return;
                }
            }
            quest.getStage().add(questStage);
        }
    }

    public void loadTask(QuestStage questStage, ConfigurationSection taskConfig) throws Exception {
        for (String id : taskConfig.getKeys(false)) {
            ConfigurationSection config = taskConfig.getConfigurationSection(id);
            TaskCache taskCache = Cronus.getCronusService().getRegisteredTask().get(config.getString("type"));
            if (taskCache == null) {
                logger.error("Task " + id + " failed to load. (Invalid Type:" + config.getString("type") + ")");
                return;
            }
            QuestTask questTask = taskCache.newInstance(config);
            if (questTask.getConfig().contains("action")) {
                try {
                    loadAction(questTask, questTask.getConfig().getConfigurationSection("action"));
                } catch (Throwable t) {
                    logger.error("Task " + id + " failed to load. (Action)");
                    t.printStackTrace();
                    return;
                }
            }
            questStage.getTask().add(questTask);
        }
    }

    public File getFolder() {
        return folder;
    }
}
