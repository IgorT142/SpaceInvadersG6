package es.urjc.jjve.spaceinvaders.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Integer> scores;

    public ViewPagerAdapter(FragmentManager fm, List<Integer> scores) {
        super(fm);
        this.scores = scores;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(scores.get(position));
    }

    @Override
    public int getCount() {
        return scores.size();
    }
}
