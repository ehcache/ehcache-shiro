package org.ehcache.integrations.shiro;

import org.apache.shiro.cache.Cache;

import java.util.AbstractCollection;
import java.util.Collection;

abstract class EhcacheCollectionWrapper<E> extends AbstractCollection<E> {

  private final Cache shiroCache;

  private final org.ehcache.Cache ehcacheCache;

  EhcacheCollectionWrapper(Cache shiroCache, org.ehcache.Cache ehcacheCache) {
    this.shiroCache = shiroCache;
    this.ehcacheCache = ehcacheCache;
  }

  public int size() {
    return shiroCache.size();
  }

  @Override
  public boolean isEmpty() {
    return !ehcacheCache.iterator().hasNext();
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException("addAll");
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("remove");
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("removeAll");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("retainAll");
  }
}
