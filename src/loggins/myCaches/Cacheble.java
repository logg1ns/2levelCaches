package loggins.myCaches;

import java.io.IOException;

public interface Cacheble<KeyType , ValueType> {
    void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException;

    ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException;

    void deleteObject(KeyType keyType);

    void cleanCache();

    ValueType removeObject(KeyType keyType) throws IOException, ClassNotFoundException;

    boolean containsKey(KeyType keyType);

    long size();
}
