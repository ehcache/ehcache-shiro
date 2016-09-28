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
