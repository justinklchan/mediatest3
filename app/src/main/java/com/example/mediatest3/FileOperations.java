package com.example.mediatest3;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class FileOperations {

    public static void writetofile(Activity av, short[] buff, String filename) {
        Log.e("asdf","writetofile "+(buff==null));
        try {
//            String dir = av.getExternalFilesDir(null).toString();
            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            if (android.os.Build.MODEL.equals("SM-R870")) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            }
            Log.e("asdf",dir);
            writetofile(dir, buff, filename);

        } catch(Exception e) {
            Log.e("asdf",e.toString());
        }
    }

    public static double[] readfromfile(Activity av, String filename) {
        LinkedList<Double> ll = new LinkedList<Double>();

        try {
            String dir = av.getExternalFilesDir(null).toString();
            File file = new File(dir + File.separator + filename);
            BufferedReader buf = new BufferedReader(new FileReader(file));

            String line;
            while ((line = buf.readLine()) != null && line.length() != 0) {
                Log.e("asdf",line);
//                ll.add(Double.parseDouble(line));
            }

            buf.close();
        } catch (Exception e) {
            Log.e("ble",e.getMessage());
        }

        double[] ar = new double[ll.size()];
//        int counter = 0;
//        for (Double d : ll) {
//            ar[counter++] = d;
//        }
//        ll.clear();
        return ar;
    }

    public static short[] readrawasset(Context context, int id) {
        Scanner inp = new Scanner(context.getResources().openRawResource(id));
        LinkedList<Short> ll = new LinkedList<>();
        int counter=0;
        while (inp.hasNextLine()) {
            ll.add(Short.parseShort(inp.nextLine()));
            counter += 1;
//            if (counter%1000==0) {
//                Log.e("asdf",counter+"");
//            }
        }
        inp.close();
        short[] ar = new short[ll.size()];
        counter = 0;
        for (Short d : ll) {
            ar[counter++] = d;
        }
        ll.clear();

        return ar;
    }

    public static short[] readrawasset_binary(Context context, int id) {
        InputStream inp = context.getResources().openRawResource(id);
        ArrayList<Integer> ll = new ArrayList<>();
        int counter=0;
        int byteRead=0;
        try {
            while ((byteRead = inp.read()) != -1) {
                ll.add(byteRead);
                counter += 1;
//                if (counter % 1000 == 0) {
//                    Log.e("asdf", counter + "");
//                }
            }
            inp.close();
        }
        catch(Exception e) {
            Log.e("asdf",e.getMessage());
        }
        short[] ar = new short[ll.size()/2];

        counter=0;
        for (int i = 0; i < ll.size(); i+=2) {
            int out=ll.get(i)+ll.get(i+1)*256;
            if (out > 32767) {
                out=out-65536;
            }
            ar[counter++]=(short)out;
        }

        return ar;
    }

    public static void writetofile(String _ExternalFilesDir, short[] buff, String filename) {
//        Constants.writing=true;
        Log.e("asdf","writetofile " + filename + " "+(buff==null));
        long ts = System.currentTimeMillis();

        try {
            String dir = _ExternalFilesDir;
            File path = new File(dir);
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(dir, filename);

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for (int i = 0; i < buff.length; i++) {
                buf.append(""+buff[i]);
                buf.newLine();
            }
            buf.flush();
            buf.close();
        } catch(Exception e) {
            Log.e("asdf","write to file exception " +e.toString());
        }
        Log.e("asdf","finish writing "+filename + (System.currentTimeMillis()-ts));
    }
}
