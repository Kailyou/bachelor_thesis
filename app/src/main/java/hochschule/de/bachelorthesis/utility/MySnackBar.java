package hochschule.de.bachelorthesis.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

/**
 * @author Maik Thielen
 * <p>
 * Helper Class with a static function to create faster Snack Bars.
 * <p>
 * Also, it will hide the keyboard while the Snack Bar is active.
 */
public class MySnackBar {
    public static void createSnackBar(Context context, View view, String message) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
