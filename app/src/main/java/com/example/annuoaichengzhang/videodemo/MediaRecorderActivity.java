package com.example.annuoaichengzhang.videodemo;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class MediaRecorderActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton record , stop;
    // 系统的视频文件
    File videoFile ;
    MediaRecorder mRecorder;
    // 显示视频预览的surfaceview
    SurfaceView sView;
    // 记录是否正在录制视频
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        record = (ImageButton) findViewById(R.id.record);
        stop = (ImageButton) findViewById(R.id.stop);
        sView = (SurfaceView) findViewById(R.id.sView);

        stop.setEnabled(false);
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        sView = (SurfaceView) this.findViewById(R.id.sView);
        // 设置surfaceview不需要自己维护缓冲区
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        sView.getHolder().setFixedSize(320, 280);
        // 设置该组件让屏幕不会自动关闭
        sView.getHolder().setKeepScreenOn(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View source) {
        switch (source.getId())
        {
            // 单击录制视频按钮
            case R.id.record:
                if (!Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED))
                {
                    Toast.makeText(MediaRecorderActivity.this
                            , "sd卡不存在，请插入sd卡"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                try
                {
                    // 创建保存录制视频的视频文件
                    videoFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/myvideo.mp4");
                    // 创建MediaRecorder对象
                    mRecorder = new MediaRecorder();
                    mRecorder.reset();
                    // 设置从麦克风采集声音
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 设置从摄像头采集图像
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    // 设置视频文件的输出格式
                    // 必须在设置声音编码格式、图像编码格式之前设置
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    // 设置声音编码的格式
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    // 设置图像编码格式
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                    mRecorder.setVideoSize(320, 280);
                    // 每秒4帧
                    mRecorder.setVideoFrameRate(4);
                    mRecorder.setOutputFile(videoFile.getAbsolutePath());
                    // 制定使用surfaceview来预览视频
                    mRecorder.setPreviewDisplay(sView.getHolder().getSurface());  //
                    mRecorder.prepare();
                    // 开始录制
                    mRecorder.start();
                    System.out.println("---recording---");
                    // 让record按钮不可用
                    record.setEnabled(false);
                    // 让stop按钮可用
                    stop.setEnabled(true);
                    isRecording = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            // 单击停止按钮
            case R.id.stop:
                // 如果正在录制视频
                if (isRecording)
                {
                    // 停止录制
                    mRecorder.stop();
                    // 释放资源
                    mRecorder.release();
                    mRecorder = null;
                    // 让record按钮可以可用
                    record.setEnabled(true);
                    // 让stop按钮不可用
                    stop.setEnabled(false);
                }
                break;
        }
    }
}
