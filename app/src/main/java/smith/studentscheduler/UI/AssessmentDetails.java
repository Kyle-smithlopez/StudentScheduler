package smith.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smith.studentscheduler.Database.Repository;
import smith.studentscheduler.R;
import smith.studentscheduler.entities.Assessment;
import smith.studentscheduler.entities.Course;

public class AssessmentDetails extends AppCompatActivity {

    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    DatePickerDialog.OnDateSetListener start;
    DatePickerDialog.OnDateSetListener end;
    final Calendar myCalendar = Calendar.getInstance();
    String title;
    String type;
    String startDate;
    String endDate;
    int id;
    int courseId;
    Assessment assessment;
    Assessment currentAssessment;
    Repository repository;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    String[] options = {"Performance", "Objective"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        editTitle = findViewById(R.id.assessmenttitle);
        editStart = findViewById(R.id.assessmentstart);
        editEnd = findViewById(R.id.assessmentend);
        editStart.setText(sdf.format(new Date()));
        editEnd.setText(sdf.format(new Date()));
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        courseId = getIntent().getIntExtra("courseId", -1);
        type = getIntent().getStringExtra("type");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");
        editTitle.setText(title);
        editStart.setText(startDate);
        editEnd.setText(endDate);
        repository = new Repository(getApplication());

        Spinner spinner = findViewById(R.id.typespinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(type));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button button = findViewById(R.id.saveassessment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == -1) {
                    assessment = new Assessment(0, editTitle.getText().toString(), type, editStart.getText().toString(), editEnd.getText().toString(), courseId);
                    repository.insert(assessment);
                } else {
                    assessment = new Assessment(id, editTitle.getText().toString(), type, editStart.getText().toString(), editEnd.getText().toString(), courseId);
                    repository.update(assessment);
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
                new DatePickerDialog(AssessmentDetails.this, start, myCalendar
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
                new DatePickerDialog(AssessmentDetails.this, end, myCalendar
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
    }

    private void updateLabelStart() {
        editStart.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEnd() {
        editEnd.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notifystart:
                String dateFromScreen = editStart.getText().toString();
                String TitleFromScreen = editTitle.getText().toString();
                Date myStartDate = null;
                try {
                    myStartDate = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myStartDate.getTime();
                Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("key", TitleFromScreen + " started " + dateFromScreen );
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyend:
                String dateFromScreen2 = editEnd.getText().toString();
                String TitleFromScreen2 = editTitle.getText().toString();
                Date myEndDate = null;
                try {
                    myEndDate = sdf.parse(dateFromScreen2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger2 = myEndDate.getTime();
                Intent intent2 = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent2.putExtra("key", TitleFromScreen2 + " ending " + dateFromScreen2 );
                PendingIntent sender2 = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent2, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);
                return true;
            case R.id.deleteassessment:

                for (Assessment assessment : repository.getAllAssessments()) {
                    if (assessment.getAssessmentId() == id) currentAssessment = assessment;
                }
                repository.delete(currentAssessment);
                Toast.makeText(AssessmentDetails.this, currentAssessment.getAssessmentTitle() + " was deleted", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}