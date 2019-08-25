package ink.ptms.cronus.internal.task;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;

/**
 * @Author 坏黑
 * @Since 2019-05-29 22:25
 */
public interface TaskExpression {

    void eval(DataQuest dataQuest, QuestTask questTask);

}
