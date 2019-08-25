package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.cronus.CronusUtils;
import io.izzel.taboolib.module.compat.EconomyHook;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectPlayer extends Effect {

    @TInject
    private static TLogger logger;
    private String action;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "player\\.(?!var|val)(?<action>\\S+) (?<symbol>\\S+) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "player.[action] [symbol] [content]";
    }

    @Override
    public void match(Matcher matcher) {
        action = matcher.group("action").toLowerCase();
        symbol = matcher.group("symbol").toLowerCase();
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            String parsed = FunctionParser.parseAll(program, value);
            switch (action) {
                case "tag": {
                    switch (symbol) {
                        case "+":
                            player.addScoreboardTag(parsed);
                            break;
                        case "-":
                            player.removeScoreboardTag(parsed);
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                case "heart":
                case "health": {
                    double v = NumberConversions.toDouble(value);
                    switch (symbol) {
                        case "+":
                            player.setHealth(Math.min(player.getHealth() + v, player.getMaxHealth()));
                            break;
                        case "-":
                            player.setHealth(Math.max(player.getHealth() + v, 0));
                            break;
                        case "=":
                            player.setHealth(Math.max(Math.min(player.getMaxHealth(), v), 0));
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                case "food":
                case "hunger": {
                    int v = NumberConversions.toInt(value);
                    switch (symbol) {
                        case "+":
                            player.setFoodLevel(Math.min(player.getFoodLevel() + v, 20));
                            break;
                        case "-":
                            player.setFoodLevel(Math.max(player.getFoodLevel() + v, 0));
                            break;
                        case "=":
                            player.setFoodLevel(Math.max(Math.min(v, 20), 0));
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                case "xp":
                case "exp":
                case "experience": {
                    int v = NumberConversions.toInt(value);
                    switch (symbol) {
                        case "+":
                            CronusUtils.setTotalExperience(player, CronusUtils.getTotalExperience(player) + v);
                            break;
                        case "-":
                            CronusUtils.setTotalExperience(player, Math.max(CronusUtils.getTotalExperience(player) - v, 0));
                            break;
                        case "=":
                            CronusUtils.setTotalExperience(player, Math.max(v, 0));
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                case "level": {
                    int v = NumberConversions.toInt(value);
                    switch (symbol) {
                        case "+":
                            player.setLevel(player.getLevel() + v);
                            break;
                        case "-":
                            player.setLevel(Math.max(player.getLevel() - v, 0));
                            break;
                        case "=":
                            player.setLevel(Math.max(v, 0));
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                case "$":
                case "money":
                case "balance": {
                    int v = NumberConversions.toInt(value);
                    switch (symbol) {
                        case "+":
                            EconomyHook.add(player, v);
                            break;
                        case "-":
                            EconomyHook.remove(player, v);
                            break;
                        case "=":
                            EconomyHook.set(player, v);
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                }
                default:
                    logger.warn("Invalid Action: " + action + " " + symbol + " " + value);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "EffectPlayer{" +
                "action='" + action + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
