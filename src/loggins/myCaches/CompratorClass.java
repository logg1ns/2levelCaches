package loggins.myCaches;

import java.util.Comparator;
import java.util.Map;

   public class CompratorClass implements Comparator {
    Map base;

    public CompratorClass(Map base) {
        this.base = base;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if ((Integer) base.get(o1) < (Integer) base.get(o2)) {
            return 1;
        } else if (base.get(o2) == base.get(o2)) {

            return 1;
        }
        return -1;
    }
}
