package com.se.mymusicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    MediaPlayer mMp;
    Button btn_play_pause;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return new MyBinder();
    }

    @Override
    public void onCreate() {
//        mMp = new MediaPlayer();

        mMp= MediaPlayer.create(this, R.raw.sontung);
        Toast.makeText(this, "Service đã được tạo!", Toast.LENGTH_SHORT).show();
        super.onCreate();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMp.release();
    }

    public void playMusic(){
        try {

            if(mMp.isPlaying()){
                //mMp.setDataSource("");
                //mMp.prepare();
                mMp.start();
                updateSeekBar();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void pauseMusic(){
        if(mMp.isPlaying()){
            mMp.pause();

        }

    }

    public void stopMusic(){
        if(mMp.isPlaying()){
            mMp.stop();
        }

    }

    public void updateSeekBar(){
        final int duration = mMp.getDuration();
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = mMp.getCurrentPosition();

                Message msg = Message.obtain();
                Bundle bundle = new Bundle(); // map
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);

                msg.setData(bundle);
                MainActivity.mHandler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask, 100, 1000);
        //Listening music playing completed
        mMp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("the song is finished playing");
                timer.cancel();
                timerTask.cancel();
            }
        });

    }



    //Realize the specified playing position
    public void seekTo(int position){
        mMp.seekTo(position);
    }


    private class MyBinder extends Binder implements Iservice{

        @Override
        public void callPlayMusic() {
            playMusic();
        }

        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        @Override
        public void callRePlayMusic() {
            mMp.start();
            mMp.isLooping();
            updateSeekBar();
        }

        @Override
        public void stopMusic() {
            MusicService.this.stopMusic();
        }

        @Override
        public void callSeekTo(int position) {
            seekTo(position);
        }

    }
}
