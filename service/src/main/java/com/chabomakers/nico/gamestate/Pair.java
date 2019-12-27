package com.chabomakers.nico.gamestate;

public class Pair<K, V> {

  public final K key;
  public final V value;

  public static <K, V> Pair<K, V> create(K k, V v) {
    return new Pair<>(k, v);
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  private Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }
}
