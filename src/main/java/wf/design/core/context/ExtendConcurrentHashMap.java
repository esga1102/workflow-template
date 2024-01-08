package wf.design.core.context;

import java.util.concurrent.ConcurrentHashMap;

public class ExtendConcurrentHashMap<K, V>
        extends ConcurrentHashMap<K, V> {

    @Override
    public V put(K key, V value) {
        return value != null ? super.put(key, value) : null;
    }
}