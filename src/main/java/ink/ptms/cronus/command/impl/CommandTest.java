package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.CondNull;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.internal.program.effect.EffectNull;
import ink.ptms.cronus.internal.program.effect.EffectParser;
import ink.ptms.cronus.uranus.program.ProgramLoader;
import ink.ptms.cronus.uranus.program.effect.Effect;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.command.base.*;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.book.BookFormatter;
import io.izzel.taboolib.util.book.builder.BookBuilder;
import io.izzel.taboolib.util.book.builder.PageBuilder;
import io.izzel.taboolib.util.chat.ComponentSerializer;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

/**
 * @Author 坏黑
 * @Since 2019-05-31 22:10
 */
@BaseCommand(name = "CronusTest", aliases = {"cTest", "ct"}, permission = "*")
public class CommandTest extends CronusCommand {

    @SubCommand
    BaseSubCommand condition = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "测试条件语法";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("表达式")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Condition parse = ConditionParser.parse(ArrayUtil.arrayJoin(args, 0));
            if (parse instanceof CondNull) {
                error(sender, "条件格式错误.");
            } else {
                CronusMirror.Data data = new CronusMirror.Data().check(() -> {
                    try {
                        normal(sender, "条件执行结果: " + (parse.check((Player) sender, new DataQuest(), null) ? "&a成功" : "&c失败"));
                    } catch (Throwable t) {
                        error(sender, "条件执行失败: " + t.getMessage());
                    }
                });
                normal(sender, "执行耗时: &f" + data.getTimeLatest() + "ms");
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand effect = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "测试动作语法";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("表达式")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Effect parse = EffectParser.parse(ArrayUtil.arrayJoin(args, 0));
            if (parse instanceof EffectNull) {
                error(sender, "动作格式错误.");
            } else {
                CronusMirror.Data data = new CronusMirror.Data().check(() -> {
                    try {
                        parse.eval(new QuestProgram((Player) sender, new DataQuest()));
                    } catch (Throwable t) {
                        error(sender, "动作执行失败: " + t.getMessage());
                    }
                });
                normal(sender, "执行耗时: &f" + data.getTimeLatest() + "ms");
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand conditions = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出可用条件语法";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("关键字", false)
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("     §lCronus Conditions").newLine()
                    .add("").newLine()
                    .add("         可用条件").newLine()
                    .build());
            Cronus.getCronusService().getRegisteredCondition().entrySet().stream().filter(e -> args.length == 0 || e.getKey().contains(args[0])).forEach(e -> {
                Cond cond = e.getValue().getCondition().getAnnotation(Cond.class);
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + Utils.toSimple(e.getValue().getCondition().getSimpleName())).hoverText(e.getValue().getCondition().getSimpleName()).newLine()
                        .append("").newLine()
                        .append("  格式 " + Utils.toSimple(cond.example())).hoverText(format(cond.example())).newLine()
                        .append("  位置 " + Utils.toSimple(e.getValue().getCondition().getName())).hoverText(e.getValue().getCondition().getName()).newLine()
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
    BaseSubCommand effects = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出可用条件动作";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("关键字", false)
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("      §lCronus Effects").newLine()
                    .add("").newLine()
                    .add("         可用动作").newLine()
                    .build());
            ProgramLoader.getEffects().stream().filter(e -> args.length == 0 || e.getClass().getSimpleName().contains(args[0])).forEach(e -> {
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + Utils.toSimple(e.getClass().getSimpleName())).hoverText(e.getClass().getSimpleName()).newLine()
                        .append("").newLine()
                        .append("  格式 " + Utils.toSimple(e.getExample())).hoverText(format(e.getExample())).newLine()
                        .append("  位置 " + Utils.toSimple(e.getClass().getName())).hoverText(e.getClass().getName()).newLine()
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
    BaseSubCommand firework = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "在玩家位置播放烟花";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("玩家"), new Argument("-a", false)
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 §7" + args[0] + " §c不在线.");
                return;
            }
            Firework firework = Utils.spawnFirework(player.getLocation());
            firework.setMetadata("no_damage", new FixedMetadataValue(Cronus.getPlugin(), 1));
            if (args.length > 1 && args[1].equalsIgnoreCase("-a")) {
                Bukkit.getScheduler().runTaskLater(Cronus.getInst(), firework::detonate, 1);
            }
        }
    };

    @SubCommand
    BaseSubCommand enchants = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出所有附魔类型";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Enchantment[] enchantments = Enchantment.values();
            TellrawJson json = TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7附魔: §f");
            int i = 1;
            for (Enchantment enchantment : enchantments) {
                if (enchantment != null) {
                    json.append("§f" + enchantment.getName()).hoverText("§7点击复制").clickSuggest(enchantment.getName());
                    if (i++ < enchantments.length) {
                        json.append("§8, ");
                    }
                }
            }
            json.send(sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand potions = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出所有效果类型";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            PotionEffectType[] potions = PotionEffectType.values();
            TellrawJson json = TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7效果: §f");
            int i = 1;
            for (PotionEffectType potion : potions) {
                if (potion != null) {
                    json.append("§f" + potion.getName()).hoverText("§7点击复制").clickSuggest(potion.getName());
                    if (i++ < potions.length) {
                        json.append("§8, ");
                    }
                }
            }
            json.send(sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand location = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "打印当前坐标";
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Location location = ((Player) sender).getLocation();
            String str = location.getWorld().getName() + "," + Numbers.format(location.getX()) + "," + Numbers.format(location.getY()) + "," + Numbers.format(location.getZ()) + "," + Numbers.format((double) location.getYaw()) + "," + Numbers.format((double) location.getPitch());
            TellrawJson.create()
                    .append("§7§l[§f§lCronus§7§l] §7坐标: §f")
                    .append(str).hoverText("§f点击复制").clickSuggest(str)
                    .send(sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    private String format(String example) {
        return "§7" + example.replaceAll("\\.", "§8$0§7").replaceAll("\\[(\\S+)]", "§e[§6$1§e]§7");
    }
}
