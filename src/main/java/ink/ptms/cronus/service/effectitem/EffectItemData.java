package ink.ptms.cronus.service.effectitem;

import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.util.Utils;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @Author 坏黑
 * @Since 2019-07-26 11:30
 */
public class EffectItemData {

    private String id;
    private Condition condition;
    private QuestEffect effect;
    private QuestEffect effectCondition;
    private QuestEffect effectCooldown;
    private EffectItemEvent event;
    private ItemStack item;
    private long cooldown;

    public EffectItemData(ConfigurationSection config) {
        id = config.getName();
        condition = ConditionParser.fromObject(config.get("condition"));
        effect = config.contains("effect") ? new QuestEffect(config.getStringList("effect")) : null;
        effectCondition = config.contains("effect-condition") ? new QuestEffect(config.getStringList("effect-condition")) : null;
        effectCooldown = config.contains("effect-cooldown") ? new QuestEffect(config.getStringList("effect-cooldown")) : null;
        event = EffectItemEvent.fromString(config.getString("event"));
        item = config.contains("item") ? BukkitParser.toItemStack(config.getString("item")) : null;
        cooldown = config.contains("cooldown") ? Utils.toTime(config.getString("cooldown")) : 0;
    }

    public String getId() {
        return id;
    }

    public Condition getCondition() {
        return condition;
    }

    public QuestEffect getEffect() {
        return effect;
    }

    public QuestEffect getEffectCondition() {
        return effectCondition;
    }

    public QuestEffect getEffectCooldown() {
        return effectCooldown;
    }

    public EffectItemEvent getEvent() {
        return event;
    }

    public ItemStack getItem() {
        return item;
    }

    public long getCooldown() {
        return cooldown;
    }
}
