package ink.ptms.cronus.internal.bukkit;

import io.izzel.taboolib.module.lite.SimpleI18n;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-30 13:46
 */
public class Entity {

    private List<Point> points;

    public Entity(String in) {
        points = Arrays.stream(in.split("[,;]")).map(Point::new).collect(Collectors.toList());
    }

    public boolean isSelect(org.bukkit.entity.Entity entity) {
        for (Point b : points) {
            if (b.isSelect(entity)) {
                return true;
            }
        }
        return false;
    }

    public class Point {

        private String name;
        private String type;

        public Point(String in) {
            for (String e : in.split(",")) {
                String[] v = e.split("=");
                if (v.length == 1) {
                    type = v[0];
                } else if (v[0].equalsIgnoreCase("type")) {
                    type = v[1];
                } else if (v[0].equalsIgnoreCase("name")) {
                    name = v[1];
                }
            }
        }

        public boolean isSelect(org.bukkit.entity.Entity entity) {
            return (name == null || SimpleI18n.getCustomName(entity).equals(name)) && (type == null || entity.getType().name().equalsIgnoreCase(type));
        }

        @Override
        public String toString() {
            return "Point{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "points=" + points +
                '}';
    }
}
