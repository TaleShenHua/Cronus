package ink.ptms.cronus.internal.metrics;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.DatabaseType;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.service.guide.GuideWay;
import ink.ptms.cronus.service.scoreboard.Scoreboard;
import ink.ptms.cronus.service.status.Status;
import io.izzel.taboolib.module.inject.TSchedule;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-07-25 11:58
 */
public class MetricsLoader {

    private static Metrics metrics;

    @TSchedule
    static void run() {
        metrics = new Metrics(Cronus.getInst());
        // 引导模式
        metrics.addCustomChart(new Metrics.SimplePie("guide_mode", () -> Cronus.getCronusService().getService(GuideWay.class).isPerformance() ? "Performance" : "Visual"));
        // 数据库模式
        metrics.addCustomChart(new Metrics.SimplePie("database", () -> Cronus.getCronusService().getDatabaseType() == DatabaseType.SQL ? "SQL" : "YAML"));
        // 计分板模式
        metrics.addCustomChart(new Metrics.SimplePie("scoreboard", () -> Cronus.getCronusService().getService(Scoreboard.class).isEnabled() ? "Enabled" : "Disabled"));
        // 任务状态
        metrics.addCustomChart(new Metrics.SimplePie("status", () -> {
            switch (Cronus.getCronusService().getService(Status.class).getType()) {
                case ACTIONBAR:
                    return "ActionBar";
                case BOSSBAR:
                    return "BossBar";
                case TITLE:
                    return "Title";
                default:
                    return "None";
            }
        }));
        // 统计服务器所使用的条目类型
        metrics.addCustomChart(new Metrics.AdvancedPie("server_using_tasks", () -> {
            Map<String, Integer> map = Maps.newHashMap();
            // 获取所有任务
            for (Quest quest : Cronus.getCronusService().getRegisteredQuest().values()) {
                // 获取所有阶段
                for (QuestStage questStage : quest.getStage()) {
                    // 获取所有条目
                    for (QuestTask questTask : questStage.getTask()) {
                        Task task = questTask.getClass().getAnnotation(Task.class);
                        if (task != null) {
                            map.put(task.name(), map.getOrDefault(task.name(), 0) + 1);
                        }
                    }
                }
            }
            return map;
        }));
    }

}
