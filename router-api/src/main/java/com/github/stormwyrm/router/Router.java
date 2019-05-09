package com.github.stormwyrm.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class Router {
    //保存key-activity之间的映射关系
    private static Map<String, Class<? extends Activity>> sRouter = new HashMap<>();

    private Router() {
    }

    /**
     * 加载编译生成AptRouterInitializer类，该类会调用register将映射关系注入到sRouter中
     */
    public static void init() {
        try {
            Class.forName("com.github.stormwyrm.router.AptRouterInitializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过register方法向map中注入
     */
    public static void register(IRouterInitializer routerInitializer) {
        routerInitializer.init(sRouter);
    }

    public static boolean startActivity(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Class clazz = getActivityClass(url);
        if (clazz != null) {
            Intent intent = new Intent(context, clazz);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean startActivityForResult(Activity activity, String url, int requestCode) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Class clazz = getActivityClass(url);
        if (clazz != null) {
            Intent intent = new Intent(activity, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    private static Class getActivityClass(String url) {
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

}
