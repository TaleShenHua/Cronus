package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionLocationDistance extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "location.distance";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            Location location = BukkitParser.toLocation(args[0]).toBukkit();
            return location.getWorld().equals(player.getWorld()) ? location.distance(player.getLocation()) : -1;
        }
        return "<Non-Quest>";
    }
}
