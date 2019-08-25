package ink.ptms.cronus.internal.program;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.program.effect.EffectParser;
import ink.ptms.cronus.uranus.program.effect.Effect;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-26 22:29
 */
public class QuestEffect {

    private List<Effect> effect = Lists.newArrayList();

    public QuestEffect(List<String> source) {
        source.stream().map(EffectParser::parse).forEach(e -> effect.add(e));
    }

    public void eval(Player player) {
        new NoneProgram(player).eval(effect);
    }

    public void eval(Player player, DataQuest dataQuest) {
        new QuestProgram(player, dataQuest).eval(effect);
    }

    public List<Effect> getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return "QuestEffect{" +
                "effect=" + effect +
                '}';
    }
}
