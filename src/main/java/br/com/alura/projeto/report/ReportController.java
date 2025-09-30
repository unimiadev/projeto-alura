package br.com.alura.projeto.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/admin/reports")
    public String reportsPage(Model model) {
        List<CourseAccessReportDTO> topCourses = reportService.getMostAccessedCoursesReport(10);
        List<CourseAccessReportDTO> completionReport = reportService.getCourseCompletionReport();
        
        model.addAttribute("topCourses", topCourses);
        model.addAttribute("completionReport", completionReport);
        
        return "admin/reports/dashboard";
    }

    @GetMapping("/admin/reports/most-accessed")
    public String mostAccessedCoursesPage(@RequestParam(defaultValue = "10") int limit, Model model) {
        List<CourseAccessReportDTO> courses = reportService.getMostAccessedCoursesReport(limit);
        model.addAttribute("courses", courses);
        model.addAttribute("reportTitle", "Cursos Mais Acessados");
        return "admin/reports/course-list";
    }

    @GetMapping("/admin/reports/completion")
    public String courseCompletionPage(Model model) {
        List<CourseAccessReportDTO> courses = reportService.getCourseCompletionReport();
        model.addAttribute("courses", courses);
        model.addAttribute("reportTitle", "Taxa de Conclusão dos Cursos");
        return "admin/reports/course-list";
    }

    @GetMapping("/admin/reports/category/{categoryCode}")
    public String coursesByCategoryPage(@PathVariable String categoryCode, Model model) {
        List<CourseAccessReportDTO> courses = reportService.getCoursesByCategory(categoryCode);
        model.addAttribute("courses", courses);
        model.addAttribute("reportTitle", "Cursos da Categoria: " + categoryCode);
        return "admin/reports/course-list";
    }

    @GetMapping("/api/reports/most-accessed")
    @ResponseBody
    public ResponseEntity<List<CourseAccessReportDTO>> getMostAccessedCourses(
            @RequestParam(defaultValue = "10") int limit) {
        List<CourseAccessReportDTO> courses = reportService.getMostAccessedCoursesReport(limit);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/api/reports/completion")
    @ResponseBody
    public ResponseEntity<List<CourseAccessReportDTO>> getCourseCompletionReport() {
        List<CourseAccessReportDTO> courses = reportService.getCourseCompletionReport();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/api/reports/category/{categoryCode}")
    @ResponseBody
    public ResponseEntity<List<CourseAccessReportDTO>> getCoursesByCategory(@PathVariable String categoryCode) {
        List<CourseAccessReportDTO> courses = reportService.getCoursesByCategory(categoryCode);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/api/reports/summary")
    @ResponseBody
    public ResponseEntity<ReportSummaryDTO> getReportSummary() {
        List<CourseAccessReportDTO> allCourses = reportService.getMostAccessedCoursesReport(Integer.MAX_VALUE);
        
        long totalCourses = allCourses.size();
        long totalEnrollments = allCourses.stream().mapToLong(CourseAccessReportDTO::getTotalEnrollments).sum();
        long totalActiveEnrollments = allCourses.stream().mapToLong(CourseAccessReportDTO::getActiveEnrollments).sum();
        long totalCompletedEnrollments = allCourses.stream().mapToLong(CourseAccessReportDTO::getCompletedEnrollments).sum();
        
        double overallCompletionRate = totalEnrollments > 0 ? 
            (totalCompletedEnrollments * 100.0) / totalEnrollments : 0.0;

        ReportSummaryDTO summary = new ReportSummaryDTO(
            totalCourses, totalEnrollments, totalActiveEnrollments, 
            totalCompletedEnrollments, overallCompletionRate
        );

        return ResponseEntity.ok(summary);
    }

    public static class ReportSummaryDTO {
        private long totalCourses;
        private long totalEnrollments;
        private long totalActiveEnrollments;
        private long totalCompletedEnrollments;
        private double overallCompletionRate;

        public ReportSummaryDTO(long totalCourses, long totalEnrollments, 
                               long totalActiveEnrollments, long totalCompletedEnrollments, 
                               double overallCompletionRate) {
            this.totalCourses = totalCourses;
            this.totalEnrollments = totalEnrollments;
            this.totalActiveEnrollments = totalActiveEnrollments;
            this.totalCompletedEnrollments = totalCompletedEnrollments;
            this.overallCompletionRate = overallCompletionRate;
        }

        public long getTotalCourses() { return totalCourses; }
        public long getTotalEnrollments() { return totalEnrollments; }
        public long getTotalActiveEnrollments() { return totalActiveEnrollments; }
        public long getTotalCompletedEnrollments() { return totalCompletedEnrollments; }
        public double getOverallCompletionRate() { return overallCompletionRate; }
    }
}
