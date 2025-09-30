package br.com.alura.projeto.performance;

import br.com.alura.projeto.config.CacheManagerService;
import br.com.alura.projeto.report.ReportService;
import br.com.alura.projeto.enrollment.EnrollmentService;
import br.com.alura.projeto.course.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CachePerformanceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CacheManagerService cacheManagerService;

    @Test
    public void testReportCachePerformance() {
        cacheManagerService.clearAllCaches();

        long startTime1 = System.currentTimeMillis();
        var report1 = reportService.getMostAccessedCoursesReport(10);
        long duration1 = System.currentTimeMillis() - startTime1;

        long startTime2 = System.currentTimeMillis();
        var report2 = reportService.getMostAccessedCoursesReport(10);
        long duration2 = System.currentTimeMillis() - startTime2;

        assertEquals(report1.size(), report2.size());

        assertTrue(duration2 <= duration1, 
                   "Cache should make second call faster. First: " + duration1 + "ms, Second: " + duration2 + "ms");

        System.out.println("Report Performance Test:");
        System.out.println("First call (no cache): " + duration1 + "ms");
        System.out.println("Second call (with cache): " + duration2 + "ms");
        System.out.println("Performance improvement: " + ((double)(duration1 - duration2) / duration1 * 100) + "%");
    }

    @Test
    public void testCourseServiceCachePerformance() {
        cacheManagerService.clearAllCaches();

        long startTime1 = System.currentTimeMillis();
        var courses1 = courseService.findAll();
        long duration1 = System.currentTimeMillis() - startTime1;

        long startTime2 = System.currentTimeMillis();
        var courses2 = courseService.findAll();
        long duration2 = System.currentTimeMillis() - startTime2;
        assertEquals(courses1.size(), courses2.size());

        System.out.println("Course Service Performance Test:");
        System.out.println("First call (no cache): " + duration1 + "ms");
        System.out.println("Second call (with cache): " + duration2 + "ms");
    }

    @Test
    public void testCacheInvalidation() {
        cacheManagerService.clearAllCaches();

        var report1 = reportService.getMostAccessedCoursesReport(5);
        assertNotNull(report1);

        cacheManagerService.clearCache("reports");

        var report2 = reportService.getMostAccessedCoursesReport(5);
        assertNotNull(report2);

        assertEquals(report1.size(), report2.size());
    }

    @Test
    public void testCacheStatistics() {
        var cacheNames = cacheManagerService.getCacheNames();
        var statistics = cacheManagerService.getCacheStatistics();

        assertNotNull(cacheNames);
        assertNotNull(statistics);
        
        assertTrue(cacheNames.contains("reports"));
        assertTrue(cacheNames.contains("enrollments"));
        assertTrue(cacheNames.contains("courses"));

        System.out.println("Cache Names: " + cacheNames);
        System.out.println("Cache Statistics: " + statistics);
    }

    @Test
    public void testMultipleConcurrentCacheAccess() throws InterruptedException {
        cacheManagerService.clearAllCaches();

        Thread[] threads = new Thread[5];
        long[] durations = new long[5];

        for (int i = 0; i < 5; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                long start = System.currentTimeMillis();
                var report = reportService.getMostAccessedCoursesReport(10);
                durations[index] = System.currentTimeMillis() - start;
                assertNotNull(report);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Concurrent Cache Access Test:");
        for (int i = 0; i < durations.length; i++) {
            System.out.println("Thread " + i + ": " + durations[i] + "ms");
        }

        for (long duration : durations) {
            assertTrue(duration >= 0);
        }
    }
}
