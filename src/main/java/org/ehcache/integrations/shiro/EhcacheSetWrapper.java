package org.ehcache.integrations.shiro;

import org.apache.shiro.cache.Cache;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

abstract class EhcacheSetWrapper<E> extends AbstractSet<E> {

  private final Collection<E> delegate;

  EhcacheSetWrapper(Cache shiroCache, org.ehcache.Cache ehcacheCache) {
    delegate = new EhcacheCollectionWrapper<E>(shiroCache, ehcacheCache) {
      @Override
      public Iterator<E> iterator() {
        throw new IllegalStateException("Should not use this iterator");
      }
    };
  }

  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return delegate.addAll(c);
  }

  @Override
  public boolean remove(Object o) {
    return delegate.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return delegate.retainAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return delegate.retainAll(c);
  }
}
