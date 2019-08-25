package ink.ptms.cronus.command;

import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.cooldown.Cooldown;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-31 21:59
 */
public abstract class CronusCommand extends BaseMainCommand {

    @TInject
    private static Cooldown cooldown = new Cooldown("Cronus:CommandSound", 100);
    private static String normal = "§7§l[§f§lCronus§7§l] §7";
    private static String error = "§c§l[§4§lCronus§c§l] §c";

    public static void normal(CommandSender sender, String args) {
        sender.sendMessage(normal + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !cooldown.isCooldown(sender.getName(), 0)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
        }
    }

    public static void error(CommandSender sender, String args) {
        sender.sendMessage(error + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !cooldown.isCooldown(sender.getName(), 0)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    }

    @Override
    public String getCommandTitle() {
        return "§e§l----- §6§lCronus Commands §e§l-----";
    }
}
