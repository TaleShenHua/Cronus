package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 *
 * block.world,0,100,0 = air
 */
@Cond(name = "block", pattern = "block\\.(?<location>\\S+) (?<symbol>\\S+) (?<block>.+)", example = "block.[location] [symbol] [block]")
public class CondBlock extends Condition {

    private Location location;
    private String symbol;
    private Block block;

    @Override
    public void init(Matcher matcher, String text) {
        location = BukkitParser.toLocation(matcher.group("location"));
        symbol = String.valueOf(matcher.group("symbol"));
        block = BukkitParser.toBlock(matcher.group("block"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return location.isBukkit() && symbol.startsWith("=") == block.isSelect(location.toBukkit().getBlock());
    }

    @Override
    public String translate() {
        if (symbol.startsWith("=")) {
            return TLocale.asString("translate-condition-block0", location.asString(), block.asString());
        } else {
            return TLocale.asString("translate-condition-block0", location.asString(), block.asString());
        }
    }

    @Override
    public String toString() {
        return "CondBlock{" +
                "location=" + location +
                ", symbol='" + symbol + '\'' +
                ", block=" + block +
                '}';
    }
}
