package ink.ptms.cronus.internal.program.function.hook;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.TabooLibAPI;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionWorldGuard extends Function {

    @Override
    public String getName() {
        return "worldguard";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            List<String> regions = TabooLibAPI.getPluginBridge().worldguardGetRegion(player.getWorld(), player.getLocation());
            switch (args[0].toLowerCase()) {
                case "name":
                    return String.join(",", regions);
                case "name.up":
                case "name.top":
                    return regions.isEmpty() ? "null" : regions.get(0);
                case "name.down":
                case "name.bottom":
                    return regions.isEmpty() ? "null" : regions.get(regions.size() - 1);
            }
        }
        return "<Non-Quest>";
    }
}
