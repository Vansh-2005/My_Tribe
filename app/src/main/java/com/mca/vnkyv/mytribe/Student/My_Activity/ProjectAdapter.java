package com.mca.vnkyv.mytribe.Student.My_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mca.vnkyv.mytribe.R;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final Context context;
    private final List<Project> projectList;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList.clear();
        this.projectList.addAll(projectList);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);

        Glide.with(context)
                .load(project.getImageUri())
                .placeholder(R.drawable.profile_icon) // Add a placeholder image
                .error(R.drawable.profile_icon) // Add an error image
                .into(holder.projectImageView);

        holder.titleTextView.setText(project.getTitle());
        holder.creatorName.setText(project.getCreatorName());
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        ImageView projectImageView;
        TextView titleTextView;
        TextView creatorName;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            projectImageView = itemView.findViewById(R.id.communityImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            creatorName = itemView.findViewById(R.id.creatorNameTextView);
        }
    }
}
