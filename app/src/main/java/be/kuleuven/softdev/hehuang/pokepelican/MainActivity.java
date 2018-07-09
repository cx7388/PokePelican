package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import be.kuleuven.softdev.hehuang.pokepelican.mPicasso.PicassoClient;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 *
 * @author guolin
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {
    private MyApplicaton app;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    AccessToken accessToken;
    String json_url;
    String Jason_string;
    String username;
    String userid;
    String userlink;
    String facebookID;
    ImageView imageView;
    /**
     * 用于展示Main的Fragment
     */
    private MainFragment mainFragment;

    /**
     * 用于展示Category的Fragment
     */
    private CategoryFragment categoryFragment;

    /**
     * 用于展示Cart的Fragment
     */
    private CartFragment cartFragment;

    /**
     * 用于展示设置的Fragment
     */
    private SettingFragment settingFragment;

    /**
     * Main界面布局
     */
    private View mainLayout;

    /**
     * Category界面布局
     */
    private View categroyLayout;

    /**
     * Cart界面布局
     */
    private View cartLayout;

    /**
     * Profile界面布局
     */
    private View settingLayout;

    /**
     * 在Tab布局上显示Main图标的控件
     */
    private ImageView mainImage;

    /**
     * 在Tab布局上显示Category图标的控件
     */
    private ImageView categoryImage;

    /**
     * 在Tab布局上显示Cart图标的控件
     */
    private ImageView cartImage;

    /**
     * 在Tab布局上显示profile图标的控件
     */
    private ImageView settingImage;

    /**
     * 在Tab布局上显示Main标题的控件
     */
    private TextView mainText;

    /**
     * 在Tab布局上显示Category标题的控件
     */
    private TextView categoryText;

    /**
     * 在Tab布局上显示Cart标题的控件
     */
    private TextView cartText;

    /**
     * 在Tab布局上显示Profile标题的控件
     */
    private TextView settingText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        AppEventsLogger.activateApp(this.getApplication());
        app = (MyApplicaton) getApplication();
        facebookID = app.getValue();

        //宣告callback Manager

        callbackManager = CallbackManager.Factory.create();

        //找到login button

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };
        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("be.kuleuven.softdev.hehuang.pokepelican",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.e("MY KEY HASH:", sign);
//                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            Log.v("FB", "Logged, user id=" + profile.getId());
        }
        else{
            Log.v("FB", "not Logged" );
        }
        //幫loginButton增加callback function

        //這邊為了方便 直接寫成inner class
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //accessToken之後或許還會用到 先存起來

                accessToken = loginResult.getAccessToken();

                Log.d("FB", "access token got.");

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.d("FB", "complete");
                                Log.d("FB", object.optString("name"));
                                username = object.optString("name");
                                Log.d("FB", object.optString("link"));
                                userlink = object.optString("link");
                                Log.d("FB", object.optString("id"));
                                userid = object.optString("id");
                                app.setValue(userid);
                                facebookID = app.getValue();
                                String profilePicUrl = "https://graph.facebook.com/" + userid + "/picture?type=large";
                                final ImageView img = (ImageView) findViewById(R.id.imgPhoto);
                                PicassoClient.downloadImage(MainActivity.this, profilePicUrl, img);
                                json_url = "http://studev.groept.be/api/a16_sd306/";
                                StringBuilder sb = new StringBuilder();
                                sb.append(json_url + "Register/");
                                sb.append(userid + "/");
                                sb.append(username + "/");
                                json_url = sb.toString();
                                String oldurl = json_url;
                                String newurl = oldurl.replaceAll(" ", "%20");
                                Log.e("the url is", newurl);
                                new LogUpload().execute(newurl);
                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();


            }


            //登入取消

            @Override
            public void onCancel() {
                // App code

                Log.d("FB", "CANCEL");
            }

            //登入失敗

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.d("FB", exception.toString());
            }
        });

    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        mainLayout = findViewById(R.id.main_layout);
        categroyLayout = findViewById(R.id.category_layout);
        cartLayout = findViewById(R.id.cart_layout);
        settingLayout = findViewById(R.id.setting_layout);
        mainImage = (ImageView) findViewById(R.id.main_image);
        categoryImage = (ImageView) findViewById(R.id.category_image);
        cartImage = (ImageView) findViewById(R.id.cart_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        mainText = (TextView) findViewById(R.id.main_text);
        categoryText = (TextView) findViewById(R.id.category_text);
        cartText = (TextView) findViewById(R.id.cart_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        mainLayout.setOnClickListener(this);
        categroyLayout.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_layout:
                // 当点击了Main tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.category_layout:
                // 当点击了Category tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.cart_layout:
                // 当点击了cart tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                // 当点击了设置setting tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了Main tab时，改变控件的图片和文字颜色
                mainImage.setImageResource(R.drawable.ic_language_white_24dp);
                mainText.setTextColor(Color.WHITE);
                if (mainFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mainFragment = new MainFragment();
                    transaction.add(R.id.content, mainFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mainFragment);
                }
                break;
            case 1:
                // 当点击了Category tab时，改变控件的图片和文字颜色
                categoryImage.setImageResource(R.drawable.ic_assessment_white_24dp);
                categoryText.setTextColor(Color.WHITE);
                if (categoryFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    categoryFragment = new CategoryFragment();
                    transaction.add(R.id.content, categoryFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(categoryFragment);
                }
                break;
            case 2:
                // 当点击了Cart tab时，改变控件的图片和文字颜色
                cartImage.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
                cartText.setTextColor(Color.WHITE);
                if (cartFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    cartFragment = new CartFragment();
                    transaction.add(R.id.content, cartFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(cartFragment);
                }
                break;
            case 3:
            default:
                // 当点击了Profile tab时，改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.ic_assignment_ind_white_24dp);
                settingText.setTextColor(Color.WHITE);
                if (settingFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.content, settingFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        mainImage.setImageResource(R.drawable.ic_language_white_24dp);
        mainText.setTextColor(Color.parseColor("#82858b"));
        categoryImage.setImageResource(R.drawable.ic_assessment_white_24dp);
        categoryText.setTextColor(Color.parseColor("#82858b"));
        cartImage.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
        cartText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.ic_assignment_ind_white_24dp);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (cartFragment != null) {
            transaction.hide(cartFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
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
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Search(MenuItem item) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void ChangeLocation(MenuItem item) {
        Intent intent = new Intent(this, DetectLocation.class);
        startActivity(intent);
    }

    public void upload(View view) {
        Intent intent = new Intent(this, UpLoad.class);
        startActivity(intent);
    }

    public void setting(View view) {
        Intent intent = new Intent(this, ChangeSetting.class);
        startActivity(intent);
    }

    public void aboutus(View view) {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    public void electronicsBotton(View view) {
        Intent intent = new Intent(this, Electronics.class);
        startActivity(intent);
    }

    public void beautyBotton(View view) {
        Intent intent = new Intent(this, Beauty.class);
        startActivity(intent);
    }

    public void forhomeBotton(View view) {
        Intent intent = new Intent(this, ForHome.class);
        startActivity(intent);
    }

    public void booksBotton(View view) {
        Intent intent = new Intent(this, Books.class);
        startActivity(intent);
    }

    public void toysBotton(View view) {
        Intent intent = new Intent(this, Toys.class);
        startActivity(intent);
    }

    public void bikesBotton(View view) {
        Intent intent = new Intent(this, Bikes.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private class LogUpload extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((Jason_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Jason_string);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
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
            Jason_string = null; //把json的内容清空，为了下一次。
            Toast.makeText(getApplicationContext(), "Data is uploaded", Toast.LENGTH_LONG).show();
        }
    }
}