package be.kuleuven.softdev.hehuang.pokepelican;
import be.kuleuven.softdev.hehuang.pokepelican.objects.Hotitems;
import  be.kuleuven.softdev.hehuang.pokepelican.Adapters.Hot_item_adpters;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainFragment extends Fragment {
    ListView listView;
    int[] item_pic_resource = {R.drawable.girl,R.drawable.girl2,R.drawable.jinx,R.drawable.jinx2,R.drawable.view};
    String[] item_name;
    String[] item_rating;
    Hot_item_adpters hot_item_adpters ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainLayout = inflater.inflate(R.layout.main_layout,
                container, false);
        listView = (ListView)mainLayout.findViewById(R.id.list_main);     // here changed
        item_name = getResources().getStringArray(R.array.Hottest_name);
        item_rating = getResources().getStringArray(R.array.Rate);
        int i =0;
        hot_item_adpters = new Hot_item_adpters(getActivity().getApplicationContext(),R.layout.row_layout); //这两句话是把row_layout和ListView联系起来的。
        listView.setAdapter(hot_item_adpters);
        for(String names: item_name)
        {
                Hotitems hotitems = new Hotitems(item_pic_resource[i],names,item_rating[i]);
                i++;
                hot_item_adpters.add(hotitems);   //把hotitem的对象添加到Adapter里面

        }



        return mainLayout;
    }

}