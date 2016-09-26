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
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class EhcacheShiroManager implements CacheManager, Initializable, Destroyable {

  private static final Logger log = LoggerFactory.getLogger(EhcacheShiroManager.class);

  private org.ehcache.CacheManager manager;

  public <K, V> Cache<K, V> getCache(String name) throws CacheException {

    if (log.isTraceEnabled()) {
      log.trace("Acquiring EhcacheShiro instance named [" + name + "]");
    }

    org.ehcache.Cache<Object, Object> cache = ensureCacheManager().getCache(name, Object.class, Object.class);

    if (cache == null) {
      if (log.isInfoEnabled()) {
        log.info("Cache with name " + name + " does not yet exist.  Creating now.");
      }
      cache = createCache(name);

      if (log.isInfoEnabled()) {
        log.info("Added EhcacheShiro named [" + name + "]");
      }
    } else {
      if (log.isInfoEnabled()) {
        log.info("Using existing EhcacheShiro named [" + name + "]");
      }
    }

    return new EhcacheShiro<K, V>(cache);
  }

  private org.ehcache.Cache<Object, Object> createCache(String name) {
    XmlConfiguration xmlConfiguration = createConfiguration();
    try {
      CacheConfigurationBuilder<Object, Object> configurationBuilder = xmlConfiguration.newCacheConfigurationBuilderFromTemplate(
              "defaultCacheConfiguration", Object.class, Object.class);
      CacheConfiguration<Object, Object> cacheConfiguration = configurationBuilder.build();
      return ensureCacheManager().createCache(name, cacheConfiguration);
    } catch (InstantiationException e) {
      throw new CacheException(e);
    } catch (IllegalAccessException e) {
      throw new CacheException(e);
    } catch (ClassNotFoundException e) {
      throw new CacheException(e);
    }
  }

  private org.ehcache.CacheManager ensureCacheManager() {
    if (manager == null) {
      manager = CacheManagerBuilder.newCacheManager(createConfiguration());
      manager.init();
    }

    return manager;
  }

  private URL getResource() {
    final String fullPath = "/org/ehcache/integrations/shiro/ehcache.xml";
    return EhcacheShiroManager.class.getClass().getResource(fullPath);
  }

  private XmlConfiguration createConfiguration() {
    return new XmlConfiguration(getResource());
  }

  public void destroy() throws Exception {
    if (manager != null) {
      manager.close();
      manager = null;
    }
  }

  public void init() throws ShiroException {
    ensureCacheManager();
  }
}
