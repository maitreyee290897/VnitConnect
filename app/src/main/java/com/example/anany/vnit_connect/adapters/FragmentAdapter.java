package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anany.vnit_connect.ChatbotFragment;
import com.example.anany.vnit_connect.ForumFragment;

/**
 * Created by shivali on 30/3/18.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatbotFragment();
            case 1:
                return new ForumFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //Your tab titles
            case 0:
                return "Chatbot";
            case 1:
                return "Forum";
            default:
                return null;
        }
    }

}
