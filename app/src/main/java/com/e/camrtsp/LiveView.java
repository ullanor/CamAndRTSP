package com.e.camrtsp;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;


public class LiveView extends Fragment implements MediaPlayer.EventListener{


    //public LiveView() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View liveView = inflater.inflate(R.layout.fragment_live_view, container, false);

        videoLayout = liveView.findViewById(R.id.VLCVideo);
        LoadCameraObjects();

        return liveView;
    }

    //set camera
    private LibVLC libVlc;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;

    private void LoadCameraObjects(){
        libVlc = new LibVLC(getContext());
        mediaPlayer = new MediaPlayer(libVlc);
        mediaPlayer.setEventListener(this);
    }

    @Override
    public void onEvent(MediaPlayer.Event event)
    {
        if (event.type == MediaPlayer.Event.Buffering) {
            if (event.getBuffering() == 100f) {
                ((MainActivity)getActivity()).ToggleLoading(false);
            } else {
                ((MainActivity)getActivity()).ToggleLoading(true);
            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mediaPlayer.attachViews(videoLayout, null, false, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedUrl = sharedPref.getString(AppManager.URL_KEY, "rtsp://IP:port");
        int savedNetCache = sharedPref.getInt(AppManager.NET_CACHE_KEY,600);

        try {
            Media media = new Media(libVlc, Uri.parse(savedUrl));
            media.setHWDecoderEnabled(true, false);
            String netCache = AppManager.NET_CACHE_STRING+savedNetCache;
            media.addOption(netCache);

            mediaPlayer.setMedia(media);
            media.release();
            Toast.makeText(getContext(),netCache,Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(),"Error: "+ex,Toast.LENGTH_LONG).show();
        }
        mediaPlayer.play();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.release();
        libVlc.release();
    }

}
