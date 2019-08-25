package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-26 18:16
 */
@BaseCommand(name = "CronusVariable", aliases = {"cVariable", "cVar", "cv"}, permission = "*")
public class CommandVariable extends CronusCommand {

    @SubCommand
    BaseSubCommand temp = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "设置临时变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("键"), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            playerData.getDataTemp().set(args[1], args[2].equalsIgnoreCase("null") ? null : args[2]);
            playerData.push();
        }
    };

    @SubCommand
    BaseSubCommand global = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "设置永久变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("玩家"), new Argument("键"), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &7" + args[0] + " &c离线.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            playerData.getDataGlobal().set(args[1], args[2].equalsIgnoreCase("null") ? null : args[2]);
            playerData.push();
        }
    };

    @SubCommand
    BaseSubCommand server = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "设置全局变量";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {new Argument("键"), new Argument("值")};
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Cronus.getCronusService().getDatabase().setGlobalVariable(args[1], args[2].equalsIgnoreCase("null") ? null : args[2]);
        }
    };

}
