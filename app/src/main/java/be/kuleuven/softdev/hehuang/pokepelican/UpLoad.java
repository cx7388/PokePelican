package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;


public class UpLoad extends Activity implements View.OnClickListener{

    Spinner spinnercata,spinnerlocation;
    ArrayAdapter<CharSequence> spadaper,spadpterlocation;  // for spinner

    ImageView imageToUplaod;
    Button bUploadImage, bSelectImage;
    EditText uploadImageName,itemPrice,itemDiscribtion;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "UpLoad";

    String itname;
    String itdiscribtion;
    String itlocation;
    String itcatagory;
    String itprice;
    String json_url;
    String json_string;     // json 的返回
    String Jason_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
//        Intent intent = getIntent();
        spinnercata = (Spinner) findViewById(R.id.spItemCatagory);
        spinnerlocation = (Spinner) findViewById(R.id.spItemLocation);
        spadaper = ArrayAdapter.createFromResource(this,R.array.Catagories,android.R.layout.simple_spinner_item);
        spadpterlocation = ArrayAdapter.createFromResource(this,R.array.Locations,android.R.layout.simple_spinner_item);
        spadaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spadpterlocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercata.setAdapter(spadaper);
        spinnerlocation.setAdapter(spadpterlocation);
        spinnercata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itcatagory =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itlocation =  parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_up_load);

        imageToUplaod = (ImageView) findViewById(R.id.imageToUplaod);

        bSelectImage = (Button) findViewById(R.id.selectImageToUpload);
        bUploadImage = (Button) findViewById(R.id.bUploadImage);

        uploadImageName = (EditText) findViewById(R.id.etUplaodNames);
        itemPrice = (EditText) findViewById(R.id.etItemPrice);
        itemDiscribtion = (EditText) findViewById(R.id.etItemDiscribtion);

        bSelectImage.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.selectImageToUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploadImage:
                Bitmap image = ((BitmapDrawable)imageToUplaod.getDrawable()).getBitmap();
                itname = uploadImageName.getText().toString();
                itprice = itemPrice.getText().toString();
                itdiscribtion = itemDiscribtion.getText().toString();

                json_url = "http://studev.groept.be/api/a16_sd306/";
                StringBuilder sb = new StringBuilder();
                sb.append(json_url + "AddItem/");
                sb.append(itname + "/");
                sb.append(itprice + "/");
                sb.append(itcatagory + "/");
                sb.append(itdiscribtion + "/");
                sb.append(itlocation);
                json_url = sb.toString();

//                Intent intent = new Intent(UpLoad.this,AboutUs.class);
//                intent.putExtra("picname",itname);    // 把name通过intent传给其他类

                String oldurl = json_url;
                String newurl=oldurl.replaceAll(" ","%20");

                new UploadImage(image,itname).execute(newurl);
                break;

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageToUplaod.setImageURI(null);
            imageToUplaod.setImageURI(selectedImage);
            bSelectImage.setText("Change Image");
        }
    }

    private class UploadImage extends AsyncTask<String,Void,String> {
        String name;
        Bitmap image;
        private  UploadImage (Bitmap image , String name)
        {
            this.name=name;
            this.image=image;
            Toast.makeText(getApplicationContext(), "Item is uploading, please wait", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... urls) {

            Cloudinary cloudinary = new Cloudinary(MyConfiguration.getMyConfige());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((Jason_string = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(Jason_string);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                cloudinary.uploader().upload(bis,
                        ObjectUtils.asMap("folder", "itempic/", "public_id", name));

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            Jason_string=null; //把json的内容清空，为了下一次。
            Toast.makeText(getApplicationContext(),json_string,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Item is uploaded", Toast.LENGTH_LONG).show();
        }


    }
}
