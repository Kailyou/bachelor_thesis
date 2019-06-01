package hochschule.de.bachelorthesis.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import hochschule.de.bachelorthesis.R;

public class MyToast {

  public static void createToast(Context context, String text) {
    Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), text,
        Snackbar.LENGTH_LONG)
        .show();
  }
}
