package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import be.kuleuven.softdev.hehuang.pokepelican.mCloud.CloudinaryClient;
import be.kuleuven.softdev.hehuang.pokepelican.mPicasso.PicassoClient;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Intent intent = getIntent();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about_us);
        Button bfp = (Button) findViewById(R.id.bfp);
        final ImageView img = (ImageView) findViewById(R.id.selfy);   // 找到ImageView

        bfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PicassoClient.downloadImage(AboutUs.this,CloudinaryClient.resize(),img);

            }
        });

    }




}
