package com.trayis.simplimvp.view;

import com.trayis.simplimvp.utils.Logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.security.acl.NotOwnerException;
import java.util.HashMap;

import static com.trayis.simplimvp.view.SimpliFragment.TAG;

/**
 * Created by Mukund Desai on 2/17/17.
 */
public class ViewWrapper<V extends SimpliView> implements InvocationHandler {

    private final String TAG;

    private V mView;

    private static final HashMap<Class<?>, Object> DEFAULTS;

    static {
        DEFAULTS = new HashMap<>();
        DEFAULTS.put(Boolean.TYPE, false);
        DEFAULTS.put(Byte.TYPE, (byte) 0);
        DEFAULTS.put(Character.TYPE, '\000');
        DEFAULTS.put(Double.TYPE, 0.0d);
        DEFAULTS.put(Float.TYPE, 0.0f);
        DEFAULTS.put(Integer.TYPE, 0);
        DEFAULTS.put(Long.TYPE, 0L);
        DEFAULTS.put(Short.TYPE, (short) 0);
    }

    public ViewWrapper(V view) {
        this.mView = view;
        this.TAG = view.getClass().getSimpleName();
    }

    public V prepareViewDelegator() {
        try {
            Type[] types = ((ParameterizedType) mView.getClass().getGenericSuperclass()).getActualTypeArguments();
            Class<?> viewClass = null;
            for (Type type : types) {
                if (SimpliView.class.isAssignableFrom((Class<?>) type)) {
                    viewClass = (Class<?>) type;
                    break;
                }
            }
            return (V) Proxy.newProxyInstance(viewClass.getClassLoader(), new Class<?>[]{viewClass}, this);
        } catch (NullPointerException e) {
            Logging.e(TAG, e.getMessage(), e);
        }

        return mView;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (mView == null) {
            Class<?> returnType = method.getReturnType();
            return DEFAULTS.get(returnType);
        }

        return method.invoke(mView, args);
    }

    public void dropView() {
        mView = null;
    }
}
