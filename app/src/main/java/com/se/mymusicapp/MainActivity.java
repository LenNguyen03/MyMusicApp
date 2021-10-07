package com.se.mymusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.se.mymusicapp.MusicService;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Iservice mIservice;
    private static SeekBar mSeekBar;
    private MusicService musicService;
    MediaPlayer mMp;
     Button btn_play_pause;

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Get the data we carry
            Bundle data = msg.getData();
            //Get the total duration and current progress of the song
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");

            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPosition);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//  requestPermissions(new String[]{Manifest.permission.INTERNET}, 100);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        btn_play_pause = (Button) findViewById(R.id.btn_play_pause);

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        bindService(intent, conn, BIND_AUTO_CREATE);
        //Setting the drag time monitor of seekbar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mIservice.callSeekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        btn_play_pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(musicService.bindService()) {
//                    mIservice.callPlayMusic();
//                    btn_play_pause.setBackgroundResource(R.drawable.ic_play);
//
//                } else{
//                    mIservice.callPauseMusic();
//                    btn_play_pause.setBackgroundResource(R.drawable.ic_pause);
//                }
//            }
//        });


    }

    public void playmusic(View view) {
        mIservice.callPlayMusic();
        btn_play_pause.setBackgroundResource(R.drawable.ic_play);
    }

    public void onPause(View view) {
        mIservice.callPauseMusic();
        btn_play_pause.setBackgroundResource(R.drawable.ic_pause);
    }

    public void stopMusci(View view) {
        mIservice.stopMusic();
    }

    public void replaymusic(View view) {
        mIservice.callRePlayMusic();
    }





    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIservice = (Iservice) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}