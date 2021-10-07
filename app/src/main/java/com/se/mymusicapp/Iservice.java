package com.se.mymusicapp;

public interface Iservice {
    public void callPlayMusic();
    public void callPauseMusic();
    public void callRePlayMusic();
    public void stopMusic();
    public void callSeekTo(int position);
}
