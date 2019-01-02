package hochschule.de.bachelorthesis.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.activities.FoodActivity;

public class MyFoodFragment extends Fragment {

    String[] MAIN_TEXTS = {
            "Product 1",
            "Product 2",
            "Product 3",
            "Product 4",
            "Product 5",
            "Product 6",
    };

    String[] SUB_TEXTS = {
            "sub text 1",
            "sub text 2",
            "sub text 3",
            "sub text 4",
            "sub text 5",
            "sub text 6",
    };

    String[] META_TEXTS = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_food, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView lv = getView().findViewById(R.id.list);
        FoodAdapter adapter = new FoodAdapter();

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), FoodActivity.class));
            }
        });
    }

    class FoodAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return MAIN_TEXTS.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlayout, null);
            TextView mainText = view.findViewById(R.id.mainText);
            mainText.setText(MAIN_TEXTS[i]);

            TextView subText = view.findViewById(R.id.subText);
            subText.setText(SUB_TEXTS[i]);

            TextView meta = view.findViewById(R.id.metaData);
            meta.setText(META_TEXTS[i]);

            return view;
        }
    }
}

