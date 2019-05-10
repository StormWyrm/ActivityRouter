package com.github.stormwyrm.router.initializer;

import android.app.Activity;

import java.util.Map;

public interface IRouterInitializer {
    void init(Map<String, Class<? extends Activity>> sRouter);
}
