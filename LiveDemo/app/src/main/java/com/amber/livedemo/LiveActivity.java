package com.amber.livedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;

import com.ksyun.media.streamer.kit.KSYStreamer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by luosiyi on 2017/6/30.
 */

public class LiveActivity extends AppCompatActivity {

    private KSYStreamer mStreamer;

    @BindView(R.id.camera_preview)
    GLSurfaceView mCameraPreviewView;

    private Unbinder unbinder;

    public final static String URL = "url";
    public final static String FRAME_RATE = "framerate";
    public final static String VIDEO_BITRATE = "video_bitrate";
    public final static String AUDIO_BITRATE = "audio_bitrate";
    public final static String CAP_RESOLUTION = "cap_resolution";
    public final static String PREVIEW_RESOLUTION = "preview_resolution";
    public final static String VIDEO_RESOLUTION = "video_resolution";
    public final static String ORIENTATION = "orientation";
    public final static String ENCODE_TYPE = "encode_type";
    public final static String ENCODE_METHOD = "encode_method";
    public final static String ENCODE_SCENE = "encode_scene";
    public final static String ENCODE_PROFILE = "encode_profile";
    public final static String STEREO_STREAM = "stereo_stream";
    public final static String START_AUTO = "start_auto";
    public static final String SHOW_DEBUGINFO = "show_debuginfo";


    public static void startActivity(Context context, int fromType,
                                     String rtmpUrl, int frameRate,
                                     int videoBitrate, int audioBitrate,
                                     int capResolution, int previewResolution,
                                     int targetResolution, int orientation,
                                     int encodeType, int encodeMethod,
                                     int encodeScene, int encodeProfile,
                                     boolean stereoStream,
                                     boolean startAuto, boolean showDebugInfo) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", fromType);
        intent.putExtra(URL, rtmpUrl);
        intent.putExtra(FRAME_RATE, frameRate);
        intent.putExtra(VIDEO_BITRATE, videoBitrate);
        intent.putExtra(AUDIO_BITRATE, audioBitrate);
        intent.putExtra(CAP_RESOLUTION, capResolution);
        intent.putExtra(PREVIEW_RESOLUTION, previewResolution);
        intent.putExtra(VIDEO_RESOLUTION, targetResolution);
        intent.putExtra(ORIENTATION, orientation);
        intent.putExtra(ENCODE_TYPE, encodeType);
        intent.putExtra(ENCODE_METHOD, encodeMethod);
        intent.putExtra(ENCODE_SCENE, encodeScene);
        intent.putExtra(ENCODE_PROFILE, encodeProfile);
        intent.putExtra(STEREO_STREAM, stereoStream);
        intent.putExtra(START_AUTO, startAuto);
        intent.putExtra(SHOW_DEBUGINFO, showDebugInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        unbinder= ButterKnife.bind(this);
        mStreamer = new KSYStreamer(this);
        mStreamer.setDisplayPreview(mCameraPreviewView);
    }
}
