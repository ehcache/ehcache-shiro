/**
 * Copyright Terracotta, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehcache.integrations.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EhcacheShiro<K, V> implements Cache<K, V> {

  private static final Logger log = LoggerFactory.getLogger(EhcacheShiro.class);

  private final org.ehcache.Cache<K, V> cache;

  public EhcacheShiro(org.ehcache.Cache cache) {
    if (cache == null) {
      throw new IllegalArgumentException("Cache argument cannot be null.");
    }

    this.cache = cache;
  }

  public V get(K k) throws CacheException {
    trace("Getting object", k);

    if (k == null) {
      return null;
    }

    V value = cache.get(k);
    if (value == null) {
      log.trace("Element for [{}] is null.", k);
    }

    return value;
  }

  public V put(K k, V v) throws CacheException {
    trace("Putting object", k);

    V previousValue = null;

    while (true) {
      previousValue = cache.get(k);
      if (previousValue == null) {
        if (cache.putIfAbsent(k, v) == null) {
          break;
        }
      } else {
        if (cache.replace(k, v) != null) {
          break;
        }
      }
    }

    return previousValue;
  }

  public V remove(K k) throws CacheException {
    trace("Removing object", k);

    V previousValue = null;

    while (true) {
      previousValue = cache.get(k);
      if (previousValue == null) {
        break;
      } else {
        if (cache.remove(k, previousValue)) {
          break;
        }
      }
    }

    return previousValue;
  }

  public void clear() throws CacheException {
    log.trace("Clearing all objects from cache [" + cache + "]");
    cache.clear();
  }

  public int size() {
    Iterator<org.ehcache.Cache.Entry<K, V>> iterator = cache.iterator();
    int size = 0;
    while (iterator.hasNext()) {
      iterator.next();
      size++;
    }

    return size;
  }

  public Set<K> keys() {
    return new EhcacheSetWrapper<K>(this, cache) {
      @Override
      public Iterator<K> iterator() {
        return new EhcacheIterator<K, V, K>(cache.iterator()) {

          protected K getNext(Iterator<org.ehcache.Cache.Entry<K, V>> cacheIterator) {
            return cacheIterator.next().getKey();
          }
        };
      }
    };
  }

  public Collection<V> values() {
    return new EhcacheCollectionWrapper<V>(this, cache) {
      @Override
      public Iterator<V> iterator() {
        return new EhcacheIterator<K, V, V>(cache.iterator()) {
          protected V getNext(Iterator<org.ehcache.Cache.Entry<K, V>> cacheIterator) {
            return cacheIterator.next().getValue();
          }
        };
      }
    };
  }

  private void trace(String operation, K k) {
    log.trace("{} using cache [{}] for key [{}]", operation, cache, k);
  }
}
