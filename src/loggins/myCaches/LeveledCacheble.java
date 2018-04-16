package loggins.myCaches;

import java.io.IOException;

public interface LeveledCacheble<KeyType , ValueType> extends Cacheble<KeyType, ValueType>,FrequencyCallObject<KeyType> {
    void reCache() throws IOException, ClassNotFoundException;
}
