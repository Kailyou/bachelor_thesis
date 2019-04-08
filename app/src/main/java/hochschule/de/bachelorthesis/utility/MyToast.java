package hochschule.de.bachelorthesis.utility;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import hochschule.de.bachelorthesis.R;

public class MyToast {

    public static void createToast(Context context, String text) {
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);

        //Gets the actual oval background of the Toast then sets the colour filter
        View view = toast.getView();
        view.setBackgroundResource(R.color.colorPrimary);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
