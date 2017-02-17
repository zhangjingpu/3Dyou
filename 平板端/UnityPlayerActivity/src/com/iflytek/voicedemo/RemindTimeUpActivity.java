package com.iflytek.voicedemo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


import java.io.IOException;

/**
 * Created by Administrator on 2015/8/27.
 */
public class RemindTimeUpActivity extends Activity
{

    private static final float MIN_OFFSET = 30;
    private MediaPlayer mediaPlayer;
    private float startY, offsetY;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_time_up);
        Log.e("RemindTimeUpActivity", "鬧鐘開啟");
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        container = (RelativeLayout) findViewById(R.id.activity_remind_time_up_container);

        mediaPlayer.setLooping(true);
        try
        {
            if (mediaPlayer != null)
                mediaPlayer.stop();

            mediaPlayer.prepare();
        } catch (IOException e)
        {
            Log.e("MediaPlayer Error", e.toString());
        }
        mediaPlayer.start();

        container.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetY = startY - event.getY();
                        if (offsetY >= MIN_OFFSET)
                        {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            finish();
                        }
                        break;

                }
                return true;
            }
        });


        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener()
                {
                    // @Override
                    /*覆盖文件播出完毕事件*/
                    public void onCompletion(MediaPlayer arg0)
                    {
                        try
                        {
                            /*解除资源与MediaPlayer的赋值关系
                            * 让资源可以为其它程序利用*/
                            mediaPlayer.release();
                            /*改变TextView为播放结束*/
                        } catch (Exception e)
                        {
                            Log.e("MediaPlayer Error", e.toString());
                        }
                    }
                });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
          /*覆盖错误处理事件*/
            public boolean onError(MediaPlayer arg0, int arg1, int arg2)
            {
                try
                {
              /*发生错误时也解除资源与MediaPlayer的赋值*/
                    mediaPlayer.release();
                } catch (Exception e)
                {
                    Log.e("MediaPlayer Error", e.toString());
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        mediaPlayer.release();
        super.onDestroy();
    }
}
