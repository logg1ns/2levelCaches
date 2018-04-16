package loggins.myCaches;

import java.util.Set;

public interface FrequencyCallObject<KeyType> {

    Set<KeyType> getMostFrequentlyUsedKeys();

    int getFrequencyOfCallingObject(KeyType keyType);

}
