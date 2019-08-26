/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/1$ 16:05$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.os.Parcel
import android.os.Parcelable

data class AlbumInfo(var path :String,var type : AlbumItemType,var duration : Int,var createDate : Long)

enum class AlbumItemType{
    Image,Video;
}