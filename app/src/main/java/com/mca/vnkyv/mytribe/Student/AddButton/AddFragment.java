package com.mca.vnkyv.mytribe.Student.AddButton;

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
import android.widget.Toast;

import com.mca.vnkyv.mytribe.R;

public class AddFragment extends Fragment {

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

         return view;
     }
}