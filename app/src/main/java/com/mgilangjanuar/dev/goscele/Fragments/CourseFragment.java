package com.mgilangjanuar.dev.goscele.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.goscele.Adapters.AllCoursesViewAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.CurrentCoursesViewAdapter;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.Fragments.Courses.AllCourseFragment;
import com.mgilangjanuar.dev.goscele.Fragments.Courses.CurrentCourseFragment;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.SearchCourseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseFragment extends Fragment {

    @BindView(R.id.toolbar_course)
    Toolbar toolbar;
    @BindView(R.id.view_pager_fragment_course)
    ViewPager viewPager;
    @BindView(R.id.tab_fragment_course)
    TabLayout tabLayout;
    private CoursePresenter presenter;

    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_course));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        buildTabLayout(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.course_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_course_add) {
            ((BaseActivity) getActivity()).redirect(new Intent(getActivity(), SearchCourseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildTabLayout(final View view) {
        BaseTabViewPagerAdapter fragmentPagerAdapter = new BaseTabViewPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.addFragment(CurrentCourseFragment.newInstance(), getResources().getString(R.string.title_fragment_current_course));
        fragmentPagerAdapter.addFragment(AllCourseFragment.newInstance(), getResources().getString(R.string.title_fragment_all_course));
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateRecyclerViewAdapter(view, tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void updateRecyclerViewAdapter(View view, String param) {
        presenter = new CoursePresenter(getActivity(), view);

        if (param.equalsIgnoreCase(getResources().getString(R.string.title_fragment_all_course)) && CoursePresenter.isDataAllCoursesViewAdapterChanged) {
            CoursePresenter.isDataAllCoursesViewAdapterChanged = false;
            (new Thread(() -> {
                final AllCoursesViewAdapter adapter = presenter.getAllCoursesViewAdapter();
                if (getActivity() == null) return;
                RecyclerView rvAllCourses = (RecyclerView) view.findViewById(R.id.recycler_view_all_course);
                getActivity().runOnUiThread(() -> rvAllCourses.setAdapter(adapter));
            })).start();
        } else if (param.equalsIgnoreCase(getResources().getString(R.string.title_fragment_current_course)) && CoursePresenter.isDataCurrentCoursesViewAdapterChanged) {
            CoursePresenter.isDataCurrentCoursesViewAdapterChanged = false;
            (new Thread(() -> {
                final CurrentCoursesViewAdapter adapter = presenter.getCurrentCoursesViewAdapter();
                if (getActivity() == null) return;
                RecyclerView rvCurrentCourses = (RecyclerView) view.findViewById(R.id.recycler_view_current_course);
                getActivity().runOnUiThread(() -> rvCurrentCourses.setAdapter(adapter));
            })).start();
        }
    }
}
