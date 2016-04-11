package com.example.annuoaichengzhang.videodemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, Runnable {


    // 1. 加载VideoView
    private VideoView videoView;

    //////////////////////////////
    // 控制部分的成员

    // 控制条
    private ViewGroup mediaController;

    // 播放进度条
    private SeekBar mediaProgressBar;

    // 播放进度时间
    private TextView txtProgressInfo;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 处理控制条的部分

            int what = msg.what;

            switch (what){
                case 1:    // 隐藏控制条
                    mediaController.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    int current = msg.arg1;
                    int duration = msg.arg2;
                    mediaProgressBar.setMax(duration);
                    mediaProgressBar.setProgress(current);
                    break;
            }

        }
    };

    private boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setOnTouchListener(this);

        // 加载自定义的播放控制部分
        mediaController = (ViewGroup) findViewById(R.id.mediacontroller);
        mediaProgressBar = (SeekBar) findViewById(R.id.media_progress_bar);
        txtProgressInfo = (TextView) findViewById(R.id.txt_progress_info);


        // 按钮处理部分
        Button btn = (Button) findViewById(R.id.btn_back);
        if (btn != null) {
            btn.setOnClickListener(this);
        }

        btn = (Button) findViewById(R.id.btn_play_pause);
        if (btn != null) {
            btn.setOnClickListener(this);
        }

        btn = (Button) findViewById(R.id.btn_forward);
        if (btn != null) {
            btn.setOnClickListener(this);
        }


        // 设置VideoView的默认的控制条，显示进度、时间、播放控制
//        MediaController controller = new MediaController(this);
//
//        // 设置控制器浮动在 VideoView上，等一小段时间自动消失
//        controller.setAnchorView(videoView);
//
//        videoView.setMediaController(controller);


        // 2. 指定 VideoView 播放相应的视频文件

        // 加载应用程序 res/raw 目录的视频
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_test);

        // 加载服务器的视频
        Uri uri = Uri.parse("http://down.fodizi.com/haitaofs/twd3117-1.flv");

        videoView.setVideoURI(uri);

//        File file = new File("/mnt/sdcard/videoviewdemo.mp4");
//        if (file.exists()) {
//            videoView.setVideoPath(file.getAbsolutePath());
//        }

        videoView.start();

        Thread thread = new Thread(this);

        thread.start();

        // 加载存储卡中的文件，形成 Uri
//        String state = Environment.getExternalStorageState();
//        if(state.equals(Environment.MEDIA_MOUNTED)){
//
//            File directory =
//                    Environment.getExternalStorageDirectory();
//
//            File file = new File(directory, "video_test_1.3gp");
//
//            Uri uri = Uri.fromFile(file);
//
//            videoView.setVideoURI(uri);
//
//            // 3. 播放视频
//            videoView.start();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止视频的播放
        videoView.stopPlayback();
        running = false;
    }

    @Override
    public void onClick(View v) {
        // 进行控制按钮的控制

        int id = v.getId();

        switch (id) {
            case R.id.btn_play_pause:
                // 如果视频正在播放
                if (videoView.isPlaying()) {
                    // 那么暂停播放
                    videoView.pause();
                } else {
                    try {
                        // 获取当前播放的进度
                        int pos = videoView.getCurrentPosition();
                        // 获取视频总时长
                        int duration = videoView.getDuration();
                        if (pos > 0 && pos < duration) {
                            videoView.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }
    @Override
    public void run() {
        try {

            running = true;
            while (running) {
                if(videoView.isPlaying()) {
                    int duration = videoView.getDuration();

                    int currentPosition = videoView.getCurrentPosition();

                    Message message = handler.obtainMessage(2);
                    message.arg1 = currentPosition;
                    message.arg2 = duration;
                    handler.sendMessage(message);
                }

                Thread.sleep(1000);
            }
        }catch (Exception ex){

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            // 点击之后，检查当前控制条是否显示，
            int visibility = mediaController.getVisibility();
            if(visibility == View.INVISIBLE){
                // 如果没有显示，那么显示控制条
                mediaController.setVisibility(View.VISIBLE);

                // 当控制条显示的时候，等待5秒，自动隐藏。
                Message message = handler.obtainMessage(1);
                handler.sendMessageDelayed(message, 3000);


            }else{
                mediaController.setVisibility(View.INVISIBLE);
            }
        }

        return false;

    }
}
