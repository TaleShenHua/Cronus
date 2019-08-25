package ink.ptms.cronus.internal.bukkit;

import org.bukkit.event.player.PlayerFishEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class FishState extends EnumEntry<PlayerFishEvent.State> {

    public FishState(String in) {
        super(in);
    }

    @Override
    public Class origin() {
        return PlayerFishEvent.State.class;
    }

    @Override
    public String toString() {
        return "FishState{" +
                "data=" + data +
                '}';
    }
}
