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

package com.trayis.simplimvp.presenter;

import android.content.Context;
import android.os.Bundle;

import com.trayis.simplimvp.view.SimpliView;

import io.reactivex.disposables.CompositeDisposable;

import static com.trayis.simplimvp.presenter.SimpliPresenter.State.DESTROYED;
import static com.trayis.simplimvp.presenter.SimpliPresenter.State.INITIALIZED;
import static com.trayis.simplimvp.presenter.SimpliPresenter.State.VIEW_ATTACHED;
import static com.trayis.simplimvp.presenter.SimpliPresenter.State.VIEW_DETACHED;

/**
 * Created by Mukund Desai on 2/17/17.
 */
public abstract class SimpliPresenter<V extends SimpliView> {

    protected String TAG;

    private boolean mInitialized;

    protected CompositeDisposable mDisposable;

    protected Context mContext;

    public SimpliPresenter() {
        TAG = getClass().getSimpleName();
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public void markInitialized() {
        mDisposable = new CompositeDisposable();
        mInitialized = true;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * The LifecycleState of a {@link SimpliPresenter}
     */
    public class State {
        private State() {
        }

        ;
        /**
         * Initial state of the presenter before {@link #onCreate()} got called
         */
        public static final int INITIALIZED = 0;

        /**
         * presenter is running fine but has no attached view. Either it gets a view  and
         * transitions to {@link #VIEW_ATTACHED} or the presenter gets destroyed {@link #DESTROYED}
         */
        public static final int VIEW_DETACHED = 1;

        /**
         * the view is attached. In any case, the next step will be {@link
         * #VIEW_DETACHED}
         */
        public static final int VIEW_ATTACHED = 2;

        /**
         * termination state. It will never change again.
         */
        public static final int DESTROYED = -1;
    }

    protected V mView;

    private volatile int mState = INITIALIZED;

    public void invalidateView() {
        mView = null;
    }

    public void bindView(V view) {
        this.mView = view;
    }

    public void onCreate() {
        moveToState(VIEW_DETACHED);
    }

    public void onCreateComplete() {

    }

    public void onSaveinstanceState(Bundle outState) {
    }

    public void onStart() {
        moveToState(VIEW_ATTACHED);
    }

    public void onStopBefore() {
        moveToState(VIEW_DETACHED);
    }

    public void onStopAfter() {
    }

    public void onDestroy() {
        moveToState(DESTROYED);
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void moveToState(int newState) {
        final int oldState = mState;
        if (newState != oldState) {
            switch (oldState) {
                case INITIALIZED:
                    if (newState == VIEW_DETACHED) {
                        // move allowed
                        break;
                    } else {
                        throw new IllegalStateException("Can't move to state " + newState
                                + ", the next state after INITIALIZED has to be VIEW_DETACHED");
                    }
                case VIEW_DETACHED:
                    if (newState == VIEW_ATTACHED) {
                        // move allowed
                        break;
                    } else if (newState == DESTROYED) {
                        // move allowed
                        break;
                    } else {
                        throw new IllegalStateException("Can't move to state " + newState
                                + ", the allowed states after VIEW_DETACHED are VIEW_ATTACHED or DESTROYED");
                    }
                case VIEW_ATTACHED:
                    // directly moving to DESTROYED is not possible, first detach the view
                    if (newState == VIEW_DETACHED) {
                        // move allowed
                        break;
                    } else {
                        throw new IllegalStateException("Can't move to state " + newState
                                + ", the next state after VIEW_ATTACHED has to be VIEW_DETACHED");
                    }
                case DESTROYED:
                    throw new IllegalStateException(
                            "once destroyed the presenter can't be moved to a different state");
            }

            mState = newState;
        }
    }

}
