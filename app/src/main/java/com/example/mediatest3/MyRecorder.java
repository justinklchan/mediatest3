package com.example.mediatest3;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class MyRecorder extends Thread {

    public boolean recording;
    public int samplingfrequency;
    public short[] samples;
    public short[] samples1;
    public short[] samples2;
    public short[] temp;
    int channels;
    int count;
    int count1;
    int count2;
    AudioRecord rec;
    int minbuffersize;
    String filename;

    public MyRecorder(Activity av,String filename, int time) {
        int sampleRate = 48000;
        channels=AudioFormat.CHANNEL_IN_STEREO;
        this.filename=filename;
        // Define the channel configuration
        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                .setChannelIndexMask(0x8)  // /**/Configuring to record from the front microphone
                .build();

        int buffersize = AudioRecord.getMinBufferSize(
                sampleRate,
                channels,
                AudioFormat.ENCODING_PCM_16BIT);
        minbuffersize=buffersize;
        temp = new short[minbuffersize];
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
//        }
        int arrLength=sampleRate*time;
        if (channels == AudioFormat.CHANNEL_IN_MONO) {
            samples = new short[arrLength];
        }
        else if (channels == AudioFormat.CHANNEL_IN_STEREO) {
            samples1 = new short[arrLength];
            samples2 = new short[arrLength];
        }
        try {
            if (ActivityCompat.checkSelfPermission(av, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ;
            }
//            rec = new AudioRecord.Builder()
////                    .setAudioSource(MediaRecorder.AudioSource.MIC)  // Select microphone as the audio source
//                    .setAudioFormat(audioFormat)
//                    .setBufferSizeInBytes(buffersize)
//                    .build();

            rec = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channels,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minbuffersize);
        }
        catch(IllegalArgumentException e) {
            Log.e("asdf","");
        }
    }

    public void run() {
        Log.e("rec","rec");
        if (channels == AudioFormat.CHANNEL_IN_MONO) {
            Log.e("rec","mono");
            count = 0;
            int bytesread;
            rec.startRecording();
            recording = true;
            while (recording) {
                bytesread = rec.read(temp, 0, minbuffersize);
                Log.e("rec",System.currentTimeMillis()+","+bytesread);
                for (int i = 0; i < bytesread; i++) {
                    if (count >= samples.length && rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        rec.stop();
                        rec.release();
                        recording = false;
                        Log.e("rec","COMPLETE");
                        FileOperations.writetofile(MainActivity.av, samples, filename+".txt");

//                        Constants.sensorFlag=false;
//                        FileOperations.writeSensors(MainActivity.av,filename+".txt");
                        break;
                    } else if (count < samples.length) {
                        samples[count] = temp[i];
                        count++;
                    } else {
                        break;
                    }
                }
            }
        }
        else if (channels == AudioFormat.CHANNEL_IN_STEREO) {
            Log.e("rec","stereo");
            count1 = 0;
            count2 = 0;
            int bytesread;
            rec.startRecording();
            recording = true;
            while (recording) {
                bytesread = rec.read(temp, 0, minbuffersize);
                Log.e("rec",System.currentTimeMillis()+","+bytesread);
                if (count1 < samples1.length) {
//                    Log.e("rec",count1+","+samples1.length);
                    for (int i = 0; i < bytesread; i+=2) {
                        if (count1 < samples1.length && count2 < samples2.length) {
                            if (android.os.Build.MODEL.equals("SM-N975U1")) {
                                //s10
                                samples2[count1] = temp[i];
                                samples1[count2] = temp[i + 1];
                            }
                            else if (android.os.Build.MODEL.equals("SM-G950U")) {
                                //s8
                                samples1[count1] = temp[i];
                                samples2[count2] = temp[i + 1];
                            }
                            else if (android.os.Build.MODEL.equals("SM-G960U")) {
                                //s9
                                samples1[count1] = temp[i];
                                samples2[count2] = temp[i + 1];
                            }
                            else {
                                samples1[count1] = temp[i];
                                samples2[count2] = temp[i + 1];
                            }
                            count1++;
                            count2++;
                        }
                    }
                }
                else if (count1 >= samples1.length && rec.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
//                else if (count1 >= samples1.length){
                    Log.e("asdf","OVER");
                    rec.stop();
                    rec.release();
                    recording = false;

//                    Constants.sensorFlag=false;
                    new Runnable() {
                        public void run() {
                            Log.e("asdf","write to file");
                            FileOperations.writetofile(MainActivity.av, samples1, filename+"-top.txt");
                            FileOperations.writetofile(MainActivity.av, samples2, filename+"-bottom.txt");
//                            FileOperations.writeSensors(MainActivity.av,filename+".txt");
                        }
                    }.run();
                    break;
                }
            }
        }
    }
}
