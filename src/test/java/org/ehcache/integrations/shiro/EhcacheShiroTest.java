package org.ehcache.integrations.shiro;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class EhcacheShiroTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullEhCache() {
    new EhcacheShiro<Long, String>(null);
  }

  private CacheManager cacheManager;

  private Cache<Long, String> basicCache;

  private EhcacheShiro<Long, String> shiroCache;

  @Before
  public void setUp() {
    cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache("basicCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Long.class, String.class, ResourcePoolsBuilder.heap(100))).build(true);

    basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
    shiroCache = new EhcacheShiro<Long, String>(basicCache);
  }

  @After
  public void tearDown() {
    basicCache.clear();
    cacheManager.close();
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
