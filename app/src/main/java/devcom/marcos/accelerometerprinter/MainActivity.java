package devcom.marcos.accelerometerprinter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    double time;
    double[] distance = new double[3];
    private double[] linear_acceleration = new double[3];

    private DecimalFormat f;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    TextView tf;
    TextView tf2;
    //private double[] gravity = new double[3];
    private Sensor mSensorG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = System.currentTimeMillis();
        f = new DecimalFormat("##0.00000");
        tf = findViewById(R.id.tf);
        tf2 = findViewById(R.id.tf2);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //mSensorG = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mSensorG, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mSensor);
        //mSensorManager.unregisterListener(this, mSensorG);
    }

    public void onSensorChanged(SensorEvent var1){
        time = System.currentTimeMillis() - time;
        distance[0] += (linear_acceleration[0]*time*time)/2;
        distance[1] += (linear_acceleration[1]*time*time)/2;
        distance[2] += (linear_acceleration[2]*time*time)/2;
        /*
        switch (var1.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:

                // In this example, alpha is calculated as t / (t + dT),
                // where t is the low-pass filter's time-constant and
                // dT is the var1 delivery rate.

                final double alpha = 0.8;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * var1.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * var1.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * var1.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = var1.values[0] - gravity[0];
                linear_acceleration[1] = var1.values[1] - gravity[1];
                linear_acceleration[2] = var1.values[2] - gravity[2];
                Log.i("x", Float.toString(var1.values[0]));
                Log.i("y", Float.toString(var1.values[1]));
                Log.i("z", Float.toString(var1.values[2]));
                tf.setText(Double.toString(linear_acceleration[0])+" "+Double.toString(linear_acceleration[1])+" "+Double.toString(linear_acceleration[2]));

                Log.i("x", Double.toString(var1.values[0]));
                Log.i("y", Double.toString(var1.values[1]));
                Log.i("z", Double.toString(var1.values[2]));
                break;*/
        time = System.currentTimeMillis();
        linear_acceleration[0] = var1.values[0];
        linear_acceleration[1] = var1.values[1];
        linear_acceleration[2] = var1.values[2];

        tf.setText(f.format(linear_acceleration[0])+" "+f.format(linear_acceleration[1])+" "+f.format(linear_acceleration[2]));
        tf2.setText(f.format(distance[0])+" "+f.format(distance[1])+" "+f.format(distance[2]));
        Log.i("info", f.format(linear_acceleration[0])+" "+f.format(linear_acceleration[1])+" "+f.format(linear_acceleration[2]));
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}
