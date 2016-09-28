package org.ehcache.integrations.shiro;

import java.util.Iterator;

abstract class EhcacheIterator<K, V, T> implements Iterator<T> {

  private final Iterator<org.ehcache.Cache.Entry<K, V>> cacheIterator;

  EhcacheIterator(Iterator<org.ehcache.Cache.Entry<K, V>> cacheIterator) {
    this.cacheIterator = cacheIterator;
  }

  public boolean hasNext() {
    return cacheIterator.hasNext();
  }

  public T next() {
    return getNext(cacheIterator);
  }

  public void remove() {
    throw new UnsupportedOperationException("remove");
  }

  abstract protected T getNext(Iterator<org.ehcache.Cache.Entry<K, V>> cacheIterator);
}
