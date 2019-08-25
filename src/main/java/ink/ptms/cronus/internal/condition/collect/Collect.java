package ink.ptms.cronus.internal.condition.collect;

import com.google.common.collect.Maps;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.inject.TFunction;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 12:40
 */
public abstract class Collect extends Condition implements ConfigurationSerializable {

    protected List<Condition> condition;
    protected Map<String, Object> map;

    public Collect(List<Condition> condition, Map<String, Object> map) {
        this.condition = condition;
        this.map = map;
    }

    @Override
    public void init(Matcher matcher, String text) {
    }

    @Override
    public Map<String, Object> serialize() {
        return Maps.newHashMap(map);
    }

    public List<Condition> getCondition() {
        return condition;
    }

    @TFunction.Init
    static void registerSerializable() {
        ConfigurationSerialization.registerClass(CollectA.class, "ALL_MATCH");
        ConfigurationSerialization.registerClass(CollectO.class, "ANY_MATCH");
    }

    @TFunction.Cancel
    static void unregisterSerializable() {
        ConfigurationSerialization.unregisterClass(CollectA.class);
        ConfigurationSerialization.unregisterClass(CollectO.class);
    }
}
