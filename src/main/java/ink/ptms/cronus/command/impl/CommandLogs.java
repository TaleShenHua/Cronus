package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.internal.program.NoneProgram;
import ink.ptms.cronus.uranus.function.FunctionParser;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.ArrayUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-10 20:26
 */
@BaseCommand(name = "CronusLogs", aliases = {"cLogs", "cl"}, permission = "*")
public class CommandLogs extends Command {

    @SubCommand
    BaseSubCommand write = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "写入任务日志";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("内容")};
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer data = CronusAPI.getData(player);
            data.writeQuestLogs(FunctionParser.parseAll(new NoneProgram(player), TLocale.Translate.setColored(ArrayUtil.arrayJoin(args, 1))));
            data.push();
        }
    };

    @SubCommand
    BaseSubCommand clear = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "清空任务日志";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家")};
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer data = CronusAPI.getData(player);
            data.getQuestLogs().clear();
            data.push();
        }
    };

    @SubCommand
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "打开任务日志";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家")};
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            CronusAPI.openQuestLogs(player);
        }
    };

}
