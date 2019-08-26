package zippler.cn.xs.gson;

import java.util.List;

/**
 * Created by Zipple on 2018/5/24.
 */

public class UTGson {


    /**
     * code : 200
     * data : ["url1","url2"]
     * msg : ok
     * status : true
     */

    private int code;
    private String msg;
    private boolean status;
    private List<String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
