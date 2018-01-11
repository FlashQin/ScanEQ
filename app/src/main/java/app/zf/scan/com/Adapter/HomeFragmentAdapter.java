package app.zf.scan.com.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import app.zf.scan.com.Fragment.HomeOneFragment;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/12.
 */

public class HomeFragmentAdapter extends FragmentStatePagerAdapter{
    public static final String TAB_TAG = "@dream@";
    private List<Fragment> mFragments;
    private List<String> mTitles;
    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
               super(fm);
        mFragments=fragments;
              mTitles = titles;
         }
    @Override
    public Fragment getItem(int position) {

      //fragment.setTitle(title[0]);
           return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
