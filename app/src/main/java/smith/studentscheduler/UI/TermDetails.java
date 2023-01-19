package smith.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import smith.studentscheduler.entities.Course;
import smith.studentscheduler.entities.Term;

public class TermDetails extends AppCompatActivity {
    EditText editName;
    EditText editStart;
    EditText editEnd;
    DatePickerDialog.OnDateSetListener start;
    DatePickerDialog.OnDateSetListener end;
    final Calendar myCalendar = Calendar.getInstance();
    String name;
    String startDate;
    String endDate;
    int id;
    Term term;
    Term currentTerm;
    int numCourses;
    Repository repository;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        editName = findViewById(R.id.termname);
        editStart = findViewById(R.id.termstart);
        editEnd = findViewById(R.id.termend);
        editStart.setText(sdf.format(new Date()));
        editEnd.setText(sdf.format(new Date()));
        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");
        editName.setText(name);
        editStart.setText(startDate);
        editEnd.setText(endDate);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.courserecycleview);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for (Course c : repository.getAllCourses()) {
            if (c.getTermId() == id) filteredCourses.add(c);
        }
        courseAdapter.setCourses(filteredCourses);
        Button button = findViewById(R.id.saveterm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == -1) {
                    term = new Term(0, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                    repository.insert(term);
                } else {
                    term = new Term(id, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                    repository.update(term);
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
                new DatePickerDialog(TermDetails.this, start, myCalendar
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
                new DatePickerDialog(TermDetails.this, end, myCalendar
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
        };
        end = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                intent.putExtra("termId", id);
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

//        name = getIntent().getStringExtra("name");
//        startDate = getIntent().getStringExtra("start");
//        endDate = getIntent().getStringExtra("end");
        editName.setText(name);
        editStart.setText(startDate);
        editEnd.setText(endDate);

        // Update the UI fields with the correct data
//        editName.setText(term.getTermName());
//        editStart.setText(term.getStartDate());
//        editEnd.setText(term.getEndDate());
        RecyclerView recyclerView = findViewById(R.id.courserecycleview);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for (Course p : repository.getAllCourses()) {
            if (p.getTermId() == id) filteredCourses.add(p);
        }
        courseAdapter.setCourses(filteredCourses);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deletecourse, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteterm:

                for (Term term : repository.getAllTerms()) {
                    if (term.getTermId() == id) currentTerm = term;
                }
                numCourses = 0;
                for (Course course : repository.getAllCourses()) {
                    if (course.getTermId() == id) ++numCourses;
                }
                if (numCourses == 0) {
                    repository.delete(currentTerm);
                    Toast.makeText(TermDetails.this, currentTerm.getTermName() + " was deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TermDetails.this, "Can't delete a term with courses", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}