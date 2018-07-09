package be.kuleuven.softdev.hehuang.pokepelican.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.softdev.hehuang.pokepelican.R;
import be.kuleuven.softdev.hehuang.pokepelican.objects.Hotitems;

/**
 * Created by Administrator on 18/12/2016.
 */

public class Hot_item_adpters extends ArrayAdapter{
    List list = new ArrayList();     //放所有item的对象
    public Hot_item_adpters(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler
    {
        ImageView photo;
        TextView name;
        TextView rating;
    }

    @Override
    public void add(Object object) {

        list.add(object);    //把加进去的object加到list里面， 好来显示
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout,parent,false);
            handler = new DataHandler();
            handler.photo = (ImageView) row.findViewById(R.id.item_pic);
            handler.name = (TextView) row.findViewById(R.id.item_name);
            handler.rating = (TextView) row.findViewById(R.id.item_rate);
            row.setTag(handler);
        }
        else
        {
            handler = (DataHandler) row.getTag();
        }

        Hotitems itemsprovider ;
        itemsprovider = (Hotitems) this.getItem(position);
        handler.photo.setImageResource(itemsprovider.getPic_resoureces());
        handler.name.setText(itemsprovider.getItem_name());
        handler.rating.setText(itemsprovider.getItem_rating());


        return row;
    }
}
