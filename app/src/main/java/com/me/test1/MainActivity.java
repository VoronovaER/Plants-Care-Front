package com.me.test1;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.me.test1.databinding.ActivityMainBinding;
import com.me.test1.dto.PlantTypeListRecordDTO;
import com.me.test1.network.PlantTypeApi;
import com.me.test1.ui.dashboard.PlantTypeCardFragment;
import com.me.test1.ui.dashboard.PlantTypeListFragment;
import com.me.test1.ui.home.FloristPlantsFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void replaceFragment1(PlantTypeListRecordDTO plantType){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        PlantTypeCardFragment mFrag = new PlantTypeCardFragment(plantType);
        t.replace(R.id.frame_layout, mFrag);
        t.commit();
    }

    public void replaceFragment2(){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        PlantTypeListFragment mFrag = new PlantTypeListFragment();
        t.replace(R.id.frame_layout, mFrag);
        t.commit();
    }

    public void replaceFragmentHome(){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        FloristPlantsFragment mFrag = new FloristPlantsFragment();
        t.replace(R.id.home_frame_layout, mFrag);
        t.commit();
    }


}