package ink.ptms.cronus.service.guide;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import io.izzel.taboolib.Version;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-29 19:32
 */
public class GuideWayData {

    private static GuideWay service = Cronus.getCronusService().getService(GuideWay.class);
    private static DecimalFormat doubleFormat = new DecimalFormat("#.##");
    private String owner;
    private Location target;
    private List<ArmorStand> entity;
    private List<String> text;
    private double distance;

    GuideWayData(String owner, Location target, List<ArmorStand> entity, List<String> text, double distance) {
        this.owner = owner;
        this.target = target;
        this.entity = entity;
        this.text = text;
        this.distance = distance;
    }

    public static GuideWayData create(Player player, Location target, List<String> text, double distance) {
        Location midPoint = getMidPoint(player.getEyeLocation(), target, distance);
        if (midPoint != null) {
            String dis = doubleFormat.format(player.getLocation().distance(target));
            List<ArmorStand> list = Lists.newArrayList();
            for (int i = 0; i < text.size(); i++) {
                String line = text.get(i);
                list.add(player.getWorld().spawn(midPoint.clone().add(0, i * -0.3 - 0.5, 0), ArmorStand.class, e -> {
                    e.setCustomName(line.replace("{distance}", dis));
                    e.setCustomNameVisible(true);
                    e.setGravity(false);
                    e.setVisible(false);
                    e.setSmall(true);
                    e.setBasePlate(false);
                    e.setMarker(true);
                    e.setInvulnerable(true);
                    e.setGlowing(true);
                    e.setMetadata("cronus_guide_owner", new FixedMetadataValue(Cronus.getInst(), player.getName()));
                }));
            }
            return new GuideWayData(player.getName(), target, list, text, distance);
        }
        return null;
    }

    public void update() {
        try {
            Player player = Bukkit.getPlayer(owner);
            if (player == null) {
                cancel();
                return;
            }
            Location midPoint = getMidPoint(player.getEyeLocation(), target, distance);
            if (midPoint != null) {
                String dis = doubleFormat.format(player.getLocation().distance(target));
                for (int i = 0; i < entity.size(); i++) {
                    entity.get(i).setCustomName(text.get(i).replace("{distance}", dis));
                    entity.get(i).teleport(midPoint.clone().add(0, i * -0.3 - 0.5, 0));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void cancel() {
        entity.forEach(Entity::remove);
    }

    public void display() {
        Player player = Bukkit.getPlayer(owner);
        if (player != null) {
            player.spawnParticle(Version.isAfter(Version.v1_9) ? Particle.END_ROD : Particle.CLOUD, target, 500, 0, 100, 0, 0);
        }
    }

    public static Location getMidPoint(Location start, Location end, double distance) {
        if (!start.getWorld().equals(end.getWorld())) {
            return null;
        }
        if (start.distance(end) < distance) {
            return end.clone();
        }
        Vector vectorAB = end.clone().subtract(start).toVector().normalize();
        return start.clone().add(vectorAB.clone().multiply(distance));
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public String getOwner() {
        return owner;
    }

    public Location getTarget() {
        return target;
    }

    public List<ArmorStand> getEntity() {
        return entity;
    }

    public List<String> getText() {
        return text;
    }

    public double getDistance() {
        return distance;
    }
}