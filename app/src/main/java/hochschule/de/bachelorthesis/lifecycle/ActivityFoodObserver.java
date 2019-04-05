package hochschule.de.bachelorthesis.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class ActivityFoodObserver implements LifecycleObserver {

    private String TAG = getClass().getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() { Log.i(TAG, "ON_CREATE"); }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() { Log.i(TAG, "ON_CREATE"); }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() { Log.i(TAG, "ON_CREATE"); }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() { Log.i(TAG, "ON_CREATE"); }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() { Log.i(TAG, "ON_CREATE"); }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() { Log.i(TAG, "ON_CREATE"); }
}
