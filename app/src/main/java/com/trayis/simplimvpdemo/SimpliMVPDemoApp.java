package com.trayis.simplimvpdemo;

import android.app.Application;

import com.trayis.simplimvp.utils.Logging;
import com.trayis.simplimvp.utils.SimpliProviderUtil;
import com.trayis.simplimvpannotation.generated.SimpliMVPProvider;

/**
 * Created by mudesai on 10/10/17.
 */

public class SimpliMVPDemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logging.initLogger();
        SimpliProviderUtil.getInstance().setProvider(SimpliMVPProvider.getInstance());
    }
}
