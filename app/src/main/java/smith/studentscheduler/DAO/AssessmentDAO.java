package smith.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import smith.studentscheduler.entities.Assessment;
@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM ASSESSMENTS ORDER BY assessmentId ASC")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM Assessments WHERE courseId= :courseId ORDER BY assessmentId ASC")
    List<Assessment> getAllAssociatedAssessments(int courseId);
}
