package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.Application;

/**
 * Created by 黄河 on 12/27/2016.
 */

public class MyApplicaton extends Application{
    private static final String NAME = "";

    private String value;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setValue(NAME); // 初始化全局变量
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

}
