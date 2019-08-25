package ink.ptms.cronus.internal.condition.impl;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.lite.SimpleEquip;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 * <p>
 * item.hand == diamond
 * item.inventory == diamond
 */
@Cond(name = "item.slot", pattern = "item\\.(?<slot>\\S+) (?<symbol>\\S+) (?<value>.+)", example = "item.[slot] [symbol] [item]")
public class CondItem extends Condition {

    private int slot;
    private String symbol;
    private SimpleEquip equipment;
    private ItemStack itemStack;
    private boolean inventory;

    @Override
    public void init(Matcher matcher, String text) {
        // 背包检查
        if (matcher.group("slot").equalsIgnoreCase("inventory")) {
            inventory = true;
        } else {
            // 指定位置
            try {
                equipment = SimpleEquip.valueOf(matcher.group("slot").toUpperCase());
            } catch (Throwable ignored) {
                slot = NumberConversions.toInt(matcher.group("slot"));
            }
        }
        symbol = matcher.group("symbol");
        itemStack = BukkitParser.toItemStack(matcher.group("value"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        if (inventory) {
            return symbol.startsWith("=") == itemStack.hasItem(player);
        } else {
            return symbol.startsWith("=") == itemStack.isItem(equipment != null ? equipment.getItem(player) : player.getInventory().getItem(slot));
        }
    }

    @Override
    public String translate() {
        if (symbol.startsWith("=")) {
            if (inventory) {
                return TLocale.asString("translate-condition-item4", itemStack.asString());
            } else if (equipment != null) {
                return TLocale.asString("translate-condition-item0", TLocale.asString("translate-item-" + equipment.name().toLowerCase().replace("_", "")), itemStack.asString());
            } else {
                return TLocale.asString("translate-condition-item2", String.valueOf(slot), itemStack.asString());
            }
        } else {
            if (inventory) {
                return TLocale.asString("translate-condition-item5", itemStack.asString());
            } else if (equipment != null) {
                return TLocale.asString("translate-condition-item1", TLocale.asString("translate-item-" + equipment.name().toLowerCase().replace("_", "")), itemStack.asString());
            } else {
                return TLocale.asString("translate-condition-item3", String.valueOf(slot), itemStack.asString());
            }
        }
    }

    @Override
    public String toString() {
        return "CondItem{" +
                "slot=" + slot +
                ", symbol='" + symbol + '\'' +
                ", equipment=" + equipment +
                ", itemStack=" + itemStack +
                ", inventory=" + inventory +
                '}';
    }
}
