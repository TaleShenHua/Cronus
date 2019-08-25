package ink.ptms.cronus.database.impl;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.db.source.DBSource;
import io.izzel.taboolib.module.db.sql.SQLColumn;
import io.izzel.taboolib.module.db.sql.SQLColumnType;
import io.izzel.taboolib.module.db.sql.SQLHost;
import io.izzel.taboolib.module.db.sql.SQLTable;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public class DatabaseSQL extends Database {

    private SQLHost host;
    private SQLTable table;
    private SQLTable tableVar;
    private DataSource dataSource;
    private boolean uniqueId;

    @Override
    public void init() {
        host = new SQLHost(Cronus.getConf().getConfigurationSection("Database"), Cronus.getInst());
        table = new SQLTable(Cronus.getConf().getStringColored("Database.table"), SQLColumn.PRIMARY_KEY_ID, new SQLColumn(SQLColumnType.TEXT, "player"), new SQLColumn(SQLColumnType.LONGTEXT, "data"));
        tableVar = new SQLTable(Cronus.getConf().getStringColored("Database.table") + "_variable", SQLColumn.PRIMARY_KEY_ID, new SQLColumn(SQLColumnType.TEXT, "key"), new SQLColumn(SQLColumnType.LONGTEXT, "value"));
        try {
            dataSource = DBSource.create(host);
            table.executeUpdate(table.createQuery()).dataSource(dataSource).run();
            tableVar.executeUpdate(tableVar.createQuery()).dataSource(dataSource).run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        uniqueId = Cronus.getConf().getBoolean("Database.uniqueId");
    }

    @Override
    public void cancel() {
        DBSource.closeDataSource(host);
    }

    @Override
    protected void upload0(Player player, DataPlayer dataPlayer) {
        if (isExists(player)) {
            table.executeUpdate("data = ?", "player = ?")
                    .dataSource(dataSource)
                    .statement(s -> {
                        s.setString(1, dataPlayer.writeBase64());
                        s.setString(2, toName(player));
                    }).run();
        } else {
            table.executeInsert("null, ?, ?")
                    .dataSource(dataSource)
                    .statement(s -> {
                        s.setString(1, toName(player));
                        s.setString(2, dataPlayer.writeBase64());
                    }).run();
        }
    }

    @Override
    protected DataPlayer download0(Player player) {
        DataPlayer dataPlayer = new DataPlayer(player);
        table.executeSelect("player = ?")
                .dataSource(dataSource)
                .statement(s -> s.setString(1, toName(player)))
                .resultNext(r -> dataPlayer.readBase64(r.getString("data")))
                .run();
        return dataPlayer;
    }

    @Override
    protected void setGV0(String key, String value) {
        if (value == null) {
            tableVar.executeQuery("delete from " + tableVar.getTableName() + " where key = ?")
                    .dataSource(dataSource)
                    .statement(s -> s.setString(1, key)).run();
        } else {
            tableVar.executeQuery("insert into " + tableVar.getTableName() + " values(null, ?, ?) on duplicate key update value = ?")
                    .dataSource(dataSource)
                    .statement(s -> {
                        s.setString(1, key);
                        s.setString(2, value);
                        s.setString(3, value);
                    }).run();
        }
    }

    @Override
    protected String getGV0(String key) {
        return tableVar.executeSelect("key = ?")
                .dataSource(dataSource)
                .statement(s -> s.setString(1, key))
                .resultNext(r -> r.getString("value"))
                .run(null, "");
    }

    @Override
    protected Map<String, String> getGV0() {
        Map<String, String> map = Maps.newHashMap();
        tableVar.executeSelect()
                .dataSource(dataSource)
                .resultAutoNext(r -> map.put(r.getString("key"), r.getString("value"))).run();
        return map;
    }

    public boolean isExists(Player player) {
        return table.executeSelect("player = ?")
                .dataSource(dataSource)
                .statement(s -> s.setString(1, toName(player)))
                .resultNext(r -> true)
                .run(false, false);
    }

    public String toName(Player player) {
        return uniqueId ? player.getUniqueId().toString() : player.getName();
    }
}
