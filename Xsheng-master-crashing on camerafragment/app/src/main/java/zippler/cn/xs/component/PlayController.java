package zippler.cn.xs.component;

/**
 * Created by Zipple on 2018/5/21.
 */

public interface PlayController {
    boolean play();

    boolean stop();

    void seekTo(int to);

    int getCurrentPosition();
}
