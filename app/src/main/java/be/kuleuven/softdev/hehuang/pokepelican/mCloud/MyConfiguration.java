package be.kuleuven.softdev.hehuang.pokepelican.mCloud;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 19/12/2016.
 */

public class MyConfiguration {

    public static HashMap getMyConfige()
    {
        HashMap config = new HashMap();
        config.put("cloud_name","zh325700");
        config.put("api_key","655772832243321");
        config.put("api_secret","1PVfGLgqBnBBXkdZTekwVOT6jqU");

        return config;
    }
}
