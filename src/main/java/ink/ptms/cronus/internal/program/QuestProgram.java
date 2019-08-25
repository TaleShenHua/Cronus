package ink.ptms.cronus.internal.program;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-26 22:38
 */
public class QuestProgram extends Program {

    private DataQuest dataQuest;
    private Event event;
    private int delay;

    public QuestProgram(Player player, DataQuest dataQuest) {
        this.dataQuest = dataQuest;
        this.sender = player;
    }

    public QuestProgram(Player player, DataQuest dataQuest, Event event) {
        this.dataQuest = dataQuest;
        this.sender = player;
        this.event = event;
    }

    public void eval(List<Effect> effects) {
        delay = 0;
        for (Effect effect : effects) {
            if (delay == 0) {
                Bukkit.getScheduler().runTask(Cronus.getInst(), () -> eval(effect));
            } else {
                Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> eval(effect), delay);
            }
        }
    }

    private void eval(Effect effect) {
        try {
            effect.eval(this);
        } catch (Throwable t) {
            logger.error("程序 \"" + effect.getSource() + "\" 执行异常: " + t.getMessage());
        }
    }

    public Player getPlayer() {
        return (Player) sender;
    }

    public DataQuest getDataQuest() {
        return dataQuest;
    }

    public DataPlayer getDataPlayer() {
        return CronusAPI.getData(getPlayer());
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Event getEvent() {
        return event;
    }
}
