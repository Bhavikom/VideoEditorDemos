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

import java.util.ArrayList;
import java.util.List;

import zippler.cn.xs.R;
import zippler.cn.xs.adapter.UserFavVidPreAdapter;
import zippler.cn.xs.util.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFavoriteFragment extends Fragment {

    private RecyclerView recyclerView ;
    private ImageView imageView ;
    public UserFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorite, container, false);

        recyclerView = view.findViewById(R.id.my_fav_recycler_view);
        imageView = view.findViewById(R.id.none_img_view_fav);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //the data should be loaded from internet..
        List<String> videoList = new ArrayList<>();
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v1);
        //ImageList
        if (videoList.size()>0){
            imageView.setVisibility(View.GONE);
            recyclerView.setAdapter(new UserFavVidPreAdapter(getContext(),videoList));
        }else{
            imageView.setVisibility(View.VISIBLE);
        }

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 0, false));
        return view;
    }

}
