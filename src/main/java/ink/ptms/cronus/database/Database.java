package ink.ptms.cronus.database;

import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.service.Service;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public abstract class Database implements Service {

    public void upload(Player player, DataPlayer dataPlayer) {
        CronusMirror.getMirror("Database:AsyncUpload").start();
        upload0(player, dataPlayer);
        CronusMirror.getMirror("Database:AsyncUpload").stop();
    }

    public DataPlayer download(Player player) {
        CronusMirror.getMirror("Database:AsyncDownload").start();
        DataPlayer dataPlayer = download0(player);
        CronusMirror.getMirror("Database:AsyncDownload").stop();
        return dataPlayer;
    }

    public void setGlobalVariable(String key, String value) {
        CronusMirror.getMirror("Database:GlobalVariableUpload").start();
        setGV0(key, value);
        CronusMirror.getMirror("Database:GlobalVariableUpload").stop();
    }

    public String getGlobalVariable(String key) {
        CronusMirror.getMirror("Database:GlobalVariableDownload").start();
        String variable = getGV0(key);
        CronusMirror.getMirror("Database:GlobalVariableDownload").stop();
        return variable;
    }

    public Map<String, String> getGlobalVariables() {
        CronusMirror.getMirror("Database:GlobalVariableDownload").start();
        Map<String, String> variable = getGV0();
        CronusMirror.getMirror("Database:GlobalVariableDownload").stop();
        return variable;
    }

    abstract protected void upload0(Player player, DataPlayer dataPlayer);

    abstract protected DataPlayer download0(Player player);

    abstract protected void setGV0(String key, String value);

    abstract protected String getGV0(String key);

    abstract protected Map<String, String> getGV0();


}
