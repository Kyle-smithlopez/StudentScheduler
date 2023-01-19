package smith.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.EntityDeletionOrUpdateAdapter;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import smith.studentscheduler.Database.Repository;
import smith.studentscheduler.R;
import smith.studentscheduler.entities.Assessment;
import smith.studentscheduler.entities.Course;
import smith.studentscheduler.entities.Term;

public class CourseDetails extends AppCompatActivity {

    EditText editName;
    EditText editStart;
    EditText editEnd;
    EditText editStatus;
    EditText editNote;
    EditText editCiName;
    EditText editCiEmail;
    EditText editCiPhone;
    DatePickerDialog.OnDateSetListener start;
    DatePickerDialog.OnDateSetListener end;
    final Calendar myCalendar = Calendar.getInstance();
    String name;
    String status;
    String startDate;
    String endDate;
    String ciName;
    String ciEmail;
    String ciPhone;
    String note;
    int id;
    int termId;
    Course course;
    Course currentCourse;
    Repository repository;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    String[] options = {"Completed", "In Progress", "Dropped", "Planned to take"};
    private boolean spinnerSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        editName = findViewById(R.id.coursename);
        editStart = findViewById(R.id.coursestart);
        editEnd = findViewById(R.id.courseend);
        editCiName = findViewById(R.id.ciname);
        editCiPhone = findViewById(R.id.ciphone);
        editCiEmail = findViewById(R.id.ciemail);
        editNote = findViewById(R.id.editnote);
        editStart.setText(sdf.format(new Date()));
        editEnd.setText(sdf.format(new Date()));
        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");
        ciName = getIntent().getStringExtra("ciName");
        ciPhone = getIntent().getStringExtra("ciPhone");
        ciEmail = getIntent().getStringExtra("ciEmail");
        termId = getIntent().getIntExtra("termId", -1);
        status = getIntent().getStringExtra("status");
        note = getIntent().getStringExtra("note");
        editName.setText(name);
        editCiName.setText(ciName);
        editCiPhone.setText(ciPhone);
        editCiEmail.setText(ciEmail);
        editStart.setText(startDate);
        editEnd.setText(endDate);
        editNote.setText(note);
        repository = new Repository(getApplication());

        RecyclerView recyclerView = findViewById(R.id.assessmentrecyclerview);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessment = new ArrayList<>();
        for (Assessment p : repository.getAllAssessments()) {
            if (p.getCourseId() == id) filteredAssessment.add(p);
        }
        assessmentAdapter.setAssessments(filteredAssessment);

        Spinner spinner1 = findViewById(R.id.progresspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(adapter.getPosition(status));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter<Term> termArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllTerms());
//        spinner.setAdapter(termArrayAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    editNote.setText(termArrayAdapter.getItem(i).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                editNote.setText("");
//            }
//        });


        Button button = findViewById(R.id.savecourse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == -1) {
                    course = new Course(0, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), status, editCiName.getText().toString(), editCiPhone.getText().toString(), editCiEmail.getText().toString(), editNote.getText().toString(), termId);
                    repository.insert(course);
                } else {
                    course = new Course(id, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), status, editCiName.getText().toString(), editCiPhone.getText().toString(), editCiEmail.getText().toString(), editNote.getText().toString(), termId);
                    repository.update(course);
                }
                finish();
            }
        });

        editStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date date;
                String info = editStart.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetails.this, start, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date date;
                String info = editEnd.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetails.this, end, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        start = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        }

        ;

        end = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        }

        ;

        FloatingActionButton fab = findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                intent.putExtra("courseId", id);
                startActivity(intent);
            }
        });
    }

    private void updateLabelStart() {
        editStart.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEnd() {
        editEnd.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.assessmentrecyclerview);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessment = new ArrayList<>();
        for (Assessment p : repository.getAllAssessments()) {
            if (p.getCourseId() == id) filteredAssessment.add(p);
        }
        assessmentAdapter.setAssessments(filteredAssessment);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Course Notes:");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.notifystart:
                String dateFromScreen = editStart.getText().toString();
                String nameFromScreen = editName.getText().toString();
                Date myStartDate = null;
                try {
                    myStartDate = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myStartDate.getTime();
                Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("key", nameFromScreen + " is starting " + dateFromScreen);
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyend:
                String dateFromScreen2 = editEnd.getText().toString();
                String nameFromScreen2 = editName.getText().toString();
                Date myEndDate = null;
                try {
                    myEndDate = sdf.parse(dateFromScreen2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger2 = myEndDate.getTime();
                Intent intent2 = new Intent(CourseDetails.this, MyReceiver.class);
                intent2.putExtra("key", nameFromScreen2 + " is ending " + dateFromScreen2);
                PendingIntent sender2 = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent2, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);
                return true;
            case R.id.deletecourse:

                for (Course course : repository.getAllCourses()) {
                    if (course.getCourseId() == id) currentCourse = course;
                }
                repository.delete(currentCourse);
                Toast.makeText(CourseDetails.this, currentCourse.getCourseName() + " was deleted", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}