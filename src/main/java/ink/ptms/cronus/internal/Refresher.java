package ink.ptms.cronus.internal;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import io.izzel.taboolib.module.inject.TSchedule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-31 23:03
 */
public class Refresher implements Listener {

    /**
     * 当玩家死亡时进行任务检查
     */
    @EventHandler
    public void e(PlayerDeathEvent e) {
        check(e.getEntity());
    }

    /**
     * 任务检查
     * - 无效任务
     * - 超时任务
     * - 自动放弃任务
     * - 自动重置阶段
     * - 自动重置条目
     */
    @TSchedule(delay = 40, period = 40)
    public static void check() {
        Bukkit.getOnlinePlayers().forEach(Refresher::check);
    }

    public static void check(Player player) {
        DataPlayer playerData = CronusAPI.getData(player);
        for (Map.Entry<String, DataQuest> entry : playerData.getQuest().entrySet()) {
            if (!playerData.isQuestCompleted(entry.getKey())) {
                Quest quest = entry.getValue().getQuest();
                // 无效任务
                if (quest == null) {
                    playerData.getQuest().remove(entry.getKey());
                    playerData.push();
                    continue;
                }
                // 超时任务
                if (quest.getTimeout() != null && quest.getTimeout().isTimeout(entry.getValue())) {
                    playerData.failureQuest(quest);
                    playerData.push();
                    continue;
                }
                // 任务失败
                if (quest.getConditionFailure() != null && quest.getConditionFailure().check(player, entry.getValue())) {
                    playerData.failureQuest(quest);
                    playerData.push();
                    continue;
                }
                // 阶段检查
                QuestStage stage = entry.getValue().getStage();
                if (stage == null) {
                    playerData.getQuest().remove(entry.getKey());
                    playerData.push();
                }
                // 阶段重置
                else if (stage.getConditionRestart() != null && stage.getConditionRestart().check(player, entry.getValue())) {
                    stage.reset(entry.getValue());
                    stage.eval(new QuestProgram(player, entry.getValue()), Action.RESTART);
                    playerData.push();
                }
                // 条目检查
                else {
                    for (QuestTask task : stage.getTask()) {
                        // 条目重置
                        if (task.getConditionRestart() != null && task.getConditionRestart().check(player, entry.getValue())) {
                            task.reset(entry.getValue());
                            task.eval(new QuestProgram(player, entry.getValue()), Action.RESTART);
                            playerData.push();
                        }
                    }
                }
            }
        }
    }

}
