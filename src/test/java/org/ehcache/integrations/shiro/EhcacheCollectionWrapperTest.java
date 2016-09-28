package org.ehcache.integrations.shiro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

public class EhcacheCollectionWrapperTest extends BaseEhcacheShiroTest {

  private Collection<Long> collection;

  @Before
  public void setUp() {
    super.setUp();
    collection = new EhcacheCollectionWrapper<Long>(new EhcacheShiro(basicCache), basicCache) {
      @Override
      public Iterator<Long> iterator() {
        return null;
      }
    };
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdd() {
    collection.add(Long.valueOf(1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddAll() {
    collection.addAll(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    collection.remove(Long.valueOf(1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRemoveAll() {
    collection.removeAll(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRetainAll() {
    collection.retainAll(null);
  }

  @Test
  public void testSize() {
    assertEmpty();

    basicCache.put(Long.valueOf(1), "someValue");
    Assert.assertEquals(1, collection.size());
    Assert.assertFalse(collection.isEmpty());

    basicCache.clear();
    assertEmpty();
  }

  private void assertEmpty() {
    Assert.assertEquals(0, collection.size());
    Assert.assertTrue(collection.isEmpty());
  }
}
