package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.TabooLibAPI;
import io.izzel.taboolib.cronus.CronusUtils;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionPlayer extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "player";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program.getSender() instanceof Player) {
            Player player = (Player) program.getSender();
            switch (args[0].toLowerCase()) {
                case "name":
                    return player.getName();
                case "name.display":
                    return player.getDisplayName();
                case "name.list":
                    return player.getPlayerListName();
                case "exp":
                    return CronusUtils.getTotalExperience(player);
                case "exp.at.level":
                    return CronusUtils.getExpAtLevel(player.getLevel());
                case "exp.to.level":
                    return CronusUtils.getExpToLevel(player.getLevel());
                case "exp.until.next.level":
                    return CronusUtils.getExpUntilNextLevel(player);
                case "level":
                    return player.getLevel();
                case "world":
                    return player.getWorld();
                case "health":
                    return player.getHealth();
                case "health.max":
                    return player.getMaxHealth();
                case "food":
                    return player.getFoodLevel();
                case "saturation":
                    return player.getSaturation();
                case "exhaustion":
                    return player.getExhaustion();
                case "gamemode":
                    return player.getGameMode().name();
                case "loc.x":
                case "location.x":
                    return player.getLocation().getX();
                case "loc.y":
                case "location.y":
                    return player.getLocation().getY();
                case "loc.z":
                case "location.z":
                    return player.getLocation().getZ();
                case "loc.block.x":
                case "location.block.x":
                    return player.getLocation().getBlockX();
                case "loc.block.y":
                case "location.block.y":
                    return player.getLocation().getBlockY();
                case "loc.block.z":
                case "location.block.z":
                    return player.getLocation().getBlockZ();
                case "fire.tick":
                    return player.getFireTicks();
                case "ip":
                    return player.getAddress().getHostName();
                case "playtime":
                    return player.getPlayerTime();
                case "playtime.first":
                    return player.getFirstPlayed();
                case "playtime.last":
                    return player.getLastPlayed();
                case "$":
                case "money":
                case "balance":
                    return TabooLibAPI.getPluginBridge().economyLook(player);
                default:
                    return "<invalid>";
            }
        }
        return "<none>";
    }
}
