package kr.co.company.work3;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    MySurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MySurfaceView(this);
        setContentView(view);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
}