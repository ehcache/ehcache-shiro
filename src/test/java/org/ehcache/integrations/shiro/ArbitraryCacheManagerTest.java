package org.ehcache.integrations.shiro;

import org.apache.shiro.ShiroException;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ArbitraryCacheManagerTest extends BaseEhcacheShiroTest {

  private EhcacheShiroManager ehcacheShiroManager;

  @Before
  public void setUp() {
    super.setUp();
    ehcacheShiroManager = new EhcacheShiroManager();
  }

  @Test
  public void testDefaultCacheManager() throws Exception {
    ehcacheShiroManager.init();
    CacheManager cacheManager = ehcacheShiroManager.getCacheManager();

    Assert.assertNotNull(cacheManager);
    Assert.assertEquals(Status.AVAILABLE, cacheManager.getStatus());

    ehcacheShiroManager.destroy();
    Assert.assertEquals(Status.UNINITIALIZED, cacheManager.getStatus());
  }

  @Test(expected = ShiroException.class)
  public void testDefaultCacheManagerBadConfigFile() {
    final String badConfiguration = "someStrangeValue";
    ehcacheShiroManager.setCacheManagerConfigFile(badConfiguration);
    Assert.assertEquals(badConfiguration, ehcacheShiroManager.getCacheManagerConfigFile());

    ehcacheShiroManager.init();
  }

  @Test
  public void testSetCacheManager() {
    ehcacheShiroManager.setCacheManager(cacheManager);
    ehcacheShiroManager.init();

    Assert.assertEquals(cacheManager, ehcacheShiroManager.getCacheManager());
    Assert.assertSame(cacheManager, ehcacheShiroManager.getCacheManager());
  }

  /**
   * Test showing issues:
   * <p>
   * https://github.com/ehcache/ehcache-shiro/issues/12
   * https://github.com/ehcache/ehcache-shiro/issues/13
   *
   * @throws Exception
   */
  @Test
  @Ignore
  public void testArbitraryCacheManager() throws Exception {
    ehcacheShiroManager.init();
    CacheManager firstCacheManager = ehcacheShiroManager.getCacheManager();
    Assert.assertNotNull(firstCacheManager);

    ehcacheShiroManager.setCacheManager(cacheManager);
    CacheManager secondCacheManager = ehcacheShiroManager.getCacheManager();
    Assert.assertNotSame(firstCacheManager, secondCacheManager);
    Assert.assertNotEquals(firstCacheManager, secondCacheManager);

    ehcacheShiroManager.destroy();

    Assert.assertEquals(Status.UNINITIALIZED, firstCacheManager.getStatus());
    Assert.assertEquals(Status.AVAILABLE, secondCacheManager);
  }
}
