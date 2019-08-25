package ink.ptms.cronus.service.status;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.event.CronusTaskNextEvent;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerAttack;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.util.Sxpression;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.SoundPack;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-07-21 14:40
 */
@Auto
public class Status implements Service, Listener {

    private StatusType type;
    private Map<String, BukkitTask> cancelTask = Maps.newHashMap();
    private Map<String, BossBar> statusBar = Maps.newHashMap();
    private BarColor barColor;
    private BarStyle barStyle;
    private SoundPack sound;

    @Override
    public void init() {
        try {
            type = StatusType.valueOf(Cronus.getConf().getString("Status.type").toUpperCase());
        } catch (Throwable ignored) {
            type = StatusType.NONE;
        }
        if (type == StatusType.BOSSBAR) {
            try {
                barColor = BarColor.valueOf(Cronus.getConf().getString("Status.bossbar.color").toUpperCase());
            } catch (Throwable ignored) {
                barColor = BarColor.BLUE;
            }
            try {
                barStyle = BarStyle.valueOf(Cronus.getConf().getString("Status.bossbar.style").toUpperCase());
            } catch (Throwable ignored) {
                barStyle = BarStyle.SEGMENTED_20;
            }
        }
        sound = new SoundPack(Cronus.getConf().getString("Status.sound"));
        CronusReloadServiceEvent.call(this);
    }

    @Override
    public void cancel() {
        Bukkit.getOnlinePlayers().forEach(this::cancel);
    }

    @EventHandler
    public void e(CronusTaskNextEvent e) {
        Bukkit.getScheduler().runTask(Cronus.getInst(), () -> {
            // 事件未被取消
            if (!e.isCancelled() && !e.getQuestTask().getStatus().equals("<no-status>")) {
                display(e.getPlayer(), e.getDataQuest(), e.getQuestTask());
                // 延迟关闭并注销之前的延迟关闭任务
                Optional.ofNullable(cancelTask.put(e.getPlayer().getName(), Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                    cancel(e.getPlayer());
                    cancelTask.remove(e.getPlayer().getName());
                }, 100))).ifPresent(BukkitTask::cancel);
            }
        });
    }

    @EventHandler
    public void e(PlayerQuitEvent e) {
        cancelTask.remove(e.getPlayer().getName());
    }

    public void display(Player player, DataQuest dataQuest, QuestTask questTask) {
        String parsed = FunctionParser.parseAll(new QuestProgram(player, dataQuest), TLocale.Translate.setColored(questTask.getStatus()));
        switch (type) {
            case BOSSBAR:
                BossBar newBar = Bukkit.createBossBar(parsed, barColor, barStyle);
                // 移除老的血条
                Optional.ofNullable(statusBar.put(player.getName(), newBar)).ifPresent(BossBar::removeAll);
                // 直接计数
                if (questTask instanceof Countable) {
                    newBar.setProgress((double) ((Countable) questTask).getCount(dataQuest) / ((Countable) questTask).getCountMax());
                }
                // 表达式计数
                else if (questTask instanceof Uncountable) {
                    Sxpression total = ((Uncountable) questTask).getTotal();
                    if (total == null) {
                        newBar.setProgress(questTask.isCompleted(dataQuest) ? 1 : 0.5);
                    } else {
                        double totalNumber = total.getNumber().getNumber().doubleValue();
                        double totalCurrent = ((Uncountable) questTask).getTotal(dataQuest);
                        if (totalNumber >= 0) {
                            newBar.setProgress(totalCurrent / totalNumber);
                        } else {
                            newBar.setProgress(totalCurrent > 0 ? 0 : ((totalCurrent * -1) / (totalNumber * -1)));
                        }
                    }
                }
                // 造成伤害
                else if (questTask instanceof TaskPlayerAttack) {
                    newBar.setProgress(((TaskPlayerAttack) questTask).getDamage(dataQuest) / ((TaskPlayerAttack) questTask).getDamage());
                }
                // 承受伤害
                else if (questTask instanceof TaskPlayerDamaged) {
                    newBar.setProgress(((TaskPlayerDamaged) questTask).getDamage(dataQuest) / ((TaskPlayerDamaged) questTask).getDamage());
                }
                // 不可计数
                else {
                    newBar.setProgress(questTask.isCompleted(dataQuest) ? 1 : 0.5);
                }
                newBar.setVisible(true);
                newBar.addPlayer(player);
                break;
            case ACTIONBAR:
                TLocale.Display.sendActionBar(player, parsed);
                break;
            case TITLE:
                TLocale.Display.sendTitle(player, "", parsed, 5, 40, 5);
                break;
        }
        // 提示音效
        sound.play(player);
    }

    public void cancel(Player player) {
        switch (type) {
            case BOSSBAR:
                Optional.ofNullable(statusBar.remove(player.getName())).ifPresent(BossBar::removeAll);
                break;
        }
    }

    public StatusType getType() {
        return type;
    }

    public Map<String, BukkitTask> getCancelTask() {
        return cancelTask;
    }

    public Map<String, BossBar> getStatusBar() {
        return statusBar;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }
}
