package com.meishe.sdkdemo.utils;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by CaoZhiChao on 2018/9/21 18:58
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
