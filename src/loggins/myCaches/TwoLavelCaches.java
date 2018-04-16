package loggins.myCaches;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class TwoLavelCaches<KeyType, ValueType extends Serializable> extends Object implements LeveledCacheble<KeyType, ValueType> {

    RamCache<KeyType, ValueType> ramCache;
    MemoryCacheClass<KeyType, ValueType> memoryCache;
    int maxRamCacheCapacity;
    int numberOfRequests;
    int numberOfRequestForRecech;

    public TwoLavelCaches(int maxRamCacheCapacity, int numberOfRequestForRecech) {
        this.maxRamCacheCapacity = maxRamCacheCapacity;
        this.numberOfRequestForRecech = numberOfRequestForRecech;
        ramCache = new RamCache<KeyType, ValueType>();
        memoryCache = new MemoryCacheClass<KeyType, ValueType>();
        numberOfRequests = 0;
    }

    @Override
    public void reCache() throws IOException, ClassNotFoundException {
        TreeSet<KeyType> ramKeyTypeTreeSet = new TreeSet<>(ramCache.getMostFrequentlyUsedKeys());
        int boundFrequency = 0;

        for (KeyType key : ramKeyTypeTreeSet) {
            boundFrequency += ramCache.getFrequencyOfCallingObject(key);
        }
        boundFrequency /= ramKeyTypeTreeSet.size();

        for (KeyType key : ramKeyTypeTreeSet) {
            if (ramCache.getFrequencyOfCallingObject(key) <= boundFrequency) {
                memoryCache.cache(key, ramCache.removeObject(key));
            }
        }
        TreeSet<KeyType> memoryKeyTypeTreeSet = new TreeSet<>(memoryCache.getMostFrequentlyUsedKeys());
        for (KeyType key : memoryKeyTypeTreeSet) {
            try {
                if (memoryCache.getFrequencyOfCallingObject(key) > boundFrequency) {
                    ramCache.cache(key, memoryCache.removeObject(key));
                }

            } catch (IOException ex) {
                memoryCache.deleteObject(key);
                continue;
            } catch (ClassNotFoundException ex) {
                continue;
            }
        }

    }

    @Override
    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        ramCache.cache(keyType, valueType);

    }

    @Override
    public ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if (ramCache.containsKey(keyType)) {
            numberOfRequests++;
            if (numberOfRequests > numberOfRequestForRecech) {
                this.reCache();
                numberOfRequests = 0;
            }
            return ramCache.getObject(keyType);
        }
        if (memoryCache.containsKey(keyType)) {
            numberOfRequests++;
            if (numberOfRequests > numberOfRequestForRecech) {
                this.reCache();
                numberOfRequests = 0;
            }
            return memoryCache.getObject(keyType);
        }
        return null;

    }

    @Override
    public void deleteObject(KeyType keyType) {
        if (ramCache.containsKey(keyType)) {
            ramCache.deleteObject(keyType);
        }
        if (memoryCache.containsKey(keyType)) {
            memoryCache.deleteObject(keyType);
        }
    }

    @Override
    public void cleanCache() {
        memoryCache.cleanCache();
        ramCache.cleanCache();
    }

    @Override
    public ValueType removeObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if (ramCache.containsKey(keyType)) {
            return ramCache.removeObject(keyType);
        }
        if (memoryCache.containsKey(keyType)) {
            return memoryCache.removeObject(keyType);
        }
        return null;
    }

    @Override
    public boolean containsKey(KeyType keyType) {
        if (ramCache.containsKey(keyType)) {
            return true;
        }
        if (memoryCache.containsKey(keyType)) {
            return true;
        }
        return false;
    }

    @Override
    public long size() {
        return ramCache.size() + memoryCache.size();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        TreeSet<KeyType> set = new TreeSet<>(ramCache.getMostFrequentlyUsedKeys());
        set.addAll(memoryCache.getMostFrequentlyUsedKeys());

        return set;
    }

    @Override
    public int getFrequencyOfCallingObject(KeyType keyType) {
        if (ramCache.containsKey(keyType)) {
            return ramCache.getFrequencyOfCallingObject(keyType);
        }
        if (memoryCache.containsKey(keyType)) {
            return memoryCache.getFrequencyOfCallingObject(keyType);
        }
        return 0;

    }
}
