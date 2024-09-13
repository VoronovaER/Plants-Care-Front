package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.me.test1.MainActivity;
import com.me.test1.R;

public class PlantTypeCardFragment extends Fragment {

    private Button btnFrag1;;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_type_card, container, false);

        btnFrag1 = view.findViewById(R.id.btnFrag1);

        btnFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment2();
                Toast.makeText(requireContext(), "Кнопка нажалась",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}