package es.urjc.jjve.spaceinvaders.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.urjc.jjve.spaceinvaders.HighScoreActivity;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    List<PageFragment> scores;

    public CustomPagerAdapter(Context context,List<PageFragment> scores) {
        mContext = context;
        this.scores=scores;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        PageFragment currentPage = scores.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        collection.addView(currentPage);
        return currentPage;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "View "+ (position+1);
    }

    @Override
    public int getItemPosition(Object object){
        return scores.indexOf(object);
    }

}
