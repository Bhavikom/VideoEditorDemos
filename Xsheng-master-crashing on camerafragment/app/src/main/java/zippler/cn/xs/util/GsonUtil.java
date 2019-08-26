package zippler.cn.xs.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Zipple on 2018/5/22.
 */

public class GsonUtil {
    private List<?> parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, new TypeToken<List<?>>() {}.getType());
    }
}
