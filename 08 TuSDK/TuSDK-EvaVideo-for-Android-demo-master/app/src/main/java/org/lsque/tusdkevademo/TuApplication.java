package org.lsque.tusdkevademo;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkApplication;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.utils.TuSdkEva;

/**
 * @author MirsFang
 * <p>
 * org.lsque.tusdkevademo
 * @date 2019-06-27 15:25
 */
public class TuApplication extends TuSdkApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        TuSdkEva.register();
        // 设置输出状态，建议在接入阶段开启该选项，以便定位问题。
        this.setEnableLog(true);
        /**
         *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
         *
         *  @param appkey 应用秘钥 (请前往 http://tusdk.com 申请秘钥)
         */
        TuSdk.setResourcePackageClazz(org.lsque.tusdkevademo.R.class);
        this.initPreLoader(this.getApplicationContext(), "88ac0898a18d73d0-04-ewdjn1");

    }


}
