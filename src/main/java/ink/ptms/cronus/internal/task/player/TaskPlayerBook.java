package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_book")
public class TaskPlayerBook extends Countable<PlayerEditBookEvent> {

    private String title;
    private String content;
    private Sxpression page;

    public TaskPlayerBook(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        title = data.containsKey("title") ? String.valueOf(data.get("title")) : null;
        content = data.containsKey("content") ? String.valueOf(data.get("content")) : null;
        page = data.containsKey("page") ? new Sxpression(data.get("page")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerEditBookEvent e) {
        return (title == null || e.getNewBookMeta().getTitle().contains(title)) && (content == null || String.join("", e.getNewBookMeta().getPages()).contains(content)) && (page == null || page.isSelect(e.getNewBookMeta().getPageCount()));
    }

    @Override
    public String toString() {
        return "TaskPlayerBook{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", page=" + page +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
