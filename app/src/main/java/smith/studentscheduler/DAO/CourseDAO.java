package smith.studentscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import smith.studentscheduler.entities.Course;
@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM COURSES ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM COURSES WHERE termId= :termId ORDER BY courseId ASC")
    List<Course> getAllAssociatedCourses(int termId);
}
