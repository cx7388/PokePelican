package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cartLayout = inflater.inflate(R.layout.cart_layout,
                container, false);
        return cartLayout;
    }

}
