package devcom.marcos.accelerometerprinter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    TextView tf;
    private double[] gravity = new double[3];
    private double[] linear_acceleration = new double[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = findViewById(R.id.tf);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent var1){
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
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}
