package smith.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import smith.studentscheduler.Database.Repository;
import smith.studentscheduler.R;
import smith.studentscheduler.entities.Term;

public class TermDetails extends AppCompatActivity {
    EditText editName;
    EditText editStart;
    EditText editEnd;
    String name;
    String start;
    String end;
    int id;
    Term term;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        editName = findViewById(R.id.termname);
        editStart = findViewById(R.id.termstart);
        editEnd = findViewById(R.id.termend);
        id=getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        editName.setText(name);
        editStart.setText(start);
        editEnd.setText(end);
        repository=new Repository(getApplication());
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
}