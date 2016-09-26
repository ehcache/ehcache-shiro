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
import java.util.HashSet;
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
    if (log.isTraceEnabled()) {
      trace("Getting object", k);
    }

    if (k == null) {
      return null;
    }

    V value = cache.get(k);
    if (value == null) {
      if (log.isTraceEnabled()) {
        log.trace("Element for [" + k + "] is null.");
      }
    }

    return value;
  }

  public V put(K k, V v) throws CacheException {
    if (log.isTraceEnabled()) {
      trace("Putting object", k);
    }

    V previousValue = get(k);
    cache.put(k, v);
    return previousValue;
  }

  public V remove(K k) throws CacheException {
    if (log.isTraceEnabled()) {
      trace("Removing object", k);
    }

    V previousValue = get(k);
    cache.remove(k);
    return previousValue;
  }

  public void clear() throws CacheException {
    if (log.isTraceEnabled()) {
      log.trace("Clearing all objects from cache [" + cache + "]");
    }

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
    Iterator<org.ehcache.Cache.Entry<K, V>> iterator = cache.iterator();
    final Set<K> keys = new HashSet<K>();
    while (iterator.hasNext()) {
      org.ehcache.Cache.Entry<K, V> entry = iterator.next();
      keys.add(entry.getKey());
    }

    return keys;
  }

  public Collection<V> values() {
    Iterator<org.ehcache.Cache.Entry<K, V>> iterator = cache.iterator();
    final Set<V> values = new HashSet<V>();
    while (iterator.hasNext()) {
      org.ehcache.Cache.Entry<K, V> entry = iterator.next();
      values.add(entry.getValue());
    }

    return values;
  }

  private void trace(String operation, K k) {
    log.trace(operation + " using cache [" + cache + "] for key [" + k + "]");
  }
}
