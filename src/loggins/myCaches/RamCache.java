package loggins.myCaches;

import java.io.IOException;
import java.util.*;

public class RamCache<KeyType, ValueType> implements Cacheble<KeyType, ValueType>, FrequencyCallObject<KeyType> {
    private HashMap<KeyType, ValueType> hashMap;
    private TreeMap<KeyType, Integer> frequencyMap;


    public RamCache() {
        hashMap = new HashMap<KeyType, ValueType>();
        frequencyMap = new TreeMap<KeyType, Integer>();
    }

    @Override

    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        frequencyMap.put(keyType, 1);
        hashMap.put(keyType, valueType);
    }

    @Override
    public ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if (hashMap.containsKey(keyType)) {
            int frequency = frequencyMap.get(keyType);
            frequencyMap.put(keyType, ++frequency);
            return hashMap.get(keyType);
        }
        return null;
    }

    @Override
    public void deleteObject(KeyType keyType) {
        if (hashMap.containsKey(keyType)) {
            hashMap.remove(keyType);
            frequencyMap.remove(keyType);
        }

    }

    @Override
    public void cleanCache() {
        hashMap.clear();
        frequencyMap.clear();
    }

    @Override
    public ValueType removeObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if (hashMap.containsKey(keyType)) {
            ValueType result = this.getObject(keyType);
            this.deleteObject(keyType);
            return result;
        }
        return null;
    }

    @Override
    public boolean containsKey(KeyType keyType) {

        return hashMap.containsKey(keyType);
    }

    @Override
    public long size() {
        return hashMap.size();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        CompratorClass comprator = new CompratorClass(frequencyMap);
        TreeMap<KeyType, Integer> sorted = new TreeMap<>(comprator);
        sorted.putAll(frequencyMap);
        return sorted.keySet();
    }

    @Override
    public int getFrequencyOfCallingObject(KeyType keyType) {
        if (hashMap.containsKey(keyType)) {
            return frequencyMap.get(keyType);
        }
        return 0;
    }



}
