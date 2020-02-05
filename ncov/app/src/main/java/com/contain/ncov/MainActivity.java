package com.contain.ncov;

import android.content.Context;
import android.os.Bundle;

import com.baidu.location.service.LocationService;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public LocationService locationService;
    private MyLocationListener myListener = new MyLocationListener();
    private TextView locationText, recordsText;
    private Button inspectButton;
    private long seqNum = 0;
    private int lastLine=0;

    private  File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        locationText = (TextView)findViewById(R.id.latidude);
        recordsText = (TextView)findViewById(R.id.records);
        inspectButton = (Button)findViewById(R.id.inspectButon);
        inspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showData();
            }
        });
        Date currentTime = Calendar.getInstance().getTime();
        locationText.setText("#"+seqNum+"\nTime:"+currentTime+"\nLatitude:0\nLongitude:0\nRadius:0");
        file = initFile();
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListener);
        locationService.start();
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

    private void showData(){
        if (file.exists()) {
            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (IOException ioe) {
                recordsText.setText("Exception while new input file " + ioe);
            }
            if (null == is)
                return;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for (int i=0; i< lastLine; i++){
                try {
                    String line = reader.readLine();
                } catch (IOException ioe) {
                    recordsText.setText("Exception while reading file " + ioe);
                    break;
                }
            }
            int readLines;
            String output="";
            for (readLines=0; readLines<20; readLines++){
                String line="";
                try {
                    line = reader.readLine();
                } catch (IOException ioe) {
                    //recordsText.setText("Exception while reading file " + ioe);
                    break;
                }
                if (null == line)
                    break;
                output = output+line+"\n";
            }
            lastLine += readLines;
            recordsText.setText(output);
            try{
                reader.close();
            } catch (IOException ioe) {
                recordsText.setText("Exception while closing file " + ioe);
            }
        }
    }

    private File initFile(){
        Context context = getApplicationContext();
        File path = context.getFilesDir();
        File file = new File(path, "savedLocations.txt");
        if (!file.exists()) {
            try{
                file.createNewFile();
                //file.setReadable(true, false);
            }
            catch (IOException ioe) {
                locationText.setText("Exception while creating file " + ioe);
            }
            Log.d("length","New file");
        } else
            Log.d("length",String.valueOf(file.length()));
        return file;
    }

    public void appendFile(String data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(data.getBytes());
        }
        catch (FileNotFoundException e) {
            locationText.setText("File not found" + e);
        }
        catch (IOException ioe) {
            locationText.setText("Exception while writing file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                locationText.setText("Error while closing stream: " + ioe);
            }
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            Date currentTime = Calendar.getInstance().getTime();
            seqNum++;
            locationText.setText("#"+seqNum+"\nTime:"+currentTime + "\nLatitude: " + latitude+"\nLongitude:"+
                    longitude+"\nRadius:"+radius);
            String position = seqNum+","+currentTime+","+latitude+","+longitude+","+radius+"\n";
            appendFile(position);
        }
    }
}
