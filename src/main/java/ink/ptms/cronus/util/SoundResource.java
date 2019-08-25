package ink.ptms.cronus.util;

import io.izzel.taboolib.TabooLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SoundResource {

    private String sound;
    private Float a;
    private Float b;
    private int delay;

    SoundResource(String sound, float a, float b) {
        this(sound, a, b, 0);
    }

    SoundResource(String sound, float a, float b, int delay) {
        this.sound = sound;
        this.a = a;
        this.b = b;
        this.delay = delay;
    }

    public SoundResource(String s) {
        this.parse(s);
    }

    public void play(Player p) {
        Bukkit.getScheduler().runTaskLater(TabooLib.getPlugin(), () -> p.playSound(p.getLocation(), this.sound, this.a, this.b), this.delay);
    }

    public void play(Location l) {
        Bukkit.getScheduler().runTaskLater(TabooLib.getPlugin(), () -> l.getWorld().playSound(l, this.sound, this.a, this.b), this.delay);
    }

    public void parse(String s) {
        try {
            String[] split = s.split("-");
            this.sound = split[0];
            this.a = Float.parseFloat(split[1]);
            this.b = Float.parseFloat(split[2]);
            this.delay = split.length > 3 ? Integer.parseInt(split[3]) : 0;
        } catch (Exception var3) {
            this.sound = "entity.villager.no";
            this.a = 1.0F;
            this.b = 1.0F;
            this.delay = 0;
        }
    }

    public String getSound() {
        return this.sound;
    }

    public Float getA() {
        return this.a;
    }

    public Float getB() {
        return this.b;
    }

    public int getDelay() {
        return this.delay;
    }

    public String toString() {
        return "SoundPack{sound=" + this.sound + ", a=" + this.a + ", b=" + this.b + ", delay=" + this.delay + '}';
    }
}
