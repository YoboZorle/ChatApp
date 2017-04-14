package beaststudio.in.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {

    int tabnum;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.tabnum = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                signIn tab1 = new signIn();
                return tab1;
            case 1:
                signUp tab2 = new signUp();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabnum;
    }
}
