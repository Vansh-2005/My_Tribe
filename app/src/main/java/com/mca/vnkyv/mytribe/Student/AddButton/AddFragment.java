package com.mca.vnkyv.mytribe.Student.AddButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.AddButton.Community.Add_Community_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Event.Add_Event_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Project.Add_Project_Activity;

public class AddFragment extends Fragment {

 private Button add_Community_btn,add_event_btn,add_project_btn;

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new, container, false);

         ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

         ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2c2f49"));
         actionBar.setBackgroundDrawable(colorDrawable);
         actionBar.setTitle("New Project");
         actionBar.setDisplayHomeAsUpEnabled(true);
         actionBar.setDisplayShowHomeEnabled(true);
         setHasOptionsMenu(true);


         Window window = getActivity().getWindow();
         window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
         window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.profile_notificationbar_color));

      add_Community_btn=view.findViewById(R.id.add_Community_btn);
         add_event_btn=view.findViewById(R.id.add_event_btn);
         add_project_btn=view.findViewById(R.id.add_Project_btn);

      add_Community_btn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
        Intent i = new Intent(getActivity(), Add_Community_Activity.class);
        startActivity(i);
       }
      });

         add_event_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(getActivity(), Add_Event_Activity.class);
                 startActivity(i);
             }
         });
         add_project_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(getActivity(), Add_Project_Activity.class);
                 startActivity(i);
             }
         });
         return view;
     }
}