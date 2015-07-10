package com.myapps.whodeletedme;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivityTabbed extends AppCompatActivity implements UserListFragment.OnFragmentInteractionListener{


    private static final String TAG = "MainActivityTabbed";
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    ProgressDialog progressDialog;
    BroadcastReceiver receiver;
    User mUser;
    int mCurrentColor, mGreen, mBlue, mRed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) ParseUser.getCurrentUser();
        if(mUser == null) {
            startActivity(new Intent(this,WelcomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main_activity_tabbed);

        ButterKnife.inject(this);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        setSupportActionBar(mToolbar);
        mGreen = getResources().getColor(R.color.green_color_500);
        mBlue = getResources().getColor(R.color.main_color_500);
        mRed = getResources().getColor(R.color.accent_color_500);
        mCurrentColor = mBlue;
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        colorAnimate(mCurrentColor, mBlue);
                        mCurrentColor = mBlue;
                        break;
                    case 1:
                        colorAnimate(mCurrentColor, mGreen);
                        mCurrentColor = mGreen;
                        break;
                    case 2:
                        colorAnimate(mCurrentColor, mRed);
                        mCurrentColor = mRed;
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void colorAnimate(int colorFrom, int colorTo){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolbar.setBackgroundColor((Integer)animator.getAnimatedValue());
                mTabLayout.setBackgroundColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        Log.d(TAG,"onFragmentInteraction "+id);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0) {

                return UserListFragment.newInstance("0", 0);
            }
            if(position == 1) {

                return UserListFragment.newInstance("1", 1);
            }
            if(position == 2) {

                return UserListFragment.newInstance("2", 2);
            }

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);

            }
            return null;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
            ((TextView) rootView.findViewById(R.id.fragment_textView))
                .setText(String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
