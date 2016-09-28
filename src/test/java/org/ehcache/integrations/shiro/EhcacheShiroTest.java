package org.ehcache.integrations.shiro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class EhcacheShiroTest extends BaseEhcacheShiroTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullEhCache() {
    new EhcacheShiro<Long, String>(null);
  }


  private EhcacheShiro<Long, String> shiroCache;

  @Before
  public void setUp() {
    super.setUp();
    shiroCache = new EhcacheShiro<Long, String>(basicCache);
  }

  @Test
  public void testPutAndGet() {
    final Long key = Long.valueOf(1);
    final String value = "some string value";

    Assert.assertNull(shiroCache.get(key));
    Assert.assertNull(shiroCache.put(key, value));
    Assert.assertEquals(value, shiroCache.get(key));
    Assert.assertEquals(value, shiroCache.put(key, "another value"));
  }

  @Test
  public void testSize() {
    putElementsAndAssertSize();
  }

  @Test
  public void testClear() {
    putElementsAndAssertSize();
    shiroCache.clear();
    Assert.assertEquals(0, shiroCache.size());
  }

  private void putElementsAndAssertSize() {
    int count = 10;
    for (int i = 0; i < count; i++) {
      shiroCache.put(Long.valueOf(i), "prefix-" + i);
    }

    Assert.assertEquals(10, shiroCache.size());
  }

  @Test
  public void testKeys() {
    putElementsAndAssertSize();
    assertEquals(shiroCache.keys());
  }

  @Test
  public void testValues() {
    putElementsAndAssertSize();
    assertEquals(shiroCache.values());
  }

  private <T> void assertEquals(Collection<T> toInspect) {
    Assert.assertEquals(toInspect.size(), shiroCache.size());
  }

  @Test
  public void testRemove() {
    final Long key = Long.valueOf(1);
    final String value = "yet another value";
    Assert.assertNull(shiroCache.put(key, value));
    Assert.assertEquals(value, shiroCache.remove(key));
  }
}
