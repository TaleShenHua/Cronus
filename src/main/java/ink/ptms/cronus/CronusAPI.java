package ink.ptms.cronus;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusTaskNextEvent;
import ink.ptms.cronus.event.CronusTaskSuccessEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.service.globalevent.GlobalEvent;
import ink.ptms.cronus.service.guide.GuideWay;
import ink.ptms.cronus.service.guide.GuideWayData;
import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.book.BookFormatter;
import io.izzel.taboolib.util.book.builder.BookBuilder;
import io.izzel.taboolib.util.book.builder.PageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:09
 */
public class CronusAPI {

    public static DataPlayer getData(Player player) {
        return Cronus.getCronusService().getPlayerData().getOrDefault(player.getName(), new DataPlayer(player));
    }

    public static void guideHandle(Player player) {
        GuideWay service = Cronus.getCronusService().getService(GuideWay.class);
        service.cancel(player);
        DataPlayer playerData = CronusAPI.getData(player);
        playerData.getQuest().values().forEach(dataQuest -> {
            QuestStage questStage = dataQuest.getStage();
            if (questStage == null) {
                return;
            }
            for (QuestTask questTask : questStage.getTask()) {
                // 尚未完成
                if (!questTask.isCompleted(dataQuest)
                        // 有引导
                        && questTask.getGuide() != null
                        // 同世界
                        && questTask.getGuide().getTarget().isSelectWorld(player.getLocation())
                        // 未隐藏
                        && !playerData.getQuestHide().contains(dataQuest.getCurrentQuest())) {
                    // 创建引导
                    service.add(player, GuideWayData.create(player, questTask.getGuide().getTarget().toBukkit(), questTask.getGuide().getText(), questTask.getGuide().getDistance()));
                }
            }
        });
    }

    @SafeVarargs
    public static void stageHandle(Player player, Event event, Class<? extends QuestTask>... tasks) {
        // 主线程判断
        // 因 1.14 新增的事件线程安全监测
        if (Version.isBefore(Version.v1_14) || Bukkit.isPrimaryThread()) {
            stageHandle(player, event, Lists.newArrayList(tasks));
        } else {
            Bukkit.getScheduler().runTask(Cronus.getInst(), () -> stageHandle(player, event, Lists.newArrayList(tasks)));
        }
    }

    /**
     * 进行以该玩家为目标的任务条目判断
     * 注意！1.14+ 版本仅允许在主线程执行该方法
     */
    public static void stageHandle(Player player, Event event, List<Class<? extends QuestTask>> tasks) {
        if (tasks == null || tasks.isEmpty() || event == null) {
            return;
        }
        // 全局事件处理
        Cronus.getCronusService().getService("GlobalEvent", GlobalEvent.class).handle0(player, event, tasks);

        // 任务计算耗能监控部分
        (CronusMirror.isIgnored(event.getClass()) ? CronusMirror.getMirror() : CronusMirror.getMirror("StageHandle:" + event.getEventName())).check(() -> {

            // 玩家数据是否被修改
            // 如果被修改则在计算完成后上传数据
            boolean changed = false;

            // 读取玩家数据
            // 如果玩家数据尚未下载完成则使用虚拟数据进行任务判断
            DataPlayer playerData = CronusAPI.getData(player);

            // 检查所有阶段
            for (DataQuest dataQuest : playerData.getQuest().values()) {

                // 判断任务阶段是否合理存在
                // 如果玩家接受了无效的阶段则跳过判断
                // 性能损耗：从 RegisteredQuest 读取所有已注册任务，从 List 中读取阶段
                // 确保整个过程中无 lambda 表达式以追求更高性能
                QuestStage questStage = dataQuest.getStage();
                if (questStage == null) {
                    continue;
                }
                // 检查所有条目
                for (QuestTask questTask : questStage.getTask()) {

                    // 检查玩家正在进行的条目中是否含有本次计算条目
                    if (tasks.contains(questTask.getClass())

                            // 检查玩家任务条目是否未完成
                            // 性能损耗：yaml 数据读取
                            && !questTask.isCompleted(dataQuest)

                            // 检查任务事件的进行条件
                            // 性能损耗：坐标、物品对比等高耗能计算
                            && questTask.check(player, dataQuest, event)

                            // 检查任务条目的条件是否达成
                            // 性能损耗：脚本、变量等动态编译的高耗能计算
                            && (questTask.getCondition() == null || questTask.getCondition().check(player, dataQuest, event))) {

                        // 唤起单次完成时间并检查是否被取消
                        if (CronusTaskNextEvent.call(player, dataQuest.getQuest(), questStage, questTask).isCancelled()) {
                            continue;
                        }
                        // 条目单次目标完成，执行动作并记录数据
                        questTask.next(player, dataQuest, event);
                        questTask.eval(new QuestProgram(player, dataQuest, event), Action.NEXT);

                        // 检查条目是否已经完成
                        if (questTask.isCompleted(dataQuest)) {

                            // 唤起完成事件并处理任务完成动作
                            CronusTaskSuccessEvent.call(player, dataQuest.getQuest(), questStage, questTask);
                            questTask.eval(new QuestProgram(player, dataQuest, event), Action.SUCCESS);
                            dataQuest.checkAndComplete(player);
                        }
                        changed = true;
                    }
                }
            }
            // 如果数据被修改则更新玩家数据
            if (changed) {
                playerData.push();
            }
        });
    }

    /**
     * 验证任务是否可以正常运行
     */
    public static boolean isValid(Quest quest) {
        // 无任务阶段
        if (quest.getStage().isEmpty()) {
            return false;
        }
        QuestStage stage = quest.getStage().get(0);
        // 无阶段内容
        if (stage.getContent().isEmpty()  && stage.getContentGlobal().isEmpty()) {
            return false;
        }
        // 无阶段条目
        return !stage.getTask().isEmpty();
    }

    /**
     * 打开任务日志
     */
    public static void openQuestLogs(Player player) {
        DataPlayer data = getData(player);
        BookFormatter.forceOpen(player, toBookItem(data.getQuestLogs().isEmpty() ? Lists.newArrayList(TLocale.asString("quest-logs-empty")) : data.getQuestLogs()));
    }

    /**
     * 将多行内容创建为成书
     * 会根据行数自动翻页，但不会自动换行
     */
    public static ItemStack toBookItem(List<String> lines) {
        BookBuilder bookBuilder = BookFormatter.writtenBook();
        PageBuilder builder = null;
        int index = 0;
        for (String line : lines) {
            if (builder == null) {
                builder = new PageBuilder();
            }
            builder.add(line).newLine();
            if (index++ == 13) {
                bookBuilder.addPages(builder.build());
                builder = null;
                index = 0;
            }
        }
        if (builder != null) {
            bookBuilder.addPages(builder.build());
        }
        return bookBuilder.build();
    }
}
