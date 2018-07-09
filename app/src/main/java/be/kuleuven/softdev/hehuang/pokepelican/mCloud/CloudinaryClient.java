package be.kuleuven.softdev.hehuang.pokepelican.mCloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

/**
 * Created by Administrator on 19/12/2016.
 */

public class CloudinaryClient {

    public  static  String getRoundedCorners()
    {
        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
        //Manipulate
        Transformation t = new Transformation();
        t.radius(60);
        String picname="sheetblue_cdfev0.jpg";
        return cloud.url().transformation(t).generate(picname);
    }
    public  static  String resize()
    {
        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
        //Manipulate
        Transformation t = new Transformation();
        t.width(300);
        t.height(250);

        return cloud.url().transformation(t).generate("sheetblue_cdfev0.jpg");  //返回调整以后的pic url
    }

}
