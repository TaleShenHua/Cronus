package ink.ptms.cronus.command.impl;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusVisibleToggleEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.service.dialog.Dialog;
import ink.ptms.cronus.service.dialog.DialogGroup;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.command.base.*;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.book.BookFormatter;
import io.izzel.taboolib.util.book.builder.BookBuilder;
import io.izzel.taboolib.util.book.builder.PageBuilder;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-29 17:25
 */
@BaseCommand(name = "Cronus", aliases = {"CronusQuest", "cQuest", "cq"}, permission = "*")
public class Command extends CronusCommand {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<String> getQuests() {
        List<String> label = Lists.newArrayList();
        for (Quest quest : Cronus.getCronusService().getRegisteredQuest().values()) {
            label.add(quest.getId());
            label.add(quest.getLabel());
        }
        return label.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SubCommand
    BaseSubCommand info = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家")
            };
        }

        @Override
        public String getDescription() {
            return "查看玩家任务信息.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            normal(sender, "玩家 &f" + player.getName() + " &7的任务信息:");
            normal(sender, "全局数据:");
            for (String line : Utils.NonNull(playerData.getDataGlobal().saveToString()).split("\n")) {
                normal(sender, "§f" + line);
            }
            normal(sender, "临时数据:");
            for (String line : Utils.NonNull(playerData.getDataTemp().saveToString()).split("\n")) {
                normal(sender, "§f" + line);
            }
            normal(sender, "任务数据:");
            for (Map.Entry<String, DataQuest> questEntry : playerData.getQuest().entrySet()) {
                normal(sender, "  §f" + questEntry.getKey() + ":");
                normal(sender, "    任务数据:");
                for (String line : Utils.NonNull(questEntry.getValue().getDataQuest().saveToString()).split("\n")) {
                    normal(sender, "    §f" + line);
                }
                normal(sender, "    阶段数据:");
                for (String line : Utils.NonNull(questEntry.getValue().getDataStage().saveToString()).split("\n")) {
                    normal(sender, "    §f" + line);
                }
                normal(sender, "    当前阶段: §f" + questEntry.getValue().getCurrentStage());
                normal(sender, "    开始时间: §f" + dateFormat.format(questEntry.getValue().getTimeStart()));
                normal(sender, "    结束时间: §f" + (playerData.isQuestCompleted(questEntry.getKey()) ? dateFormat.format(playerData.getQuestCompleted().get(questEntry.getKey())) : "-"));
            }
            if (playerData.getQuest().isEmpty()) {
                normal(sender, "§f-");
            }
            normal(sender, "完成任务:");
            for (Map.Entry<String, Long> entry : playerData.getQuestCompleted().entrySet()) {
                normal(sender, "  " + entry.getKey() + ": §f" + (entry.getValue() == 0 ? "-" : dateFormat.format(entry.getValue())));
            }
            if (playerData.getQuestCompleted().isEmpty()) {
                normal(sender, "§f-");
            }
            normal(sender, "隐藏任务:");
            for (String hide : playerData.getQuestHide()) {
                normal(sender, "    §f" + hide);
            }
            if (playerData.getQuestHide().isEmpty()) {
                normal(sender, "§f-");
            }
        }
    };

    @SubCommand
    BaseSubCommand accept = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家接受任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            Quest quest = Cronus.getCronusService().getRegisteredQuest().get(args[1]);
            if (quest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            if (!CronusAPI.isValid(quest)) {
                error(sender, "任务 &7" + args[1] + " &c缺少必要配置.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            if (playerData.getQuest().containsKey(args[1]) && !playerData.isQuestCompleted(quest.getId())) {
                error(sender, "玩家 &7" + args[0] + " &c已接受该任务.");
                return;
            }
            playerData.acceptQuest(quest);
            playerData.push();
        }
    };

    @SubCommand
    BaseSubCommand quit = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家放弃任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest dataQuest = dataPlayer.getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[0] + " &c未接受该任务.");
                return;
            }
            if (dataPlayer.isQuestCompleted(args[1])) {
                error(sender, "玩家 &7" + args[0] + " &c已完成该任务.");
                return;
            }
            dataPlayer.failureQuest(dataQuest.getQuest());
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand stop = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家停止任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest dataQuest = dataPlayer.getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[0] + " &c未接受该任务.");
                return;
            }
            dataPlayer.stopQuest(dataQuest.getQuest());
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand complete = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家完成任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest dataQuest = dataPlayer.getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[0] + " &c未接受该任务.");
                return;
            }
            if (dataPlayer.isQuestCompleted(args[1])) {
                error(sender, "玩家 &7" + args[0] + " &c已完成该任务.");
                return;
            }
            dataPlayer.completeQuest(dataQuest.getQuest());
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand reset = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "重置任务完成时间.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            dataPlayer.getQuestCompleted().remove(args[1]);
            dataPlayer.push();
        }
    };

    @SubCommand
    BaseSubCommand visible = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"), new Argument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家切换任务可见状态.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            Quest quest = Cronus.getCronusService().getRegisteredQuest().get(args[1]);
            if (quest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            if (!playerData.getQuest().containsKey(args[1])) {
                error(sender, "玩家 &7" + args[0] + " &c未接受该任务.");
                return;
            }
            if (playerData.getQuestHide().contains(quest.getId())) {
                playerData.getQuestHide().remove(quest.getId());
            } else {
                playerData.getQuestHide().add(quest.getId());
            }
            playerData.push();
            CronusVisibleToggleEvent.call((Player) sender);
        }
    };

    @SubCommand
    BaseSubCommand action = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"),
                    new Argument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet())),
                    new Argument("状态", () -> Arrays.stream(Action.values()).map(Action::name).collect(Collectors.toList()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家执行任务动作.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            Quest quest = Cronus.getCronusService().getRegisteredQuest().get(args[1]);
            if (quest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            Action action = Action.fromName(args[2]);
            if (action == null) {
                error(sender, "状态 &7" + args[2] + " &c无效.");
                return;
            }
            DataQuest dataQuest = CronusAPI.getData(player).getQuest().getOrDefault(quest.getId(), new DataQuest(quest.getId(), quest.getFirstStage()));
            quest.eval(new QuestProgram(player, dataQuest), action);
        }
    };

    @SubCommand
    BaseSubCommand dialog = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[]{
                    new Argument("玩家"),
                    new Argument("对话", () -> Lists.newArrayList(Cronus.getCronusService().getService(Dialog.class).getDialogs().stream().map(DialogGroup::getId).collect(Collectors.toList()))),
            };
        }

        @Override
        public String getDescription() {
            return "使玩家打开任务对话.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DialogGroup dialog = Cronus.getCronusService().getService(Dialog.class).getDialog(args[1]);
            if (dialog == null) {
                error(sender, "对话 &7" + args[1] + " &c无效.");
                return;
            }
            if (dialog.getCondition() == null || dialog.getCondition().check(player)) {
                dialog.getDialog().display(player, null);
            }
        }
    };

    @SubCommand
    BaseSubCommand mirror = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "耗能监控.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("      §lCronus Mirror").newLine()
                    .add("").newLine()
                    .add("         性能监控").newLine()
                    .build());
            List<String> list = Lists.newArrayList(CronusMirror.getMirrors().keySet());
            list.sort((b, a) -> Double.compare(CronusMirror.getMirror(a).getTimeTotal(), CronusMirror.getMirror(b).getTimeTotal()));
            list.forEach(k -> {
                CronusMirror.Data v = CronusMirror.getMirror(k);
                String name = k.substring(k.indexOf(":") + 1);
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + Utils.toSimple(name)).hoverText(name).newLine()
                        .append("").newLine()
                        .append("  执行 " + v.getTimes() + " 次").newLine()
                        .append("  平均 " + v.getTimeLatest() + " 毫秒").newLine()
                        .append("  总计 " + v.getTimeTotal() + " 毫秒").newLine()
                        .toRawMessage((Player) sender)));
            });
            normal(sender, "创建完成!");
            BookFormatter.forceOpen(((Player) sender), bookBuilder.build());
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand reload = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "重载任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Cronus.reloadQuest();
            normal(sender, "重载完成.");
        }
    };
}
