package com.mca.vnkyv.mytribe.Student.My_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Add the Glide library for image loading
import com.mca.vnkyv.mytribe.R; // Adjust the package name as per your project

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private final Context context;
    private final List<Community> communityList;

    public CommunityAdapter(Context context, List<Community> communityList) {
        this.context = context;
        this.communityList = communityList;
    }
    // Add this method to set the communityList
    public void setCommunityList(List<Community> communityList) {
        this.communityList.clear();
        this.communityList.addAll(communityList);
    }
    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        Community community = communityList.get(position);

        // Load image using Glide (ensure to add the Glide dependency to your app's build.gradle)
        Glide.with(context)
                .load(community.getImageUri())
                .placeholder(R.drawable.profile_icon) // Add a placeholder image
                .error(R.drawable.profile_icon) // Add an error image
                .into(holder.communityImageView);

        holder.titleTextView.setText(community.getTitle());
        holder.creatorNameTextView.setText(community.getCreatorName());
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    static class CommunityViewHolder extends RecyclerView.ViewHolder {
        ImageView communityImageView;
        TextView titleTextView;
        TextView creatorNameTextView;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            communityImageView = itemView.findViewById(R.id.communityImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            creatorNameTextView = itemView.findViewById(R.id.creatorNameTextView);
        }
    }
}

