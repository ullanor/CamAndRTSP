package com.e.camrtsp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    final private int settingsId = 1;
    final private int liveId = 2;
    LinearLayout MainOptionsLayout;
    TextView TextCamera;
    TextView textVersion;

    //App Timer---+
    Timer timer;
    TimerTask timerTask;
    int time = 0;
    boolean timerHasStarted = false;
    String timerInfo;

    private void StartTimer(){
        if(timerHasStarted)return;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerInfo = "Time: "+time;
                        textVersion.setText(timerInfo);
                        if(time > 30){ //timeout!
                            TextCamera.setText("Cannot load stream!");
                            StopTimer();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,1000,1000);
        timerHasStarted = true;
    }

    private void StopTimer(){
        if(!timerHasStarted)return;
        textVersion.setText("");
        timerTask.cancel();
        time = 0;
        timerHasStarted = false;
        timer = null;
    }
    //---+

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cameraBtn = findViewById(R.id.btnCamera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(AppManager.CUR_FRAGMENT != liveId)ChangeAppFragment(liveId);}
        });

        ImageButton settingsBtn = findViewById(R.id.btnSettings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(AppManager.CUR_FRAGMENT != settingsId) ChangeAppFragment(settingsId);}});

        textVersion = findViewById(R.id.textVersion);
        String textVer = "v"+BuildConfig.VERSION_NAME;
        String buildVer = "\nabi: "+ Build.SUPPORTED_ABIS[0];
        textVersion.setText(textVer+buildVer);

        TextCamera = findViewById(R.id.textCamera);
        //check orientation
        MainOptionsLayout = findViewById(R.id.MainOptionsLayout);
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == 1)MainOptionsLayout.setVisibility(View.VISIBLE); else MainOptionsLayout.setVisibility(View.GONE);

        if(AppManager.FIRST_START){ChangeAppFragment(settingsId); AppManager.FIRST_START = false;}
        else ChangeAppFragment(AppManager.CUR_FRAGMENT);//keep previous fragment!
    }

    private void ChangeAppFragment(int id){
        Fragment fragment = null;
        if(id == settingsId) {StopTimer(); fragment = new Settings();}
        else if(id == liveId) {StartTimer(); fragment = new LiveView();}
        TextCamera.setText(AppManager.CAM_DEF);

        AppManager.CUR_FRAGMENT = id;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FragmentsFrame, fragment, null);
        ft.commit();
    }

    //from cam fragment
    public void ToggleLoading(boolean loading){
        if(loading) TextCamera.setText(AppManager.CAM_LOAD);
        else {
            TextCamera.setText(AppManager.CAM_DONE);
            StopTimer(); //Stream was loaded!
        }
    }
}
