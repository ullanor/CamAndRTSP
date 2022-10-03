package com.e.camrtsp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final private int settingsId = 1;
    final private int liveId = 2;
    TextView TextCamera;

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

        TextView textVersion = findViewById(R.id.textVersion);
        String textVer = "v"+BuildConfig.VERSION_NAME;
        textVersion.setText(textVer);

        TextCamera = findViewById(R.id.textCamera);
        if(AppManager.FIRST_START){ChangeAppFragment(settingsId); AppManager.FIRST_START = false;}
        else ChangeAppFragment(AppManager.CUR_FRAGMENT);//keep previous fragment!
    }

    private void ChangeAppFragment(int id){
        Fragment fragment = null;
        TextCamera.setText(AppManager.CAM_DEF);
        if(id == settingsId) {fragment = new Settings();}
        else if(id == liveId) {fragment = new LiveView();}

        AppManager.CUR_FRAGMENT = id;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FragmentsFrame, fragment, null);
        ft.commit();
    }

    public void ToggleLoading(boolean loading){
        if(loading) TextCamera.setText(AppManager.CAM_LOAD);
        else TextCamera.setText(AppManager.CAM_DONE);
    }
}
