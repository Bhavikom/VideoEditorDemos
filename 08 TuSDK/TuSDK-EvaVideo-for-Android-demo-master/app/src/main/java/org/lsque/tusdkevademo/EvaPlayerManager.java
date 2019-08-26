/**
 * TuSDK
 * TuSDKEvaDemo
 * EvaPlayerManager.java
 *
 * @author H.ys
 * @Date 2019/7/3 15:04
 * @Copyright (c) 2019 tusdk.com. All rights reserved.
 */
package org.lsque.tusdkevademo;

import org.lasque.tusdk.eva.TuSdkEvaPlayer;
import org.lasque.tusdk.eva.TuSdkEvaPlayerImpl;

/**
 * TuSDK
 * $desc$
 *
 * @author H.ys
 * @Date $data$ $time$
 * @Copyright (c) 2019 tusdk.com. All rights reserved.
 */
public class EvaPlayerManager {

    private static TuSdkEvaPlayerImpl mEvaPlayer;

    public static TuSdkEvaPlayerImpl getInstance() {
        mEvaPlayer = new TuSdkEvaPlayerImpl();
        return mEvaPlayer;
    }
}
