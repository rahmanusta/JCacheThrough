package com.kodcu;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.spi.CachingProvider;

/**
 * Created by usta on 15.09.2014.
 */
public class App {

    public static void main(String[] args) throws InterruptedException {

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        MutableConfiguration config = new MutableConfiguration();
        config.setTypes(Long.class, Book.class);
        config.setWriteThrough(true);
        config.setReadThrough(true);
        config.setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

        config.setCacheWriterFactory(()->new BackupWriter());
        config.setCacheLoaderFactory(BackupReader::new);

        Cache<Long, Book> kodcu = cacheManager.createCache("kodcu", config);
        kodcu.put(123444L, new Book(123444L, "Java ve Yazılım Tasarımı", 35));
        kodcu.put(123445L, new Book(123445L, "Java Mimarisiyle Kurumsal Çözümler", 25));
        kodcu.put(123446L, new Book(123446L, "Java ve WebSocket", 30));

        Book b1 = kodcu.get(123445L); // in Cache
        Book b2 = kodcu.get(123447L); // not in Cache but in DB
        Book b3 = kodcu.get(123999L); // neither in Cache nor DB

    }
}
