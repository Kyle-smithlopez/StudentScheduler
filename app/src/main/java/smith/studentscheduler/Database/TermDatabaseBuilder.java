package smith.studentscheduler.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import smith.studentscheduler.DAO.AssessmentDAO;
import smith.studentscheduler.DAO.CourseDAO;
import smith.studentscheduler.DAO.TermDAO;
import smith.studentscheduler.entities.Assessment;
import smith.studentscheduler.entities.Course;
import smith.studentscheduler.entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 4, exportSchema = false)
public abstract class TermDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();

    public abstract CourseDAO courseDAO();

    public abstract AssessmentDAO assessmentDAO();

    private static volatile TermDatabaseBuilder INSTANCE;

    static TermDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermDatabaseBuilder.class, "TermDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
