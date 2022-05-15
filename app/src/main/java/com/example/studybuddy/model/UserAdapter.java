package com.example.studybuddy.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybuddy.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.email.setText(user.getEmail());
        holder.gender.setText(user.getGender());
        holder.year.setText(user.getYear());
        holder.faculty.setText(user.getFaculty());

        Glide.with(context).load(user.getProfilePicUrl()).into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userProfileImage;
        public TextView name, email, gender, year, faculty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.display_image);
            name = itemView.findViewById(R.id.display_name);
            email = itemView.findViewById(R.id.display_email);
            gender = itemView.findViewById(R.id.display_gender);
            year = itemView.findViewById(R.id.display_year);
            faculty = itemView.findViewById(R.id.display_faculty);
        }
    }
}
