package com.trayis.simplimvp.utils;

/**
 * Created by mudesai on 10/9/17.
 */

public class SimpliProviderUtil {

    private static final String TAG = "SimpliProviderUtil";

    private static final SimpliProviderUtil INSTANCE = new SimpliProviderUtil();

    private SimpliProvider provider;

    private SimpliProviderUtil() {
    }

    public static SimpliProviderUtil getInstance() {
        return INSTANCE;
    }

    public void setProvider(SimpliProvider provider) {
        if (this.provider != null) {
            Logging.w(TAG, "provider already set");
            return;
        }
        this.provider = provider;
    }

    public SimpliProvider getProvider() {
        return provider;
    }
}
