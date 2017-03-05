package com.trayis.simplimvp.view;

import com.trayis.simplimvp.presenter.SimpliPresenter;

/**
 * Created by Mukund Desai on 2/17/17.
 */
public interface SimpliView <P extends SimpliPresenter> {

    boolean postToMessageQueue(Runnable runnable);

    void initializePresenter();
}
