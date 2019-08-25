package ink.ptms.cronus.service.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.event.CronusTaskNextEvent;
import ink.ptms.cronus.event.CronusVisibleToggleEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Numbers;
import io.izzel.taboolib.util.lite.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-07-01 16:55
 */
@Auto
public class Scoreboard implements Service, Listener {

    private BukkitTask scoreboardTask;
    private List<String> content;
    private List<String> format;
    private Map<String, Integer> list = Maps.newHashMap();

    @Override
    public void init() {
        content = Cronus.getConf().getStringListColored("Scoreboard.content");
        format = Cronus.getConf().getStringListColored("Scoreboard.format");
        ConfigurationSection list = Cronus.getConf().getConfigurationSection("Scoreboard.list");
        if (list != null) {
            list.getKeys(false).forEach(keyword -> this.list.put(keyword, list.getInt(keyword)));
        }
        scoreboardTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Cronus.getInst(), () -> {
            if (isEnabled()) {
                Bukkit.getOnlinePlayers().forEach(this::update);
            }
        }, 0, 40);
        CronusReloadServiceEvent.call(this);
    }

    @Override
    public void cancel() {
        if (scoreboardTask != null) {
            scoreboardTask.cancel();
        }
    }

    @EventHandler
    public void e(CronusTaskNextEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusVisibleToggleEvent e) {
        update(e.getPlayer());
    }

    public void update(Player player) {
        List<String> build = build(player);
        // 空行检查
        for (int i = 0; i < build.size(); i++) {
            if (build.get(i).isEmpty()) {
                build.set(i, Arrays.stream(String.valueOf(Numbers.getRandomInteger(1000, 9999)).split("")).map(s -> "§" + s).collect(Collectors.joining()));
            }
        }
        // 返回主线程发送计分板
        Bukkit.getScheduler().runTask(Cronus.getInst(), () -> Scoreboards.display(player, build.toArray(new String[0])));
    }

    public List<String> build(Player player) {
        DataPlayer data = CronusAPI.getData(player);
        List<DataQuest> quests = data.getQuest().values().stream()
                .filter(f -> {
                    Quest quest = f.getQuest();
                    return quest != null && quest.getBookTag().stream().anyMatch(list::containsKey) && !data.getQuestHide().contains(quest.getId()) && !data.isQuestCompleted(quest.getId());
                })
                .sorted((b, a) -> Integer.compare(a.getQuest().getBookTag().stream().filter(list::containsKey).map(list::get).findFirst().orElse(0), b.getQuest().getBookTag().stream().filter(list::containsKey).map(list::get).findFirst().orElse(0)))
                .collect(Collectors.toList());
        // 没有任务则返回空集合
        if (quests.isEmpty()) {
            return Lists.newArrayList();
        }
        List<String> content = Lists.newArrayList();
        for (String line : this.content) {
            if (line.equalsIgnoreCase("{format}")) {
                int max = getMaximum();
                List<String> format = quests.stream().flatMap(dataQuest -> buildFormat(player, dataQuest).stream()).collect(Collectors.toList());
                while (format.size() > max) {
                    format.remove(format.size() - 1);
                }
                content.addAll(format);
            } else {
                content.add(line);
            }
        }
        return content;
    }

    public List<String> buildFormat(Player player, DataQuest quest) {
        List<String> format = Lists.newArrayList();
        for (String line : this.format) {
            if (line.equalsIgnoreCase("{display}")) {
                format.add(FunctionParser.parseAll(new QuestProgram(player, quest),  quest.getQuest().getDisplay()));
            } else if (line.equalsIgnoreCase("{content}")) {
                QuestStage questStage = quest.getStage();
                if (questStage == null) {
                    continue;
                }
                for (String content : questStage.getContentGlobal()) {
                    StringBuilder builder = new StringBuilder();
                    for (Variables.Variable variable : new Variables(FunctionParser.parseAll(new QuestProgram(player, quest), content)).find().getVariableList()) {
                        if (variable.isVariable()) {
                            builder.append("<" + variable.getText() + ">");
                        } else {
                            builder.append(variable.getText());
                        }
                    }
                    format.add(builder.toString());
                }
            } else {
                format.add(line);
            }
        }
        return format;
    }

    public int getMaximum() {
        return 15 - (int) content.stream().filter(c -> !c.equalsIgnoreCase("{format}")).count();
    }

    public boolean isEnabled() {
        return Cronus.getConf().getBoolean("Scoreboard.enable");
    }
}
