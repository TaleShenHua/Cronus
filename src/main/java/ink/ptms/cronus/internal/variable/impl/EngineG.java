package ink.ptms.cronus.internal.variable.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.internal.variable.VariableEngine;
import ink.ptms.cronus.internal.variable.VariableResult;
import ink.ptms.cronus.internal.variable.VariableType;
import ink.ptms.cronus.util.Strumber;
import ink.ptms.cronus.util.Utils;
import org.bukkit.util.NumberConversions;

/**
 * @Author sky
 * @Since 2019-08-17 18:40
 */
public class EngineG extends VariableEngine {

    private Database database = Cronus.getCronusService().getDatabase();

    @Override
    public void reset(String key) {
        database.setGlobalVariable(key, null);
    }

    @Override
    public boolean modify(String key, Object value, VariableType type) {
        database.setGlobalVariable(key, String.valueOf(value));
        return true;
    }

    @Override
    public boolean increase(String key, Object value, VariableType type) {
        String current = database.getGlobalVariable(key);
        if (current == null) {
            modify(key, value, type);
            return true;
        }
        Strumber sumber = new Strumber(current);
        if (sumber.isNumber()) {
            if (type.isNumber()) {
                database.setGlobalVariable(key, String.valueOf(Utils.parseInt(NumberConversions.toDouble(current) + NumberConversions.toDouble(value))));
            } else {
                database.setGlobalVariable(key, current + value);
            }
        } else {
            database.setGlobalVariable(key, current + value);
        }
        return false;
    }

    @Override
    public boolean decrease(String key, Object value, VariableType type) {
        String current = database.getGlobalVariable(key);
        if (current == null) {
            return true;
        }
        Strumber sumber = new Strumber(current);
        if (sumber.isNumber() && type.isNumber()) {
            database.setGlobalVariable(key, String.valueOf(Utils.parseInt(NumberConversions.toDouble(current) - NumberConversions.toDouble(value))));
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
