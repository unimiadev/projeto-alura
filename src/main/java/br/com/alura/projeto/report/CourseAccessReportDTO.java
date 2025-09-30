package br.com.alura.projeto.report;

public class CourseAccessReportDTO {

    private String courseName;
    private String courseCode;
    private String categoryName;
    private String instructorEmail;
    private Long totalEnrollments;
    private Long activeEnrollments;
    private Long completedEnrollments;
    private Double completionRate;

    @Deprecated
    public CourseAccessReportDTO() {}

    public CourseAccessReportDTO(String courseName, String courseCode, String categoryName, 
                                String instructorEmail, Long totalEnrollments, 
                                Long activeEnrollments, Long completedEnrollments) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.categoryName = categoryName;
        this.instructorEmail = instructorEmail;
        this.totalEnrollments = totalEnrollments;
        this.activeEnrollments = activeEnrollments;
        this.completedEnrollments = completedEnrollments;
        this.completionRate = totalEnrollments > 0 ? 
            (completedEnrollments.doubleValue() / totalEnrollments.doubleValue()) * 100 : 0.0;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public Long getTotalEnrollments() {
        return totalEnrollments;
    }

    public Long getActiveEnrollments() {
        return activeEnrollments;
    }

    public Long getCompletedEnrollments() {
        return completedEnrollments;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public void setTotalEnrollments(Long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public void setActiveEnrollments(Long activeEnrollments) {
        this.activeEnrollments = activeEnrollments;
    }

    public void setCompletedEnrollments(Long completedEnrollments) {
        this.completedEnrollments = completedEnrollments;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }
}
