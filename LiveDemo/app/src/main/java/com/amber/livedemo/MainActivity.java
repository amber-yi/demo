package com.amber.livedemo;

import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.util.device.DeviceInfo;
import com.ksyun.media.streamer.util.device.DeviceInfoTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.connectBT)
    Button mConnectButton;
    @BindView(R.id.rtmpUrl)
    EditText mUrlEditText;
    @BindView(R.id.frameRatePicker)
    EditText mFrameRateEditText;
    @BindView(R.id.videoBitratePicker)
    EditText mVideoBitRateEditText;
    @BindView(R.id.audioBitratePicker)
    EditText mAudioBitRateEditText;

    @BindView(R.id.cap_360)
    RadioButton mCap360Button;
    @BindView(R.id.cap_480)
    RadioButton mCap480Button;
    @BindView(R.id.cap_720)
    RadioButton mCap720Button;
    @BindView(R.id.cap_1080)
    RadioButton mCap1080Button;

    @BindView(R.id.preview_360)
    RadioButton mPreview360Button;
    @BindView(R.id.preview_480)
    RadioButton mPreview480Button;
    @BindView(R.id.preview_720)
    RadioButton mPreview720Button;
    @BindView(R.id.preview_1080)
    RadioButton mPreview1080Button;

    @BindView(R.id.target_360)
    RadioButton mRes360Button;
    @BindView(R.id.target_480)
    RadioButton mRes480Button;
    @BindView(R.id.target_540)
    RadioButton mRes540Button;
    @BindView(R.id.target_720)
    RadioButton mRes720Button;

    @BindView(R.id.orientationbutton1)
    RadioButton mLandscapeButton;
    @BindView(R.id.orientationbutton2)
    RadioButton mPortraitButton;

    @BindView(R.id.encode_group)
    RadioGroup mEncodeGroup;
    @BindView(R.id.encode_sw)
    RadioButton mSWButton;
    @BindView(R.id.encode_hw)
    RadioButton mHWButton;
    @BindView(R.id.encode_sw1)
    RadioButton mSW1Button;

    @BindView(R.id.encode_type)
    RadioGroup mEncodeTypeGroup;
    @BindView(R.id.encode_h264)
    RadioButton mEncodeWithH264;
    @BindView(R.id.encode_h265)
    RadioButton mEncodeWithH265;

    @BindView(R.id.encode_scene)
    RadioGroup mSceneGroup;
    @BindView(R.id.encode_scene_default)
    RadioButton mSceneDefaultButton;
    @BindView(R.id.encode_scene_show_self)
    RadioButton mSceneShowSelfButton;
    @BindView(R.id.encode_scene_game)
    RadioButton mSceneGameButton;
    @BindView(R.id.encode_profile)
    RadioGroup mProfileGroup;
    @BindView(R.id.encode_profile_low_power)
    RadioButton mProfileLowPowerButton;
    @BindView(R.id.encode_profile_balance)
    RadioButton mProfileBalanceButton;
    @BindView(R.id.encode_profile_high_perf)
    RadioButton mProfileHighPerfButton;

    @BindView(R.id.stereo_stream)
    CheckBox mStereoStreamCheckBox;
    @BindView(R.id.autoStart)
    CheckBox mAutoStartCheckBox;
    @BindView(R.id.print_debug_info)
    CheckBox mShowDebugInfoCheckBox;

    private DeviceInfo mDeviceInfo;
    private boolean mShowDeviceToast = false;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mConnectButton.setOnClickListener(this);
        mFrameRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mVideoBitRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAudioBitRateEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        updateUI();
        mEncodeTypeGroup.setOnCheckedChangeListener(this);
        mEncodeGroup.setOnCheckedChangeListener(this);
    }

    private void updateUI() {
        if (mHWButton.isChecked() || mEncodeWithH265.isChecked()) {
            setEnableRadioGroup(mSceneGroup, false);
            if (mEncodeWithH265.isChecked()) {
                setEnableRadioGroup(mProfileGroup, false);
            } else {
                setEnableRadioGroup(mProfileGroup, true);
            }
        } else {
            setEnableRadioGroup(mSceneGroup, true);
            setEnableRadioGroup(mProfileGroup, true);
        }
    }

    private void setEnableRadioGroup(RadioGroup radioGroup, boolean enable) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enable);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //init encode info
        //若在硬编白名单中存在设备信息，则参考白名单信息进行配置
        DeviceInfo lastDeviceInfo = mDeviceInfo;
        mDeviceInfo = DeviceInfoTools.getInstance().getDeviceInfo();
        if (mDeviceInfo != null) {
            Log.i(TAG, "deviceInfo:" + mDeviceInfo.printDeviceInfo());
            if (!mShowDeviceToast || (lastDeviceInfo != null && !mDeviceInfo.compareDeviceInfo
                    (lastDeviceInfo))) {
                if (mDeviceInfo.encode_h264 == DeviceInfo.ENCODE_HW_SUPPORT) {
                    //支持硬编，建议使用硬编
                    mHWButton.setChecked(true);
                    Toast.makeText(this, "该设备支持h264硬编，建议您使用硬编", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "该设备可能不在硬编白名单中\n或者不支持硬编\n或者服务器还未返回" +
                            "\n如果支持硬编，欢迎一起更新白名单", Toast.LENGTH_SHORT).show();
                }
                mShowDeviceToast = true;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectBT:
                int frameRate = 0;
                int videoBitRate = 0;
                int audioBitRate = 0;
                int captureResolution;
                int previewResolution;
                int targetResolution;
                int encodeType;
                int encodeMethod;
                int encodeScene;
                int encodeProfile;
                int orientation;
                boolean stereoStream;
                boolean startAuto;
                boolean showDebugInfo;

                if (!TextUtils.isEmpty(mUrlEditText.getText())
                        && mUrlEditText.getText().toString().startsWith("rtmp")) {
                    if (!TextUtils.isEmpty(mFrameRateEditText.getText().toString())) {
                        frameRate = Integer.parseInt(mFrameRateEditText.getText()
                                .toString());
                    }

                    if (!TextUtils.isEmpty(mVideoBitRateEditText.getText().toString())) {
                        videoBitRate = Integer.parseInt(mVideoBitRateEditText.getText()
                                .toString());
                    }

                    if (!TextUtils.isEmpty(mAudioBitRateEditText.getText().toString())) {
                        audioBitRate = Integer.parseInt(mAudioBitRateEditText.getText()
                                .toString());
                    }

                    if (mCap360Button.isChecked()) {
                        captureResolution = StreamerConstants.VIDEO_RESOLUTION_360P;
                    } else if (mCap480Button.isChecked()) {
                        captureResolution = StreamerConstants.VIDEO_RESOLUTION_480P;
                    } else if (mCap720Button.isChecked()) {
                        captureResolution = StreamerConstants.VIDEO_RESOLUTION_720P;
                    } else {
                        captureResolution = StreamerConstants.VIDEO_RESOLUTION_1080P;
                    }

                    if (mPreview360Button.isChecked()) {
                        previewResolution = StreamerConstants.VIDEO_RESOLUTION_360P;
                    } else if (mPreview480Button.isChecked()) {
                        previewResolution = StreamerConstants.VIDEO_RESOLUTION_480P;
                    } else if (mPreview720Button.isChecked()) {
                        previewResolution = StreamerConstants.VIDEO_RESOLUTION_720P;
                    } else {
                        previewResolution = StreamerConstants.VIDEO_RESOLUTION_1080P;
                    }

                    if (mRes360Button.isChecked()) {
                        targetResolution = StreamerConstants.VIDEO_RESOLUTION_360P;
                    } else if (mRes480Button.isChecked()) {
                        targetResolution = StreamerConstants.VIDEO_RESOLUTION_480P;
                    } else if (mRes540Button.isChecked()) {
                        targetResolution = StreamerConstants.VIDEO_RESOLUTION_540P;
                    } else {
                        targetResolution = StreamerConstants.VIDEO_RESOLUTION_720P;
                    }

                    if (mEncodeWithH265.isChecked()) {
                        encodeType = AVConst.CODEC_ID_HEVC;
                    } else {
                        encodeType = AVConst.CODEC_ID_AVC;
                    }

                    if (mHWButton.isChecked()) {
                        encodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;
                        mSceneGroup.setClickable(false);
                    } else if (mSWButton.isChecked()) {
                        mSceneGroup.setClickable(true);
                        encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
                    } else {
                        mSceneGroup.setClickable(true);
                        encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT;
                    }

                    if (mSceneShowSelfButton.isChecked()) {
                        encodeScene = VideoEncodeFormat.ENCODE_SCENE_SHOWSELF;
                    } else if (mSceneGameButton.isChecked()) {
                        encodeScene = VideoEncodeFormat.ENCODE_SCENE_GAME;
                    } else {
                        encodeScene = VideoEncodeFormat.ENCODE_SCENE_DEFAULT;
                    }

                    if (mProfileLowPowerButton.isChecked()) {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER;
                    } else if (mProfileBalanceButton.isChecked()) {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;
                    } else {
                        encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE;
                    }

                    if (mLandscapeButton.isChecked()) {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    } else if (mPortraitButton.isChecked()) {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    } else {
                        orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
                    }

                    stereoStream = mStereoStreamCheckBox.isChecked();
                    startAuto = mAutoStartCheckBox.isChecked();
                    showDebugInfo = mShowDebugInfoCheckBox.isChecked();

                    LiveActivity.startActivity(getApplicationContext(), 0,
                            mUrlEditText.getText().toString(), frameRate, videoBitRate,
                            audioBitRate, captureResolution, previewResolution, targetResolution,
                            orientation, encodeType, encodeMethod, encodeScene, encodeProfile,
                            stereoStream, startAuto, showDebugInfo);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
