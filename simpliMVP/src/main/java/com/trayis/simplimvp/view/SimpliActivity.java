/*
 * Copyright (C) 2017 Mukund Desai (mukundrd)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trayis.simplimvp.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.trayis.simplimvp.presenter.SimpliPresenter;
import com.trayis.simplimvp.utils.Logging;
import com.trayis.simplimvp.utils.SimpliDelegator;
import com.trayis.simplimvp.utils.SimpliProviderUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.InvalidPropertiesFormatException;

/**
 * Created by Mukund Desai on 2/17/17.
 */

public abstract class SimpliActivity<P extends SimpliPresenter<V>, V extends SimpliView> extends AppCompatActivity implements SimpliBase<P, V>, SimpliView {

    protected String TAG;

    private final SimpliDelegator<P, V> mDelegate;

    protected P mPresenter;

    public SimpliActivity() {
        TAG = getClass().getSimpleName();
        mDelegate = new SimpliDelegator(getPresenter(), this);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDelegate.onConfigurationChangedAfterSuper(newConfig);
    }

    @Override
    public boolean postToMessageQueue(final Runnable runnable) {
        return getWindow().getDecorView().post(runnable);
    }

    public P getPresenter() {
        if (mPresenter == null) {
            try {
                mPresenter = (P) SimpliProviderUtil.getInstance().getProvider().getPresenter(this);
            } catch (InvalidPropertiesFormatException e) {
                Logging.e(TAG, e.getMessage(), e);
            }
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreateAfterSuper(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreateAfterSuper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroyAfterSuper();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceStateAfterSuper(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDelegate.onStartAfterSuper();
    }

    @Override
    protected void onStop() {
        mDelegate.onStopBeforeSuper();
        super.onStop();
        mDelegate.onStopAfterSuper();
    }

    @Override
    public String toString() {
        String presenter = mPresenter == null ? "null" : mPresenter.getClass().getSimpleName() + "@" + Integer.toHexString(mPresenter.hashCode());
        return getClass().getSimpleName() + ":" + SimpliActivity.class.getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{presenter = " + presenter + "}";
    }
}
