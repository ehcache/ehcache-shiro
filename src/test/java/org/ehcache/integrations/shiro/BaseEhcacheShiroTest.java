package org.ehcache.integrations.shiro;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.After;
import org.junit.Before;

public class BaseEhcacheShiroTest {

  protected CacheManager cacheManager;

  protected Cache<Long, String> basicCache;

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
}
