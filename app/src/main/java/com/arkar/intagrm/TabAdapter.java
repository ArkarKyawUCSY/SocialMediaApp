package com.arkar.intagrm;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    @Override
    //return the tag fragment
    public Fragment getItem(int tabPosition) {
        switch (tabPosition) {
            case 0:
                ProfileMain profileMain = new ProfileMain();
                return profileMain;
            case 1:
                UserMain userMain = new UserMain();
                return userMain;
            case 2:
                PictureMain pictureMain = new PictureMain();
                return  pictureMain;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "User";
            case 2:
                return "Share Picture";
            default:
                break;
        }

        return super.getPageTitle(position);
    }
}
