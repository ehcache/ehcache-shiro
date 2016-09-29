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
  public void testDoubleInit() throws Exception {
    ehcacheShiroManager.init();
    CacheManager firstCacheManager = ehcacheShiroManager.getCacheManager();
    Assert.assertNotNull(firstCacheManager);

    ehcacheShiroManager.setCacheManagerConfigFile("notValidPath");
    ehcacheShiroManager.init();
    CacheManager secondCacheManager = ehcacheShiroManager.getCacheManager();
    Assert.assertNotNull(secondCacheManager);

    Assert.assertEquals(firstCacheManager, secondCacheManager);
    Assert.assertSame(firstCacheManager, secondCacheManager);

    ehcacheShiroManager.destroy();
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
    Assert.assertEquals(Status.AVAILABLE, secondCacheManager.getStatus());
  }
}
