package zippler.cn.xs.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zippler.cn.xs.gson.VideoGson;

/**
 * Created by Zipple on 2018/5/17.
 * It is used to upload files or get json data.
 * ok http.
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static List<VideoGson> result = new ArrayList<>();
    public static List<VideoGson> get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " );
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String responseStr = response.body().string();
                Gson gson = new Gson();
                result = gson.fromJson(responseStr, new TypeToken<List<VideoGson>>() {}.getType());
            }
        });
        return result;
    }
}
