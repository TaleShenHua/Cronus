package ink.ptms.cronus.service.guide;

import ink.ptms.cronus.internal.bukkit.Location;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-30 23:08
 */
public class GuideWayCache {

    private double distance;
    private Location target;
    private List<String> text;

    public GuideWayCache(double distance, Location target, List<String> text) {
        this.distance = distance;
        this.target = target;
        this.text = text;
    }

    public double getDistance() {
        return distance;
    }

    public Location getTarget() {
        return target;
    }

    public List<String> getText() {
        return text;
    }
}
