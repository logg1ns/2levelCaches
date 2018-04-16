package loggins.myCaches;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public class MemoryCacheClass<KeyType, ValueType extends Serializable> implements Cacheble<KeyType, ValueType>, FrequencyCallObject<KeyType> {

    HashMap<KeyType, String> hashMap;
    TreeMap<KeyType, Integer> frequencyMap;

    public MemoryCacheClass() {
        hashMap = new HashMap<KeyType, String>();
        frequencyMap = new TreeMap<KeyType, Integer>();

        File tempFolder = new File("temp\\");
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
    }


    @Override

    public void cache(KeyType keyType, ValueType valueType) throws IOException, ClassNotFoundException {
        String pathToObject;
        pathToObject = "temp\\" + UUID.randomUUID().toString() + ".temp";
        frequencyMap.put(keyType, 1);
        hashMap.put(keyType, pathToObject);

        FileOutputStream fileStream = new FileOutputStream(pathToObject);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(valueType);
        objectStream.flush();
        objectStream.close();
        fileStream.flush();
        fileStream.close();
    }

    @Override
    public ValueType getObject(KeyType keyType) throws IOException, ClassNotFoundException {
        if (hashMap.containsKey(keyType)) {
            String pathOfObject = hashMap.get(keyType);

            FileInputStream fileInputStream = new FileInputStream(pathOfObject);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ValueType deserializationObject = (ValueType) objectInputStream.readObject();
            int frequency = frequencyMap.remove(keyType);
            frequencyMap.put(keyType, ++frequency);

            fileInputStream.close();
            objectInputStream.close();
            return deserializationObject;
        }

        return null;
    }

    @Override
    public void deleteObject(KeyType keyType) {
        if (hashMap.containsKey(keyType)) {
            File deletingFile = new File(hashMap.remove(keyType));
            frequencyMap.remove(keyType);
            deletingFile.delete();
        }
        hashMap.clear();
        frequencyMap.clear();
    }

    @Override
    public void cleanCache() {
        for (KeyType keyType : hashMap.keySet()) {
            File deletingFile = new File(hashMap.get(keyType));
            deletingFile.delete();
        }
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
        TreeMap<KeyType, Integer> sorted = new TreeMap(comprator);
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
