package Requests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SharedMap<K extends Object,V extends Object> implements Map {
    private Map<Object, Object> map;


    public SharedMap() {
        this.map = new HashMap<>();
    }

    public SharedMap(Map<Object, Object> map) {
        this.map = map;
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public synchronized boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public synchronized Object get(Object o) {
        return map.get(o);
    }

    @Override
    public synchronized Object put(Object o, Object o2) {
        return map.put(o,o2);
    }

    @Override
    public synchronized V remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }
}
