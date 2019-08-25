package ink.ptms.cronus.internal.variable.impl;

import ink.ptms.cronus.internal.variable.VariableEngine;
import ink.ptms.cronus.internal.variable.VariableResult;
import ink.ptms.cronus.internal.variable.VariableType;
import ink.ptms.cronus.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.NumberConversions;

import java.util.List;

/**
 * @Author sky
 * @Since 2019-08-17 18:40
 */
public class EngineY extends VariableEngine {

    private YamlConfiguration yaml;

    public EngineY(YamlConfiguration yaml) {
        this.yaml = yaml;
    }

    @Override
    public void reset(String key) {
        yaml.set(key, null);
    }

    @Override
    public boolean modify(String key, Object value, VariableType type) {
        switch (type) {
            case INT:
                yaml.set(key, NumberConversions.toLong(value));
                return true;
            case DOUBLE:
                yaml.set(key, NumberConversions.toDouble(value));
                return true;
            default:
                yaml.set(key, value);
                return true;
        }
    }

    @Override
    public boolean increase(String key, Object value, VariableType type) {
        Object current = yaml.get(key);
        if (current == null) {
            modify(key, value, type);
            return true;
        }
        if (current instanceof Number) {
            if (type.isNumber()) {
                yaml.set(key, Utils.parseInt(NumberConversions.toDouble(current) + NumberConversions.toDouble(value)));
            } else {
                yaml.set(key, current + String.valueOf(value));
            }
        } else if (current instanceof List) {
            yaml.set(key, add((List) current, value));
        } else {
            yaml.set(key, current + String.valueOf(value));
        }
        return true;
    }

    @Override
    public boolean decrease(String key, Object value, VariableType type) {
        Object current = yaml.get(key);
        if (current == null) {
            return true;
        }
        if (current instanceof Number && type.isNumber()) {
            yaml.set(key, Utils.parseInt(NumberConversions.toDouble(current) - NumberConversions.toDouble(value)));
        } else if (current instanceof List) {
            yaml.set(key, remove((List) current, value));
        } else {
            return false;
        }
        return true;
    }

    @Override
    public VariableResult select(String key) {
        return null;
    }
}
