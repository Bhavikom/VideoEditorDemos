/**
 *  TuSDK
 *  droid-sdk-eva$
 *  org.lsque.tusdkevademo$
 *  @author  H.ys
 *  @Date    2019/7/2$ 12:58$
 *  @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 *
 */
package org.lsque.tusdkevademo

import android.content.Context

/**
 * TuSDK
 * $desc$
 *
 * @author        H.ys
 * @Date        $data$ $time$
 * @Copyright    (c) 2019 tusdk.com. All rights reserved.
 *
 */

data class AudioItem(var audioPath: String,var audioName : String)

class AudioItemFactory {
    companion object{

        private val nameList : Array<String> = arrayOf("City Sunshine","Eye of Forgiveness","Lovely Piano Song","Motions","Pickled Pink","Rush")
        private val dirList : Array<String> = arrayOf("city_sunshine.mp3","eye_of_forgiveness.mp3","lovely_piano_song.mp3","motions.mp3","pickled_pink.mp3","rush.mp3")

        fun getAudioItemList(context: Context) : List<AudioItem>{
            var audioList : ArrayList<AudioItem> = java.util.ArrayList()
            context.assets.list("audios")
            for (i in 0 until nameList.size){
                audioList.add(AudioItem("audios/${dirList[i]}", nameList[i]))
            }
            return audioList
        }
    }
}