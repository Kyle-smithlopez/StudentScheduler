package smith.studentscheduler.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;

    private String courseName;
    private String startDate;
    private String endDate;
    private String status;
    private String ciName;
    private String ciPhone;
    private String ciEmail;
    private String note;
    private int termId;

    public Course(int courseId, String courseName, String startDate, String endDate, String status, String ciName, String ciPhone, String ciEmail, String note, int termId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.ciName = ciName;
        this.ciPhone = ciPhone;
        this.ciEmail = ciEmail;
        this.termId = termId;
        this.note = note;
    }

    public Course() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCiName() {
        return ciName;
    }

    public void setCiName(String ciName) {
        this.ciName = ciName;
    }

    public String getCiPhone() {
        return ciPhone;
    }

    public void setCiPhone(String ciPhone) {
        this.ciPhone = ciPhone;
    }

    public String getCiEmail() {
        return ciEmail;
    }

    public void setCiEmail(String ciEmail) {
        this.ciEmail = ciEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
