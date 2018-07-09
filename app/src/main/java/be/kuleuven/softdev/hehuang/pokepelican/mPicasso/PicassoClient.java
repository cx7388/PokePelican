package be.kuleuven.softdev.hehuang.pokepelican.mPicasso;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.kuleuven.softdev.hehuang.pokepelican.R;


/**
 * Created by Administrator on 19/12/2016.
 */

public class PicassoClient {

    public  static void downloadImage(Context c, String url, ImageView img)
    {
        if(url!=null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.ic_perm_identity_black_48dp).into(img);
        }
        else
        {
            Picasso.with(c).load(R.drawable.ic_perm_identity_black_48dp).into(img);
        }
    }
}
