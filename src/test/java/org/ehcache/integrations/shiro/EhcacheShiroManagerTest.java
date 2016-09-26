package org.ehcache.integrations.shiro;

import org.apache.shiro.cache.Cache;
import org.junit.Assert;
import org.junit.Test;

public class EhcacheShiroManagerTest {

  @Test
  public void testGetCache() throws Exception {
    EhcacheShiroManager cacheManager = new EhcacheShiroManager();

    try {
      Cache<Object, Object> someCache = cacheManager.getCache("someCache");
      Assert.assertNotNull(someCache);

      final String key = "key";
      final String value = "value";
      Assert.assertNull(someCache.put(key, value));
      Assert.assertEquals(value, someCache.get(key));
    } finally {
      cacheManager.destroy();
    }
  }
}
