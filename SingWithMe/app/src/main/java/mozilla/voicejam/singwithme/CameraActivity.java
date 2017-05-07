/*
 * Copyright 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mozilla.voicejam.singwithme;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity displaying a fragment that implements RAW photo captures.
 */
public class CameraActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.record_sound)
    Button recordSound;
    @BindView(R.id.play_sound)
    Button playSound;
    @BindView(R.id.stop_record)
    Button stopRecord;
    @BindView(R.id.start)
    Button startBtn;

    private String filePath;
    private static final int MY_PERMISSIONS_REQUEST = 5566;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST);
        } else {
            afterHasPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    afterHasPermissions();
                } else {
                    Toast.makeText(this, "Please accept all permissions.", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_sound:
                record();
                break;
            case R.id.play_sound:
                play();
                break;
            case R.id.stop_record:
                stop();
                break;
            case R.id.start:
                start();
                break;
        }
    }

    void record() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            File file = File.createTempFile("raw", ".amr", this.getExternalFilesDir(null));
            filePath = file.getAbsolutePath();

            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "FileIOException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    void start() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music_sample);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer MP) {
                MP.release();
            }
        });
    }

    void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    void play() {
        Uri uri = Uri.parse(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer MP) {
                MP.release();
            }
        });
    }

    void afterHasPermissions() {
        recordSound.setOnClickListener(this);

        // ToDo disable play btn when there is no voice been record
        playSound.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
        startBtn.setOnClickListener(this);
    }

}
