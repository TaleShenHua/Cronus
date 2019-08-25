package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectItem extends Effect {

    private String action;
    private ItemStack itemTake;
    private org.bukkit.inventory.ItemStack itemGive;

    @Override
    public String pattern() {
        return "item\\.(?<action>\\S+) (?<item>.+)";
    }

    @Override
    public String getExample() {
        return "item.[action] [item]";
    }

    @Override
    public void match(Matcher matcher) {
        action = matcher.group("action").toLowerCase();
        switch (action) {
            case "take":
            case "remove":
                itemTake = BukkitParser.toItemStack(matcher.group("item"));
                break;
            case "add":
            case "give":
                String[] item = matcher.group("item").split(" ");
                itemGive = Cronus.getCronusService().getItemStorage().getItem(item[0]);
                itemGive.setAmount(item.length > 1 ? NumberConversions.toInt(item[1]) : 1);
                break;
        }
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            switch (action) {
                case "take":
                case "remove":
                    itemTake.takeItem(player);
                    break;
                case "add":
                case "give":
                    player.getInventory().addItem(itemGive).forEach((k, v) -> player.getWorld().dropItem(player.getLocation(), v));
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "EffectItem{" +
                "action='" + action + '\'' +
                ", itemTake=" + itemTake +
                ", itemGive=" + itemGive +
                '}';
    }
}
