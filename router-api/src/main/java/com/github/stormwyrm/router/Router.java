package com.github.stormwyrm.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import com.github.stormwyrm.router.data.NavigationCallback;
import com.github.stormwyrm.router.data.PostCard;
import com.github.stormwyrm.router.initializer.IRouterInitializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
    //保存key-activity之间的映射关系
    private static Map<String, Class<? extends Activity>> sRouter = new HashMap<>();
    private static boolean hasInited;
    private static Application mApplication;

    private Router() {
    }

    public static Router getInstance() {
        return Holder.instance;
    }

    /**
     * 通过register方法向map中注入
     */
    public static void register(IRouterInitializer routerInitializer) {
        routerInitializer.init(sRouter);
    }

    /**
     * 加载编译生成AptRouterInitializer类，该类会调用register将映射关系注入到sRouter中
     */
    public static void init(Application application) {
        if (!hasInited) {
            hasInited = true;
            mApplication = application;
            try {
                Class.forName("com.github.stormwyrm.router.AptRouterInitializer");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Router only allow init once.");
        }
    }

    /**
     * 对类参数进行注入，仅支持activity
     *
     * @param thiz 需要注入的类
     */
    public static void inject(Object thiz) {
        String className = thiz.getClass().getCanonicalName() + "$$AutowiredInitializer";
        try {
            Class<?> aClass = Class.forName(className);
            Object instance = aClass.newInstance();
            Method method = aClass.getDeclaredMethod("inject", Object.class);
            method.setAccessible(true);
            method.invoke(instance, thiz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public PostCard build(@NonNull String path) {
        return new PostCard(path);
    }

    public PostCard build(@NonNull String path, @NonNull Bundle extras) {
        return new PostCard(path, extras);
    }

    public Object navigation(Context context, PostCard postCard, int requestCode, NavigationCallback callback) {
        Context curContext = context == null ? mApplication : context;
        if (postCard != null) {
            String path = postCard.getPath();
            Class activityClass = getActivityClass(path);
            if (TextUtils.isEmpty(path) || activityClass == null) {
                if (callback != null) {
                    callback.onLost(postCard);
                }
            } else {
                if (callback != null) {
                    callback.onFound(postCard);
                }
                Intent intent = new Intent(curContext, activityClass);
                intent.putExtras(postCard.getExtras());
                if (!(curContext instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(curContext, intent, postCard, requestCode, callback);
            }
        } else {
            if (callback != null) {
                callback.onLost(new PostCard(""));
            }
        }
        return null;
    }

    private void startActivity(Context context, Intent intent, PostCard postCard, int requestCode, NavigationCallback callback) {
        if (requestCode >= 0) {
            ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, null);
        } else {
            ActivityCompat.startActivity(context, intent, null);
        }
        if (callback != null)
            callback.onArrival(postCard);
    }


    private Class getActivityClass(String url) {
        String key;
        int tmp = url.indexOf('?');
        if (tmp > 0) {
            key = url.substring(0, tmp);
        } else {
            key = url;
        }
        Class<? extends Activity> clazz = sRouter.get(key);
        return clazz;
    }

    private static class Holder {
        private static final Router instance = new Router();
    }

}
