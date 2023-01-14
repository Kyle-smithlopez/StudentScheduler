package smith.studentscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import smith.studentscheduler.R;
import smith.studentscheduler.entities.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private final TextView courseItemView1;
        private final TextView courseItemView2;


        private CourseViewHolder(View itemview) {
            super(itemview);
            courseItemView = itemview.findViewById(R.id.textViewcoursename);
            courseItemView1 = itemview.findViewById(R.id.textViewcoursestart);
            courseItemView2 = itemview.findViewById(R.id.textViewcourseend);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("id", current.getCourseId());
                    intent.putExtra("name", current.getCourseName());
                    intent.putExtra("start", current.getStartDate());
                    intent.putExtra("end", current.getEndDate());
                    intent.putExtra("status", current.getStatus());
                    intent.putExtra("ciName", current.getCiName());
                    intent.putExtra("ciPhone", current.getCiPhone());
                    intent.putExtra("ciEmail", current.getCiEmail());
                    intent.putExtra("termId", current.getTermId());
                    context.startActivity(intent);
                }
            });
        }

    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseAdapter.CourseViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course current = mCourses.get(position);
            String name = current.getCourseName();
            String start = current.getStartDate();
            String end = current.getEndDate();
            holder.courseItemView.setText(name);
            holder.courseItemView1.setText(start);
            holder.courseItemView2.setText(end);

        } else {
            holder.courseItemView.setText("No Course Name");
        }
    }


    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }
}
