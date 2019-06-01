package hochschule.de.bachelorthesis.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BetterFloatingActionButton extends FloatingActionButton {

  public BetterFloatingActionButton(Context context) {
    super(context);
    construct();
  }

  public BetterFloatingActionButton(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    construct();
  }

  public BetterFloatingActionButton(Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    construct();
  }

  private void construct() {
    getDrawable().mutate().setTint(Color.WHITE);
  }
}
