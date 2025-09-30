package br.com.alura.projeto.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configuração padrão para todos os caches
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats());
        
        cacheManager.setCacheNames(java.util.Arrays.asList(
                "reports",           
                "enrollments",       
                "courses",          
                "users",            
                "course-stats"      
        ));
        
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats();
    }

    // Cache específico para relatórios (mais longo)
    @Bean
    public Caffeine<Object, Object> reportsCaffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterWrite(Duration.ofHours(1)) 
                .recordStats();
    }

    // Cache específico para dados de usuário (mais curto)
    @Bean
    public Caffeine<Object, Object> usersCaffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(200)
                .maximumSize(2000)
                .expireAfterWrite(Duration.ofMinutes(15)) 
                .recordStats();
    }
}
