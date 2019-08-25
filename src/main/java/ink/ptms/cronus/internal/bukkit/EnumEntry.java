package ink.ptms.cronus.internal.bukkit;

import com.google.common.base.Enums;
import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-20 14:27
 */
public abstract class EnumEntry<E extends Enum<E>> {

    @TInject
    protected static TLogger logger;
    protected List<E> data = Lists.newArrayList();

    public EnumEntry(String in) {
        for (String state : in.split("[,;]")) {
            Object entry = Enums.getIfPresent(origin(), state.toUpperCase()).orNull();
            if (entry != null) {
                data.add((E) entry);
            } else {
                logger.error(origin().getSimpleName() + " \"" + state + "\" parsing failed.");
            }
        }
    }

    public boolean isSelect(E element) {
        return data.contains(element);
    }

    public List<E> getData() {
        return data;
    }

    abstract public Class origin();
}
