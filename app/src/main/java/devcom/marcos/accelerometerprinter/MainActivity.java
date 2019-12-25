package devcom.marcos.accelerometerprinter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Maths
    private double time;
    private double[] distance = new double[3];
    private double[] linear_acceleration = new double[3];
    private double[] speed = new double[3];

    //Others
    private DecimalFormat f;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener se;

    //UI
    private TextView tf;
    private TextView tf2;
    private TextView tf3;
    private TextView periodTF;
    private Button startB;
    private Button stopB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Maths
        for (int i = 0; i<3; i++){
            distance[i] = 0;
            linear_acceleration[i] = 0;
            speed[i] = 0;
        }
        time = System.nanoTime() / 1000000000.0;

        //Initialize Others
        f = new DecimalFormat("##0.00000");
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        se = this;

        //Initialize UI
        tf = findViewById(R.id.tf);
        tf2 = findViewById(R.id.tf2);
        tf3 = findViewById(R.id.tf3);
        periodTF = findViewById(R.id.periodTF);
        startB = findViewById(R.id.startB);
        stopB = findViewById(R.id.stopB);

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSensorManager.registerListener(se, mSensor, 1000000);
            }
        });
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSensorManager.unregisterListener(se, mSensor);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(se, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mSensor);
    }

    public void onSensorChanged(SensorEvent var1){
        time = System.nanoTime() / 1000000000.0 - time;
        periodTF.setText("Period: " + f.format(time) + "s");
        distance[0] += (speed[0] * time) + (linear_acceleration[0] * time * time) / 2;
        distance[1] += (speed[1] * time) + (linear_acceleration[1] * time * time) / 2;
        distance[2] += (speed[2] * time) + (linear_acceleration[2] * time * time) / 2;

        speed[0] += linear_acceleration[0] * time;
        speed[1] += linear_acceleration[1] * time;
        speed[2] += linear_acceleration[2] * time;

        time = System.nanoTime() / 1000000000.0;
        linear_acceleration[0] = var1.values[0];
        linear_acceleration[1] = var1.values[1];
        linear_acceleration[2] = var1.values[2];

        tf.setText(f.format(linear_acceleration[0])+" "+f.format(linear_acceleration[1])+" "+f.format(linear_acceleration[2]));
        tf2.setText(f.format(distance[0])+" "+f.format(distance[1])+" "+f.format(distance[2]));
        tf3.setText(f.format(speed[0])+" "+f.format(speed[1])+" "+f.format(speed[2]));
        Log.i("info", f.format(linear_acceleration[0])+" "+f.format(linear_acceleration[1])+" "+f.format(linear_acceleration[2]));
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}
