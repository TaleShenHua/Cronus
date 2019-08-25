package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.command.base.*;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-31 22:10
 */
@BaseCommand(name = "CronusItem", aliases = {"cItem", "ci"}, permission = "*")
public class CommandItem extends CronusCommand {

    @SubCommand
    BaseSubCommand list = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "查看任务物品";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!Cronus.getCronusService().isNonHooked()) {
                normal(sender, "该功能已被其他插件代替.");
            } else {
                if (Cronus.getCronusService().getItemStorage().getItems().isEmpty()) {
                    error(sender, "空空如也.");
                    return;
                }
                normal(sender, "任务物品: " + Cronus.getCronusService().getItemStorage().getItems().stream().map(i -> "§8" + i + "§r").collect(Collectors.joining(", ")));
            }
        }
    };

    @SubCommand
    BaseSubCommand give = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("名称", () -> Cronus.getCronusService().getItemStorage().getItems()), new Argument("玩家", false), new Argument("数量", false)
            };
        }

        @Override
        public String getDescription() {
            return "获取任务物品";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!Cronus.getCronusService().isNonHooked()) {
                normal(sender, "该功能已被其他插件代替.");
            } else {
                ItemStack item = Cronus.getCronusService().getItemStorage().getItem(args[0]);
                if (Items.isNull(item)) {
                    error(sender, "物品 &7" + args[0] + " &c无效.");
                    return;
                }
                Player player;
                if (args.length > 1) {
                    player = Bukkit.getPlayerExact(args[1]);
                    if (player == null) {
                        error(sender, "玩家 &7" + args[1] + " &c离线.");
                        return;
                    }
                } else if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    error(sender, "缺少玩家参数.");
                    return;
                }
                if (args.length > 2) {
                    item.setAmount(NumberConversions.toInt(args[2]));
                }
                Utils.addItem(player, item);
            }
        }
    };

    @SubCommand
    BaseSubCommand save = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("名称")
            };
        }

        @Override
        public String getDescription() {
            return "储存任务物品";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!Cronus.getCronusService().isNonHooked()) {
                normal(sender, "该功能已被其他插件代替.");
            } else {
                if (Utils.isNull(((Player) sender).getItemInHand())) {
                    error(sender, "无效物品.");
                    return;
                }
                Cronus.getCronusService().getItemStorage().addItem(args[0], ((Player) sender).getItemInHand());
                normal(sender, "物品 &f" + args[0] + " &7已储存.");
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand delete = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("名称", () -> Cronus.getCronusService().getItemStorage().getItems())
            };
        }

        @Override
        public String getDescription() {
            return "删除任务物品";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!Cronus.getCronusService().isNonHooked()) {
                normal(sender, "该功能已被其他插件代替.");
            } else {
                Cronus.getCronusService().getItemStorage().delItem(args[0]);
                normal(sender, "物品 &f" + args[0] + " &7已删除.");
            }
        }
    };

    @SubCommand
    BaseSubCommand importGui = new BaseSubCommand() {

        @Override
        public String getLabel() {
            return "import";
        }

        @Override
        public String getDescription() {
            return "批量储存任务物品";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (!Cronus.getCronusService().isNonHooked()) {
                normal(sender, "该功能已被其他插件代替.");
            } else {
                ((Player) sender).playSound(((Player) sender).getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);
                ((Player) sender).openInventory(MenuBuilder.builder(Cronus.getInst())
                        .title("物品导入")
                        .rows(3)
                        .items()
                        .close(e -> {
                            for (ItemStack itemStack : e.getInventory()) {
                                if (Items.isNull(itemStack)) {
                                    continue;
                                }
                                String name = TLocale.Translate.setUncolored(Items.getName(itemStack));
                                Cronus.getCronusService().getItemStorage().addItem(name, itemStack);
                                normal(sender, "物品 &f" + name + " &7已储存.");
                                Utils.addItem((Player) sender, itemStack);
                            }
                        }).build());
            }
        }
    };

}
