package ink.ptms.cronus.internal.variable;

import java.util.List;

/**
 * @Author sky
 * @Since 2019-08-17 18:26
 */
public abstract class VariableEngine {

    abstract public void reset(String key);

    abstract public boolean modify(String key, Object value, VariableType type);

    abstract public boolean increase(String key, Object value, VariableType type);

    abstract public boolean decrease(String key, Object value, VariableType type);

    abstract public VariableResult select(String key);

    protected List add(List list, Object element) {
        list.add(element);
        return list;
    }

    protected List remove(List list, Object element) {
        list.remove(element);
        return list;
    }

}
