package zippler.cn.xs.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.adapter.UserVideoPreviewAdapter;
import zippler.cn.xs.util.FileUtil;
import zippler.cn.xs.util.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserVideoFragment extends Fragment {

    private RecyclerView recyclerView ;
    private ImageView imageView;
    public UserVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_video, container, false);

        recyclerView =  view.findViewById(R.id.my_video_recycler_view);
        imageView =  view.findViewById(R.id.none_img_view);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //how to get lists?
        String basePath = FileUtil.getCamera2Path()+"deploy"+ File.separator;
        List<String> pathList = FileUtil.traverseFolder(basePath);
        if (pathList!=null&&pathList.size()>0){
            imageView.setVisibility(View.GONE);
//            Collections.reverse(pathList);
            recyclerView.setAdapter(new UserVideoPreviewAdapter(getContext(),pathList));
        }else{
            imageView.setVisibility(View.VISIBLE);
        }
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 0, false));
        return view;
    }

}
