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
