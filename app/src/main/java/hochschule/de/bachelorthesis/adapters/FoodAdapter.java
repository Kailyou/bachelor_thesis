package hochschule.de.bachelorthesis.adapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<String> {

    public FoodAdapter(@NonNull Context context, @LayoutRes int resource,
                        @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}
