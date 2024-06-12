package eg.com.vodafone.web.mvc.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.stereotype.Component;

import static eg.com.vodafone.web.mvc.util.CacheName.*;

/**
 * @author Radwa Osama
 * @since 4/7/13
 */
@Component
public class CacheService {

  private static final CacheManager cacheManager = CacheManager.getInstance();

  public CacheService() {
      if(cacheManager.getCache(TEXT_FILE_CACHE.toString()) != null){
          return ;
      }
    CacheConfiguration cacheConfiguration =
      new CacheConfiguration()
        .name(TEXT_FILE_CACHE.toString())
        .maxElementsInMemory(100)
        .maxElementsOnDisk(1000)
        .overflowToDisk(true)
        .eternal(false)
        .timeToLiveSeconds(1800)
        .timeToIdleSeconds(1800);

    Cache cache = new Cache(cacheConfiguration);
    cacheManager.addCache(cache);
  }

  public boolean add(CacheName cacheName, Object key, Object value) {

    Cache cache = cacheManager.getCache(cacheName.toString());

    if (cache != null) {
      Element element = new Element(key, value);
      cache.put(element);

      return true;
    }

    return false;
  }

  public Object get(CacheName cacheName, Object key) {

    Cache cache = cacheManager.getCache(cacheName.toString());

    Element element = cache.get(key);

    if (element != null) {
      return element.getValue();
    }

    return null;
  }

}
