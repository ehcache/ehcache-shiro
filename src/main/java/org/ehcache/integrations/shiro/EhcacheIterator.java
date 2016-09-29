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
