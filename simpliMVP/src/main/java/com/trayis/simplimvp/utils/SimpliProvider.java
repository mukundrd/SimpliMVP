package com.trayis.simplimvp.utils;

import com.trayis.simplimvp.presenter.SimpliPresenter;
import com.trayis.simplimvp.view.SimpliView;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by mudesai on 10/9/17.
 */

public interface SimpliProvider<P extends SimpliPresenter, V extends SimpliView> {

    P getPresenter(V view) throws InvalidPropertiesFormatException;

}
