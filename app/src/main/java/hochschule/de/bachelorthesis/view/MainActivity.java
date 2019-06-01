package hochschule.de.bachelorthesis.view;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import hochschule.de.bachelorthesis.R;


public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getName();

  private NavController navController;
  private BottomNavigationView bottomNav;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //bottom bar transition + update action bar
    navController = Navigation.findNavController(this, R.id.main_activity_fragment_host);
    bottomNav = findViewById(R.id.main_activity_bottom_nav);
    NavigationUI.setupWithNavController(bottomNav, navController);
    NavigationUI.setupActionBarWithNavController(this, navController);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, (DrawerLayout) null);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }
}