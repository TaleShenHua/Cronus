package ink.ptms.cronus.database.impl;

import com.google.common.collect.Maps;
import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.db.local.Local;
import io.izzel.taboolib.module.db.local.LocalPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public class DatabaseYAML extends Database {

    @Override
    public void init() {
    }

    @Override
    public void cancel() {
    }

    @Override
    protected void upload0(Player player, DataPlayer dataPlayer) {
        LocalPlayer.get(player).set("Cronus.data", dataPlayer.writeBase64());
    }

    @Override
    protected DataPlayer download0(Player player) {
        DataPlayer dataPlayer = new DataPlayer(player);
        FileConfiguration playerData = LocalPlayer.get(player);
        if (playerData.contains("Cronus.data")) {
            dataPlayer.readBase64(playerData.getString("Cronus.data"));
        }
        return dataPlayer;
    }

    @Override
    protected void setGV0(String key, String value) {
        Local.get().get("data").set("global-variable.key", value);
    }

    @Override
    protected String getGV0(String key) {
        return Local.get().get("data").getString("global-variable.key");
    }

    @Override
    protected Map<String, String> getGV0() {
        Map<String, String> map = Maps.newHashMap();
        ConfigurationSection section = Local.get().get("data").getConfigurationSection("global-variable");
        if (section == null) {
            return map;
        }
        section.getValues(false).forEach((k, v) -> map.put(k, String.valueOf(v)));
        return map;
    }
}
