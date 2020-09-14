package aravind.com.placementapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
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
import aravind.com.placementapp.pojo.Recruiter;
import aravind.com.placementapp.pojo.TopPlacedStudents;

public class PlacementDashboardFragment extends Fragment implements ValueEventListener {

    private List<Notification> recentNotificationList;
    private List<Notification> recentNotificationListLimited;
    private List<TopPlacedStudents> topPlacedStudentsList;
    private List<Recruiter> recruiterList;
    private ProgressBar loadingBar;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private RecyclerView myrv;
    private RecyclerView myrv1;
    private RecyclerView myrv2;
    private RecyclerViewAdapterRecentNotification myAdapter;
    private RecyclerViewAdapterTopPlacedStudents myAdapter1;
    private RecyclerViewAdapterTopRecruiters myAdapter2;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentNotificationList = new ArrayList<>();
        topPlacedStudentsList = new ArrayList<>();
        recentNotificationListLimited = new ArrayList<>();
        recruiterList = new ArrayList<>();
        databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS);
        databaseReference1 = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_STUDENTS);
        databaseReference2 = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_RECRUITERS);
        databaseReference.addListenerForSingleValueEvent(this);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null) {
                    TopPlacedStudents topPlacedStudents;
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                        topPlacedStudents = childSnapShot.getValue(TopPlacedStudents.class);
                        if (topPlacedStudents != null) {
                            topPlacedStudentsList.add(new TopPlacedStudents(topPlacedStudents.getName(), topPlacedStudents.getCompany(), topPlacedStudents.getSalary(), topPlacedStudents.getBatch()));
                        }
                    }
                    if (loadingBar != null) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    if (myrv1 != null && myAdapter1 != null) {
                        myrv1.setVisibility(View.VISIBLE);
                        myrv1.setAdapter(myAdapter1);
                        myAdapter1.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null) {
                    Recruiter recruiter;
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                        if (childSnapShot != null) {
                            recruiter = childSnapShot.getValue(Recruiter.class);
                            if (recruiter != null) {
                                recruiterList.add(new Recruiter(recruiter.getName(), childSnapShot.getKey()));
                            }
                        }
                    }
                    if (loadingBar != null) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    if (myrv2 != null && myAdapter2 != null) {
                        myrv2.setVisibility(View.VISIBLE);
                        myrv2.setAdapter(myAdapter2);
                        myAdapter2.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_placementdashboard, container, false);
        myrv = view.findViewById(R.id.viewrecentnotification_recycler_view);
        myrv1 = view.findViewById(R.id.viewtopplacedstudents_recycler_view);
        myrv2 = view.findViewById(R.id.viewRecruiters_recycler_view);
        myrv.setVisibility(View.GONE);
        myrv1.setVisibility(View.GONE);
        myrv2.setVisibility(View.GONE);
        loadingBar = view.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.VISIBLE);
        myAdapter = new RecyclerViewAdapterRecentNotification(recentNotificationListLimited, this);
        myAdapter1 = new RecyclerViewAdapterTopPlacedStudents(topPlacedStudentsList, this);
        myAdapter2 = new RecyclerViewAdapterTopRecruiters(recruiterList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 1);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        myrv.setLayoutManager(gridLayoutManager);
        myrv1.setLayoutManager(gridLayoutManager1);
        myrv2.setLayoutManager(gridLayoutManager2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItemsAdder(sliderItems);

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

    private void sliderItemsAdder(List<SliderItem> sliderItems) {
        sliderItems.add(new SliderItem(R.drawable.imageone));
        sliderItems.add(new SliderItem(R.drawable.imagetwo));
        sliderItems.add(new SliderItem(R.drawable.imagethree));
        sliderItems.add(new SliderItem(R.drawable.imagefour));
        sliderItems.add(new SliderItem(R.drawable.imagefive));
        sliderItems.add(new SliderItem(R.drawable.imagesix));
        sliderItems.add(new SliderItem(R.drawable.imageseven));
        sliderItems.add(new SliderItem(R.drawable.imageeight));
        sliderItems.add(new SliderItem(R.drawable.imagenine));
        sliderItems.add(new SliderItem(R.drawable.imageten));
        sliderItems.add(new SliderItem(R.drawable.imageeleven));
        sliderItems.add(new SliderItem(R.drawable.imagetwelve));
        sliderItems.add(new SliderItem(R.drawable.imagethirteen));
        sliderItems.add(new SliderItem(R.drawable.imagefourteen));
        sliderItems.add(new SliderItem(R.drawable.imagefifteen));
        sliderItems.add(new SliderItem(R.drawable.imagesixteen));
        sliderItems.add(new SliderItem(R.drawable.imageseventeen));
        sliderItems.add(new SliderItem(R.drawable.imageeighteen));
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
                    Notification notification1 = new Notification(notification.getTitle(), notification.getMessage(), notification.getTimestamp());
                    notification1.setKey(Long.parseLong(childSnapShot.getKey()));
                    recentNotificationList.add(notification1);
                }
            }
            Collections.sort(recentNotificationList);
            recentNotificationListLimited.addAll(recentNotificationList.subList(0, 5));
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