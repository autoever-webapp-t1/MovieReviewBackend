package com.movie.MovieReview.movie.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // 캐시 활성화
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        // 여러 캐시 이름을 지정
        // 이런식으로 캐시할 대상을 지정.
        return new ConcurrentMapCacheManager("tmdbCache", "movieDetailsCache", "actorCache");
    }
}
