package devcom.marcos.accelerometerprinter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Maths
    private double time;
    private double minTime;
    private double maxTime;
    private double[] distance = new double[3];
    private double[] vector_rotation = new double[3];
    private double[] gravity = new double[3];
    private double[] linear_acceleration = new double[3];
    private double[] max_linear_acceleration = new double[3];
    private double[] speed = new double[3];

    //Others
    boolean first;
    private DecimalFormat f;
    private DecimalFormat f1;
    private SensorManager mSensorManager;
    private Sensor mSensorAcc;
    private Sensor mSensorRot;
    private SensorEventListener se;

    //UI
    private TextView tf;
    private TextView tf1;
    private TextView tf2;
    private TextView tf3;
    private TextView periodTF;
    private Button startB;
    private Button stopB;
    private Button startB2;
    private Button stopB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Maths
        for (int i = 0; i < 3; i++) {
            distance[i] = 0;
            linear_acceleration[i] = 0;
            speed[i] = 0;
        }
        maxTime = -1;
        minTime = 99999;
        time = System.nanoTime() / 1000000000.0;

        //Initialize Others
        f = new DecimalFormat("##0.00");
        f1 = new DecimalFormat("##0.0");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        se = this;
        first = true;

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
                mSensorManager.registerListener(se, mSensorAcc, SensorManager.SENSOR_DELAY_UI);
            }
        });
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSensorManager.unregisterListener(se, mSensorAcc);
            }
        });

    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(se, mSensorAcc);
    }

    public void onSensorChanged(SensorEvent var1) {
        time = Double.parseDouble(f.format(System.nanoTime() / 1000000000.0 - time));
        if (!first) {
            for (int i = 0; i < 3; i++) {
                if (max_linear_acceleration[i] < linear_acceleration[i])
                    max_linear_acceleration[i] = linear_acceleration[i];
            }
            if (time > maxTime) maxTime = time;
            if (time < minTime) minTime = time;
        }
        periodTF.setText("MinTime: " + f.format(minTime) + "s MaxTime: " + f.format(maxTime) + "s Period: " + f.format(time) + "s\n" +
                "Max linear Acc: " + f.format(max_linear_acceleration[0]) + " " + f.format(max_linear_acceleration[1]) +
                " " + f.format(max_linear_acceleration[2]));
        distance[0] += (Double.parseDouble(f1.format(speed[0])) * time) + (linear_acceleration[0] * time * time) / 2;
        distance[1] += (Double.parseDouble(f1.format(speed[1])) * time) + (linear_acceleration[1] * time * time) / 2;
        distance[2] += (Double.parseDouble(f1.format(speed[2])) * time) + (linear_acceleration[2] * time * time) / 2;

        speed[0] += linear_acceleration[0] * time;
        speed[1] += linear_acceleration[1] * time;
        speed[2] += linear_acceleration[2] * time;
        first = false;
        time = System.nanoTime() / 1000000000.0;

        linear_acceleration[0] = Double.parseDouble(f1.format(var1.values[0]));
        linear_acceleration[1] = Double.parseDouble(f1.format(var1.values[1]));
        linear_acceleration[2] = Double.parseDouble(f1.format(var1.values[2]));


        tf.setText(f.format(linear_acceleration[0]) + " " + f.format(linear_acceleration[1]) + " " + f.format(linear_acceleration[2]));
        tf2.setText(f.format(distance[0]) + " " + f.format(distance[1]) + " " + f.format(distance[2]));
        tf3.setText(f1.format(speed[0]) + " " + f1.format(speed[1]) + " " + f1.format(speed[2]));
        Log.i("info", f.format(linear_acceleration[0]) + " " + f.format(linear_acceleration[1]) + " " + f.format(linear_acceleration[2]));
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
