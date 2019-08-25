package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import ink.ptms.cronus.util.Vectors;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectCancel extends Effect {

    @Override
    public String pattern() {
        return "cancel[. ]event";
    }

    @Override
    public String getExample() {
        return "cancel.event";
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram && ((QuestProgram) program).getEvent() instanceof Cancellable) {
            ((Cancellable) ((QuestProgram) program).getEvent()).setCancelled(true);

            // PlayerPickupItemEvent 特殊处理
            // 创造动画伪装玩家已经捡起该物品
            if (((QuestProgram) program).getEvent() instanceof PlayerPickupItemEvent) {
                Item item = ((PlayerPickupItemEvent) ((QuestProgram) program).getEvent()).getItem();
                item.setPickupDelay(Integer.MAX_VALUE);
                // 将物品推向玩家
                Vectors.entityPush(item, ((QuestProgram) program).getPlayer().getLocation().add(0.0D, 0.5, 0.0D), 10);
                // 短暂延迟后移除物品
                Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                    item.remove();
                    // 播放 bukkit 原生捡拾音效
                    ((QuestProgram) program).getPlayer().getWorld().playSound(item.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2F, ((Numbers.getRandom().nextFloat() - Numbers.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }, 2);
            }
        }
    }
}
