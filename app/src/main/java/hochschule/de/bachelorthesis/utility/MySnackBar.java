package hochschule.de.bachelorthesis.utility;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.Snackbar;

public class MySnackBar {
  public static void createSnackBar(Context context, String text) {
    Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), text,
        Snackbar.LENGTH_LONG)
        .show();
  }
}
