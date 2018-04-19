package com.example.anany.vnit_connect.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anany.vnit_connect.AdminForumFragment;
import com.example.anany.vnit_connect.ChatbotFragment;
import com.example.anany.vnit_connect.AdminAnswerFragment;
import com.example.anany.vnit_connect.ForumFragment;

/**
 * Created by csevnitnagpur on 18-04-2018.
 */

public class FragmentAadapter extends FragmentPagerAdapter {

    private Context mContext;

    public FragmentAadapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatbotFragment();
            case 1:
                return new AdminForumFragment();
            case 2:
                return new AdminAnswerFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //Your tab titles
            case 0:
                return "Chatbot";
            case 1:
                return "Questions";
            case 2:
                return "Answers";

            default:
                return null;
        }
    }

}
