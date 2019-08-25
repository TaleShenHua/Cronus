package ink.ptms.cronus.internal.task.player.damage;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.DamageCause;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.service.selector.EntitySelector;
import io.izzel.taboolib.util.lite.Servers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_death")
public class TaskPlayerDeath extends Countable<PlayerDeathEvent> {

    private String attacker;
    private ItemStack weapon;
    private DamageCause cause;

    public TaskPlayerDeath(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        attacker = data.containsKey("attacker") ? String.valueOf(data.get("attacker")) : null;
        weapon = data.containsKey("weapon") ? BukkitParser.toItemStack(data.get("weapon")) : null;
        cause = data.containsKey("cause") ? BukkitParser.toDamageCause(data.get("cause")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerDeathEvent e) {
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            LivingEntity a = Servers.getLivingAttackerInDamageEvent((EntityDamageByEntityEvent) player.getLastDamageCause());
            return (weapon == null || weapon.isItem(a.getEquipment().getItemInHand())) && (attacker == null || Cronus.getCronusService().getService(EntitySelector.class).isSelect(a, attacker)) && (cause == null || cause.isSelect(player.getLastDamageCause().getCause()));
        } else {
            return weapon == null && attacker == null && (cause == null || cause.isSelect(player.getLastDamageCause().getCause()));
        }
    }

    @Override
    public String toString() {
        return "TaskPlayerDeath{" +
                "attacker=" + attacker +
                ", weapon=" + weapon +
                ", cause=" + cause +
                ", count=" + count +
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
