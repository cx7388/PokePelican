package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View categoryLayout = inflater.inflate(R.layout.category_layout,
                container, false);

        return categoryLayout;
    }

}
