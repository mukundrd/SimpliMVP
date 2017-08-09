package com.trayis.simplimvp.utils;

import android.content.res.Configuration;
import android.os.Bundle;

import com.trayis.simplimvp.presenter.SimpliPresenter;
import com.trayis.simplimvp.view.SimpliFragment;
import com.trayis.simplimvp.view.SimpliView;
import com.trayis.simplimvp.view.ViewWrapper;

/**
 * Created by Mukund Desai on 2/17/17.
 */
public class SimpliDelegator<P extends SimpliPresenter<V>, V extends SimpliView> {

    private static final String TAG = "SimpliDelegator";

    private final P presenter;

    private final ViewWrapper<V> viewWrapper;

    private final V view;

    public SimpliDelegator(P presenter, V view) {
        this.presenter = presenter;
        this.view = view;
        viewWrapper = new ViewWrapper<V>(view);
        presenter.bindView((V) viewWrapper.prepareViewDelegator());
    }

    public void onConfigurationChangedAfterSuper(Configuration newConfig) {
        presenter.invalidateView();
    }

    public void onCreateAfterSuper(Bundle savedInstanceState) {
        presenter.onCreate();
    }

    public void onDestroyAfterSuper() {
        viewWrapper.dropView();
        presenter.onDestroy();
    }

    public void onSaveInstanceStateAfterSuper(Bundle outState) {
        presenter.onSaveinstanceState(outState);
    }

    public void onStartAfterSuper() {
        if (!presenter.isInitialized()) {
            view.initializePresenter();
            presenter.markInitialized();
            presenter.onStart();
        }
    }

    public void onStopBeforeSuper() {
        presenter.onStopBefore();
    }

    public void onStopAfterSuper() {
        presenter.onStopAfter();
    }

    public void onPostCreateAfterSuper() {
        if (view instanceof SimpliFragment) {
            try {
                presenter.onCreate();
            } catch (IllegalStateException e) {
                Logging.e(TAG, e.getMessage(), e);
            }
        }
        presenter.onCreateComplete();
    }
}
