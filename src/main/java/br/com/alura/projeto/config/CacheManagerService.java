package br.com.alura.projeto.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * Serviço centralizado para gerenciamento de cache
 * Coordena a invalidação de caches entre diferentes serviços
 */
@Service
public class CacheManagerService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * Limpa todos os caches do sistema
     */
    @CacheEvict(value = {"reports", "enrollments", "courses", "users", "course-stats"}, allEntries = true)
    public void clearAllCaches() {
        // Limpa todos os caches quando necessário (ex: deploy, manutenção)
    }

    /**
     * Coordena a invalidação quando uma nova matrícula é criada
     */
    @CacheEvict(value = {"enrollments", "course-stats", "reports"}, allEntries = true)
    public void onEnrollmentCreated(String studentEmail, String courseCode) {
        // Limpa caches relacionados a matrículas e relatórios
        // Implementação simplificada para evitar dependência circular
    }

    /**
     * Coordena a invalidação quando uma matrícula é completada
     */
    @CacheEvict(value = {"enrollments", "course-stats", "reports"}, allEntries = true)
    public void onEnrollmentCompleted(String studentEmail, String courseCode) {
        // Limpa caches relacionados a matrículas e relatórios
        // Implementação simplificada para evitar dependência circular
    }

    /**
     * Coordena a invalidação quando um curso é modificado
     */
    @CacheEvict(value = {"courses", "reports"}, allEntries = true)
    public void onCourseModified(String courseCode) {
        // Limpa caches relacionados a cursos e relatórios
        // Implementação simplificada para evitar dependência circular
    }

    /**
     * Obtém estatísticas de uso do cache
     */
    public String getCacheStatistics() {
        StringBuilder stats = new StringBuilder();
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                stats.append("Cache: ").append(cacheName)
                     .append(" - Native Cache: ").append(cache.getNativeCache().getClass().getSimpleName())
                     .append("\n");
            }
        });
        
        return stats.toString();
    }

    /**
     * Limpa cache específico por nome
     */
    public void clearCache(String cacheName) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * Lista todos os nomes de cache disponíveis
     */
    public java.util.Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }
}
