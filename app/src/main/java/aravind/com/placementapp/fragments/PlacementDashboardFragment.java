package aravind.com.placementapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import aravind.com.placementapp.R;
import aravind.com.placementapp.constants.Constants;
import aravind.com.placementapp.helper.FirebaseHelper;
import aravind.com.placementapp.pojo.Notification;

public class PlacementDashboardFragment extends Fragment implements ValueEventListener {

    private List<Notification> recentNotificationList;
    private ProgressBar loadingBar;
    private DatabaseReference databaseReference;
    private RecyclerView myrv;
    private RecyclerViewAdapterRecentNotification myAdapter;

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentNotificationList = new ArrayList<>();
        Log.i("Value", "Hello");
        databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS);
        databaseReference.addListenerForSingleValueEvent(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_placementdashboard, container, false);
        myrv = view.findViewById(R.id.viewrecentnotification_recycler_view);
        myrv.setVisibility(View.GONE);
        loadingBar = view.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        myAdapter = new RecyclerViewAdapterRecentNotification(recentNotificationList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        myrv.setLayoutManager(gridLayoutManager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.imageone));
        sliderItems.add(new SliderItem(R.drawable.imagetwo));
        sliderItems.add(new SliderItem(R.drawable.imagethree));
        sliderItems.add(new SliderItem(R.drawable.imagefour));
        sliderItems.add(new SliderItem(R.drawable.imagefive));


        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot != null) {
            Notification notification;
            for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                notification = childSnapShot.getValue(Notification.class);
                if (notification != null) {
                    recentNotificationList.add(new Notification(notification.getTitle(), notification.getMessage(), notification.getTimestamp()));
                }
            }
            if (loadingBar != null) {
                loadingBar.setVisibility(View.GONE);
            }
            if (myrv != null && myAdapter != null) {
                myrv.setVisibility(View.VISIBLE);
                myrv.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}