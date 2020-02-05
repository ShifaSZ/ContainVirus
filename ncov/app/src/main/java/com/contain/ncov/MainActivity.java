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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public LocationService locationService;
    private MyLocationListener myListener = new MyLocationListener();
    private TextView locationText, recordsText;
    private Button inspectButton;
    private long seqNum = 0;
    private long timestemp=0;
    //private int lastLine=0;

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
        String text = "足迹保存分析器\n抗击新冠病毒！武汉加油！湖北加油！中国加油！\n"+
                "开源代码：https://github.com/ShifaSZ/ContainVirus\n"+
                "提示：连续后台运行15天即可解锁病毒感染风险分析功能";
        locationText.setText(text);
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
        final int count = 16;
        String records[] = new String[count];
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
            int readLines = 0;
            while (true) {
                try {
                    String line = reader.readLine();
                    if (null == line)
                        break;
                    records[readLines] = line;
                    readLines = (readLines+1)%count;
                } catch (IOException ioe) {
                    //recordsText.setText("Exception while reading file " + ioe);
                    break;
                }
            }
            String output="";
            for (int i=0; i<count; i++){
                output = output + "     " + records[(readLines+i)%count] + "\n";
            }
            recordsText.setText(output);
            try{
                reader.close();
            } catch (IOException ioe) {
                locationText.setText("Exception while closing file " + ioe);
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
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            Date date = new Date();
            long ts = date.getTime();
            if (ts-timestemp < 60*1000)
                return;
            timestemp = ts;
            seqNum++;
            //locationText.setText("#"+seqNum+"\nTime:"+currentTime + "\nLatitude: " + latitude+"\nLongitude:"+
            //        longitude+"\nRadius:"+radius);
            String position = "" + seqNum + "," + ts + "," + latitude + "," + longitude + "," + radius + "\n";
            appendFile(position);
        }
    }
}
