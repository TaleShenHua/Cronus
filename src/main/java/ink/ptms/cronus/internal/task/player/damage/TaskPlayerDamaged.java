package ink.ptms.cronus.internal.task.player.damage;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.bukkit.DamageCause;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.service.selector.EntitySelector;
import io.izzel.taboolib.util.lite.Numbers;
import io.izzel.taboolib.util.lite.Servers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_damaged")
public class TaskPlayerDamaged extends QuestTask<Event> {

    private int damage;
    private String attacker;
    private ItemStack weapon;
    private DamageCause cause;

    public TaskPlayerDamaged(ConfigurationSection config) {
        super(config);
    }

    public int getDamage() {
        return damage;
    }

    public double getDamage(DataQuest dataQuest) {
        return dataQuest.getDataStage().getDouble(getId() + ".damage");
    }

    @Override
    public void init(Map<String, Object> data) {
        damage = NumberConversions.toInt(data.getOrDefault("damage", 1));
        attacker = data.containsKey("attacker") ? String.valueOf(data.get("attacker")) : null;
        weapon = data.containsKey("weapon") ? BukkitParser.toItemStack(data.get("weapon")) : null;
        cause = data.containsKey("cause") ? BukkitParser.toDamageCause(data.get("cause")) : null;
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getDouble(getId() + ".damage") >= damage;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, Event event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            LivingEntity a = Servers.getLivingAttackerInDamageEvent(e);
            return (weapon == null || weapon.isItem(a.getEquipment().getItemInHand())) && (attacker == null || Cronus.getCronusService().getService(EntitySelector.class).isSelect(a, attacker)) && (cause == null || cause.isSelect(e.getCause()));
        } else {
            EntityDamageEvent e = (EntityDamageEvent) event;
            return weapon == null && attacker == null && (cause == null || cause.isSelect(e.getCause()));
        }
    }

    @Override
    public void next(Player player, DataQuest dataQuest, Event event) {
        EntityDamageEvent e = ((EntityDamageEvent) event);
        dataQuest.getDataStage().set(getId() + ".damage", Numbers.format(dataQuest.getDataStage().getInt(getId() + ".damage") + e.getDamage()));
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".damage", damage);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".damage", 0);
    }

    @Override
    public String toString() {
        return "TaskPlayerDamaged{" +
                "damage=" + damage +
                ", attacker=" + attacker +
                ", weapon=" + weapon +
                ", cause=" + cause +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\'' +
                ", action=" + action +
                '}';
    }
}
