package ink.ptms.cronus.internal.condition.collect;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.condition.CondNull;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-29 12:40
 */
@SerializableAs("ALL_MATCH")
public class CollectA extends Collect {

    public CollectA(List<Condition> condition, Map<String, Object> map) {
        super(condition, map);
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return condition.stream().allMatch(c -> c.check(player, quest, event));
    }

    public static Collect valueOf(Map<String, Object> map) {
        Object predicate = map.get("predicate");
        if (predicate instanceof String) {
            return new CollectA(Lists.newArrayList(ConditionParser.parse((String) predicate)), map);
        } else if (predicate instanceof List) {
            List<Condition> list = Lists.newArrayList();
            for (Object line : ((List) predicate)) {
                if (line instanceof String) {
                    list.add(ConditionParser.parse((String) line));
                } else if (line instanceof Condition) {
                    list.add((Condition) line);
                } else {
                    list.add(new CondNull(String.valueOf(line)));
                }
            }
            return new CollectA(list, map);
        }
        return new CollectA(Lists.newArrayList(new CondNull(String.valueOf(predicate))), map);
    }

    @Override
    public String toString() {
        return "CollectA{" +
                "condition=" + condition +
                ", map=" + map +
                '}';
    }
}
