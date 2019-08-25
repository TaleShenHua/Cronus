package ink.ptms.cronus.database.data;

import io.izzel.taboolib.util.serialize.TSerializerElement;

/**
 * @Author 坏黑
 * @Since 2019-05-25 10:08
 */
public class DataQuestSerializer implements TSerializerElement<DataQuest> {

    public static final DataQuestSerializer INSTANCE = new DataQuestSerializer();

    @Override
    public DataQuest read(String s) {
        return (DataQuest) new DataQuest().read(s);
    }

    @Override
    public String write(DataQuest o) {
        return o.write();
    }

    @Override
    public boolean matches(Class aClass) {
        return true;
    }
}
