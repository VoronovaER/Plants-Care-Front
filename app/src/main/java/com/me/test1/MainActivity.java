package com.me.test1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.me.test1.ui.dashboard.PlantRegistrationFragment;
import com.me.test1.ui.dashboard.PlantTypeCardFragment;
import com.me.test1.ui.dashboard.PlantTypeListFragment;
import com.me.test1.ui.home.EditFloristInfoFragment;
import com.me.test1.ui.home.EditFloristPlantFragment;
import com.me.test1.ui.home.FloristPlantInfoFragment;
import com.me.test1.ui.home.FloristPlantsFragment;
import com.me.test1.ui.notifications.DateNotificationFragment;
import com.me.test1.ui.notifications.TaskRegistrationFragment;

import java.time.LocalDate;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    PlantTypeApi plantTypeApi;
    private static final String TAG = "MyFirebaseMsgService";
    private static final String TAG_LAUNCH = "Launch";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.e(TAG_LAUNCH, "PERMISSION_GRANTED");
                } else {
                    Toast.makeText(this, "Приложение не будет отправлять напоминания", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, Authorization.class);
            startActivity(intent);
        } else {
            Info.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            setContentView(R.layout.activity_main);
            plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
            askNotificationPermission();

            BottomNavigationView navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupWithNavController(navView, navController);
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.i(TAG, token);
                        updateToken(token);
                    });
        }
    }

    public void updateToken(String token){
        plantTypeApi.updateFloristFirebaseToken(token, Info.getEmail()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                Log.d(TAG, "token updated");
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Log.d(TAG, "token not updated");
            }
        });
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

    public void replaceFragmentRegistration(PlantTypeListRecordDTO plantType){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        PlantRegistrationFragment mFrag = new PlantRegistrationFragment(plantType);
        t.replace(R.id.frame_layout, mFrag);
        t.commit();
    }

    public void replaceFragmentPlantCard(PlantListRecordDTO plant, Long id){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        FloristPlantInfoFragment mFrag = new FloristPlantInfoFragment(plant, id);
        t.replace(R.id.home_frame_layout, mFrag);
        t.commit();
    }

    public void replaceFragmentEditFloristInfo() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        EditFloristInfoFragment mFrag = new EditFloristInfoFragment();
        t.replace(R.id.home_frame_layout, mFrag);
        t.commit();
    }

    public void replaceDateNotificationsFragment() {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.notification_frame_layout, new DateNotificationFragment());
        t.commit();
    }

    public void replaceEditFloristPlantFragment(PlantDTO plant) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.home_frame_layout, new EditFloristPlantFragment(plant));
        t.commit();
    }

    public void replaceTaskRegistrationFragment(LocalDate date){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        TaskRegistrationFragment mFrag = new TaskRegistrationFragment(date);
        t.replace(R.id.notification_frame_layout, mFrag);
        t.commit();
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG_LAUNCH, "PERMISSION_GRANTED");
            }else {
                Log.e(TAG_LAUNCH, "NO_PERMISSION");
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}