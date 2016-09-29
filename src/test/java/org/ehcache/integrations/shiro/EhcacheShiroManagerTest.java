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
