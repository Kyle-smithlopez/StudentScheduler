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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    String name;
//    String start;
//    String end;
    int id;
    Term term;
    Term currentTerm;
    int numCourses;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        editName = findViewById(R.id.termname);
        editStart = findViewById(R.id.termstart);
        editEnd = findViewById(R.id.termend);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStart.setText(sdf.format(new Date()));
        editEnd.setText(sdf.format(new Date()));
        id=getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
//        start = getIntent().getStringExtra("start");
//        end = getIntent().getStringExtra("end");
        editName.setText(name);


//        editStart.setText(start);
//        editEnd.setText(end);
        repository=new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.courserecycleview);
        repository = new Repository(getApplication());
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for (Course c : repository.getAllCourses()) {
            if(c.getCourseId() == id) filteredCourses.add(c);
        }
        courseAdapter.setCourses(filteredCourses);
        Button button = findViewById(R.id.saveterm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==-1){
                    term= new Term(0, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                    repository.insert(term);
//                    Toast.makeText(this, "Product is saved", Toast.LENGTH_LONG).show();
                }
                else{
                    term= new Term(id, editName.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                    repository.update(term);
//                    Toast.makeText(this, "Product is updated", Toast.LENGTH_LONG).show();

                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.courserecycleview);
        final CourseAdapter partAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(partAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for (Course p : repository.getAllCourses()) {
            if (p.getTermId() == id) filteredCourses.add(p);
        }
        partAdapter.setCourses(filteredCourses);
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
                    Toast.makeText(TermDetails.this, "Can't delete a product with parts", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}