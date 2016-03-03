package com.example.parulsingh.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ScreenSlidePagerActivity extends FragmentActivity implements ScreenSlidePageFragmentVote.OnHeadlineSelectedListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    ScreenSlidePageFragment frag1;
    ScreenSlidePageFragment2 frag2;
    ScreenSlidePageFragment3 frag3;
    ScreenSlidePageFragmentVote fragVote;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    boolean isShake = false;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
       // mPager.setCurrentItem(1);
        if (getIntent().hasExtra("ZIPCODE") && getIntent().getStringExtra("ZIPCODE").equals("95070")){
            if (fragVote != null ){
                fragVote.updateContent();
            }
            if (frag1 != null ){
                frag1.updateContent2();
            }
            if (frag2 != null ){
                frag2.updateContent2();
            }
            if (frag3 != null ){
                frag3.updateContent2();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    @Override
    public void onArticleSelected(int position) {
        Intent sendIntent = new Intent(this, WatchToPhoneService.class);
        sendIntent.putExtra("NAME", "shake");
        startService(sendIntent);
        if (frag1 != null ){
            frag1.updateContent();
        }
        if (frag2 != null ){
            frag2.updateContent();
        }
        if (frag3 != null ){
            frag3.updateContent();
        }

    }
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new ScreenSlidePageFragmentHome();
            if (position == 1)
                return new ScreenSlidePageFragmentVote();
            if (position == 2)
                return new ScreenSlidePageFragment();
            if (position == 3)
                return new ScreenSlidePageFragment2();
            if (position == 4)
                return new ScreenSlidePageFragment3();
            return new ScreenSlidePageFragment();

        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 1:
                    fragVote = (ScreenSlidePageFragmentVote) createdFragment;
                    break;
                case 2:
                    frag1 = (ScreenSlidePageFragment) createdFragment;
                    break;
                case 3:
                    frag2 = (ScreenSlidePageFragment2) createdFragment;
                    break;
                case 4:
                    frag3 = (ScreenSlidePageFragment3) createdFragment;
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}