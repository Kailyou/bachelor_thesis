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
import android.widget.ListView;

import java.util.ArrayList;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.activities.FoodActivity;

public class MyFoodFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_food, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> al = new ArrayList<>();
        al.add("Milk");
        al.add("Pizza");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.food_list_item, al);

        ListView lv = getView().findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), FoodActivity.class));
            }
        });
    }
}

