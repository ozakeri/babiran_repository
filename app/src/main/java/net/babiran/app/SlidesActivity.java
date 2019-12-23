package net.babiran.app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import Fragments.FirstFragment;
import Fragments.ForthFragment;
import Fragments.SecondFragment;
import Fragments.ThirdFragment;
import me.relex.circleindicator.CircleIndicator;

public class SlidesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);

        ViewPager pager = (ViewPager) findViewById(R.id.home_viewpager);
        CircleIndicator customIndicator = (CircleIndicator) findViewById(R.id.product_indicator_custom);

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        customIndicator.setViewPager(pager);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return FirstFragment.newInstance("FirstFragment, Instance 1");
                case 1:
                    return SecondFragment.newInstance("SecondFragment, Instance 1");
                case 2:
                    return ThirdFragment.newInstance("Third, Instance 1");
                case 3:
                    return ForthFragment.newInstance("Forth, Instance 1");


            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
