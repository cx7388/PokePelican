package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class Bikes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);
        Intent intent = getIntent();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_bikes);
    }
}
