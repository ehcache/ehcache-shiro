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

import org.ehcache.Cache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EhcacheIteratorTest extends BaseEhcacheShiroTest {

  private Iterator<Long> iterator;

  @Before
  public void setUp() {
    super.setUp();
    iterator = getIterator();
  }

  private Iterator<Long> getIterator() {
    return new EhcacheIterator<Long, String, Long>(basicCache.iterator()) {

      @Override
      protected Long getNext(Iterator<Cache.Entry<Long, String>> cacheIterator) {
        return cacheIterator.next().getKey();
      }
    };
  }

  @Test(expected = NoSuchElementException.class)
  public void testNextNoElements() {
    iterator.next();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    basicCache.put(Long.valueOf(1), "someValue");
    iterator.remove();
  }

  @Test
  public void testNext() {
    Assert.assertFalse(iterator.hasNext());

    final int count = 10;
    for (int i = 0; i < count; i++) {
      basicCache.put(Long.valueOf(i), "prefix-" + i);
    }

    iterator = getIterator();
    Assert.assertTrue(iterator.hasNext());

    for (int i = 0; i < count; i++) {
      final Long next = iterator.next();
      Assert.assertNotNull(next);
    }
  }
}
