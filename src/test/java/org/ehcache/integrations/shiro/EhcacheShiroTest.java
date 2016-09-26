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

  @Before
  public void setUp() {
    cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache("basicCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Long.class, String.class, ResourcePoolsBuilder.heap(100))).build(true);

    basicCache = cacheManager.getCache("basicCache", Long.class, String.class);
  }

  @After
  public void tearDown() {
    basicCache.clear();
    cacheManager.close();
  }

  @Test
  public void testPutAndGet() {
    EhcacheShiro<Long, String> cache = new EhcacheShiro<>(basicCache);

    final Long key = Long.valueOf(1);
    final String value = "some string value";

    Assert.assertNull(cache.get(key));
    Assert.assertNull(cache.put(key, value));
    Assert.assertEquals(value, cache.get(key));
    Assert.assertEquals(value, cache.put(key, "another value"));
  }

  @Test
  public void testSize() {
    assertSize();
  }

  @Test
  public void testClear() {
    EhcacheShiro<Long, String> cache = assertSize();
    cache.clear();
    Assert.assertEquals(0, cache.size());
  }

  private EhcacheShiro<Long, String> assertSize() {
    EhcacheShiro<Long, String> cache = new EhcacheShiro<>(basicCache);

    int count = 10;
    for (int i = 0; i < count; i++) {
      cache.put(Long.valueOf(i), "prefix-" + i);
    }

    Assert.assertEquals(10, cache.size());

    return cache;
  }

  @Test
  public void testKeys() {
    EhcacheShiro<Long, String> cache = assertSize();
    assertEquals(cache.keys(), cache);
  }

  @Test
  public void testValues() {
    EhcacheShiro<Long, String> cache = assertSize();
    assertEquals(cache.values(), cache);
  }

  private <T> void assertEquals(Collection<T> toInspect, EhcacheShiro<Long, String> cache) {
    Assert.assertEquals(toInspect.size(), cache.size());
  }

  @Test
  public void testRemove() {
    EhcacheShiro<Long, String> cache = new EhcacheShiro<>(basicCache);
    final Long key = Long.valueOf(1);
    final String value = "yet another value";
    Assert.assertNull(cache.put(key, value));
    Assert.assertEquals(value, cache.remove(key));
  }
}
