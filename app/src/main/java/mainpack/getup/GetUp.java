package mainpack.getup;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.os.SystemClock;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class GetUp extends ActionBarActivity implements SensorEventListener, OnClickListener{
    private Button resetBtn;
    private TextView textViewTime;

    private Chronometer chronometer;
    private SeekBar sitTimeSeek;
    private TextView sitTimeDisplay, debug;

    private Sensor accelerometer;
    private SensorManager sm;
    private TextView acceleration;
    private int i = 0;
    private int n = 0;
    //protected Vibrator vib;

    private static int TIME_MAX = 30000; // Need to map to UI element
    private final int TIME_INTERVAL = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getup);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        acceleration = (TextView) findViewById(R.id.acceleration);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setFormat("00:%s");
        chronometer.start();
        ((Button) findViewById(R.id.resetBtn)).setOnClickListener(this);

        sitTimeDisplay = (TextView) findViewById(R.id.sitTimeDisplay);
        debug = (TextView) findViewById(R.id.debug);
        sitTimeSeek = (SeekBar) findViewById(R.id.sitTime);
        sitTimeSeek.setProgress(90);
        //sitTimeDisplay.setText("Sit time: + sitTimeSeek.get");
        sitTimeDisplay.setText("Sit time: " + (sitTimeSeek.getProgress()/60) + " hours "+ (sitTimeSeek.getProgress() - (sitTimeSeek.getProgress()/60)*60) + " minutes");
        sitTimeSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int period = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                sitTimeDisplay.setText("Sit time: " + (progress/60) + " hours "+ (progress - (progress/60)*60) + " minutes");

            }
        });

    }


    private void resetChrono(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void onClick(View v) {
        resetChrono();
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(500);
    }

    public void vib(Vibrator v) {
        v.vibrate(500);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        int k = 0;
        if(sitTimeSeek.getProgress() / 12 == n/5){
            acceleration.setText("YOU FUCKING DID IT");
            resetChrono();
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);
            n = 0;
            n = 1;
            n = 0;
        }
        else {
            acceleration.setText("sec:  "+n/5+ "\ngoal: "+sitTimeSeek.getProgress()/12);
            //acceleration.setText("X: " + event.values[0] +
            //        "\nY: " + event.values[1] +
            //        "\nZ: " + event.values[2]);
        }
        if(event.values[0]<1 && event.values[0] > -1){
            if(event.values[1]<1 && event.values[1] > -1){
                if((event.values[2]<11 && event.values[2] > 9) || event.values[2] >-11 && event.values[2] < -9){
                    k = 1;
                }
            }
        }
        if(event.values[0]<8 && event.values[0] > 6.5){
            if(event.values[1]<4 && event.values[1] > 2.5){
                if((event.values[2]<-4 && event.values[2] > -5.5)){
                    k = 1;
                }
            }
        }
        if (k != 1){
            n++;
        }


        //System.out.println(i);
        //System.out.println(event.values[0]+" : "+event.values[1]+" : "+event.values[2]);

        String countStr = chronometer.getText().toString();
        Integer count = 0;
        String[] countLst = countStr.split(":");
        count += Integer.parseInt(countLst[0])*60;
        count += Integer.parseInt(countLst[1]);
        //count += Integer.parseInt(countLst[2]);

        debug.setText(count.toString());

        if( count == sitTimeSeek.getProgress()){
            debug.setText("MATCH!!!!!!!");
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(5000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_getup, menu);
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



}
