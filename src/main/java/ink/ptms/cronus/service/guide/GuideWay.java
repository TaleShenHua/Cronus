package ink.ptms.cronus.service.guide;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.event.*;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.module.packet.TPacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author 坏黑
 * @Since 2019-05-29 19:33
 */
@Auto
public class GuideWay implements Service, Listener {

    private boolean performance;
    private Map<String, List<GuideWayData>> data = Maps.newConcurrentMap();

    public GuideWayData fromUUID(UUID uuid) {
        return data.values().stream().flatMap(Collection::stream).filter(guideWayData -> guideWayData.getEntity().stream().anyMatch(armorStand -> armorStand.getUniqueId().equals(uuid))).findFirst().orElse(null);
    }

    public void cancel(Player player) {
        List<GuideWayData> list = data.remove(player.getName());
        if (list != null) {
            list.forEach(GuideWayData::cancel);
        }
    }

    public void add(Player player, GuideWayData data) {
        this.data.computeIfAbsent(player.getName(), d -> Lists.newArrayList()).add(data);
    }

    private void update(Player player) {
        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> CronusAPI.guideHandle(player), 20);
    }

    private boolean isMoved(PlayerMoveEvent e) {
        return e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ();
    }

    @EventHandler
    public void e(CronusReloadEvent e) {
        Bukkit.getOnlinePlayers().forEach(CronusAPI::guideHandle);
    }

    @EventHandler
    public void e(CronusDataPullEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusQuestAcceptEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusQuestSuccessEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusQuestFailureEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusQuestStopEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusStageAcceptEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusStageSuccessEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusTaskSuccessEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(PlayerChangedWorldEvent e) {
        update(e.getPlayer());
    }

    @EventHandler
    public void e(CronusVisibleToggleEvent e) {
        CronusAPI.guideHandle(e.getPlayer());
    }

    @EventHandler
    public void e(PlayerQuitEvent e) {
        cancel(e.getPlayer());
    }

    @EventHandler
    public void e(PlayerMoveEvent e) {
        if (performance ? !e.getFrom().getBlock().equals(e.getTo().getBlock()) : isMoved(e)) {
            List<GuideWayData> list = data.get(e.getPlayer().getName());
            if (list != null) {
                Cronus.getCronusService().async(() -> list.forEach(GuideWayData::update));
            }
        }
    }

    @Override
    public void init() {
        // 注册数据包监听
        TPacketHandler.addListener(Cronus.getInst(), new GuideWayPacket());
        // 粒子和刷新
        Bukkit.getScheduler().runTaskTimerAsynchronously(Cronus.getInst(), () -> {
            data.values().stream().flatMap(Collection::stream).forEach(guideWayDatum -> {
                guideWayDatum.update();
                guideWayDatum.display();
            });
        }, 0, 40);
        // 性能优化模式
        performance = Cronus.getConf().getString("Settings.guide-mode", "performance").equalsIgnoreCase("performance");
        CronusReloadServiceEvent.call(this);
    }

    @Override
    public void cancel() {
        data.values().stream().flatMap(Collection::stream).forEach(GuideWayData::cancel);
    }

    public boolean isPerformance() {
        return performance;
    }

    public Map<String, List<GuideWayData>> getData() {
        return data;
    }
}
