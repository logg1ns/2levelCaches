package loggins.myCaches;

import java.util.Set;

public interface FrequencyCallObject<KeyType> {

    Set<KeyType> getMostFrequencyUsedObject();

    int getFrequencyOfCallingObject(KeyType keyType);

}
