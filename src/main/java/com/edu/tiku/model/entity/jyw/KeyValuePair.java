package com.edu.tiku.model.entity.jyw;

public class KeyValuePair<K, V>
{
	public K Key;
    public V Value;

    public KeyValuePair(K key, V value)
    {
        this.Key = key;
        this.Value = value;
    }
}
