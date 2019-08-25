package ink.ptms.cronus.internal.bukkit;

import ink.ptms.cronus.util.Sxpression;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-30 17:16
 */
public class Enchantment {

    @TInject
    private static TLogger logger;
    private static Pattern pattern = Pattern.compile("(?<enchant>\\S+)[ ]?(?<expression>.+)");
    private List<Point> points;

    public Enchantment(String in) {
        points = Arrays.stream(in.split("[,;]")).map(Point::new).collect(Collectors.toList());
    }

    public boolean isSelect(org.bukkit.enchantments.Enchantment enchant, int level) {
        for (Point b : points) {
            if (b.isSelect(enchant, level)) {
                return true;
            }
        }
        return false;
    }

    public class Point {

        private org.bukkit.enchantments.Enchantment enchant;
        private Sxpression expression;

        public Point(String in) {
            Matcher matcher = pattern.matcher(in);
            if (!matcher.find()) {
                logger.error("Enchantment \"" + in + "\" parsing failed.");
                return;
            }
            enchant = Items.asEnchantment(matcher.group("enchant").toUpperCase());
            if (enchant == null) {
                logger.error("Enchantment \"" + in + "\" parsing failed.");
                return;
            }
            expression = new Sxpression(matcher.group("expression"));
        }

        public boolean isSelect(org.bukkit.enchantments.Enchantment enchant, int level) {
            return enchant.equals(this.enchant) && expression.isSelect(level);
        }

        public String asString() {
            return enchant.getName() + " " + expression.translate();
        }

        @Override
        public String toString() {
            return "Point{" +
                    "enchant=" + enchant +
                    ", expression=" + expression +
                    '}';
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public String asString() {
        return points.stream().map(Point::asString).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Enchantment{" +
                "points=" + points +
                '}';
    }
}
