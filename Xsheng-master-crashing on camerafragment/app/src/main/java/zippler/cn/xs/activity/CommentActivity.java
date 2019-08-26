package zippler.cn.xs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.component.CommentPopView;
import zippler.cn.xs.entity.Comment;

/**
 * Created by ice on 2018/5/17.
 */

public class CommentActivity extends AppCompatActivity{

    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        rootView = findViewById(R.id.root);
    }

    public void showComment(View v) throws CloneNotSupportedException {

        List<Comment> comments = new ArrayList<>();

        Comment comment = new Comment();
        comment.setContent("菊花残，满地伤，你的笑容已泛黄，花落人断肠，我心事静静躺！");
        comment.setTime("2小时前");

        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);


        CommentPopView commentPopView = new CommentPopView(this,comments);
        commentPopView.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    }

}
