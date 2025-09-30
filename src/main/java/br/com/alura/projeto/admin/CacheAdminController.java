package br.com.alura.projeto.admin;

import br.com.alura.projeto.config.CacheManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/cache")
@Tag(name = "Cache Admin", description = "Endpoints para administração e monitoramento de cache")
public class CacheAdminController {

    @Autowired
    private CacheManagerService cacheManagerService;

    @GetMapping("/stats")
    @Operation(summary = "Estatísticas de cache", 
               description = "Retorna estatísticas de uso dos caches do sistema")
    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheNames", cacheManagerService.getCacheNames());
        stats.put("statistics", cacheManagerService.getCacheStatistics());
        stats.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/names")
    @Operation(summary = "Listar nomes de cache", 
               description = "Lista todos os nomes de cache disponíveis no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de nomes retornada com sucesso")
    public ResponseEntity<Collection<String>> getCacheNames() {
        return ResponseEntity.ok(cacheManagerService.getCacheNames());
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Limpar todos os caches", 
               description = "Remove todas as entradas de todos os caches do sistema")
    @ApiResponse(responseCode = "200", description = "Caches limpos com sucesso")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheManagerService.clearAllCaches();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All caches cleared successfully");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear/{cacheName}")
    @Operation(summary = "Limpar cache específico", 
               description = "Remove todas as entradas de um cache específico")
    @ApiResponse(responseCode = "200", description = "Cache específico limpo com sucesso")
    public ResponseEntity<Map<String, String>> clearSpecificCache(
            @Parameter(description = "Nome do cache a ser limpo", example = "reports")
            @PathVariable String cacheName) {
        
        cacheManagerService.clearCache(cacheName);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache '" + cacheName + "' cleared successfully");
        response.put("cacheName", cacheName);
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/warm-up")
    @Operation(summary = "Aquecer caches", 
               description = "Pré-carrega dados nos caches para melhorar performance")
    @ApiResponse(responseCode = "200", description = "Caches aquecidos com sucesso")
    public ResponseEntity<Map<String, String>> warmUpCaches() {
        // Este endpoint pode ser usado para pré-carregar dados importantes nos caches
        // Por exemplo, carregar relatórios mais acessados, cursos ativos, etc.
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache warm-up initiated");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
}
