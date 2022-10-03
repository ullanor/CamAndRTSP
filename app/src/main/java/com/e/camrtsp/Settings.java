package com.e.camrtsp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Fragment {
    //public Settings() { }
    TextView TextUrl, TextCache;
    Button BtnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        BtnSave = settingsView.findViewById(R.id.btnSave);
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSettings();
            }
        });
        TextUrl = settingsView.findViewById(R.id.inputRTSPurl);
        TextCache = settingsView.findViewById(R.id.inputRTSPcache);
        LoadSettings();

        return settingsView;
    }

    private void LoadSettings(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedUrl = sharedPref.getString(AppManager.URL_KEY, "rtsp://IP:port");
        int savedNetCache = sharedPref.getInt(AppManager.NET_CACHE_KEY,600);

        TextUrl.setText(savedUrl);
        TextCache.setText(Integer.toString(savedNetCache));
    }

    private void SaveSettings(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AppManager.URL_KEY,TextUrl.getText().toString());
        editor.putInt(AppManager.NET_CACHE_KEY,Integer.parseInt(TextCache.getText().toString()));
        editor.apply();

        Toast.makeText(getContext(),"Saved!",Toast.LENGTH_SHORT).show();
    }
}
