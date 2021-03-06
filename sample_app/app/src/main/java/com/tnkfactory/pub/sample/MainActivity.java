package com.tnkfactory.pub.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tnkfactory.ad.AdError;
import com.tnkfactory.ad.AdItem;
import com.tnkfactory.ad.AdListener;
import com.tnkfactory.ad.InterstitialAdItem;
import com.tnkfactory.pub.sample.Interstitial_ad.InterstitialActivity;
import com.tnkfactory.pub.sample.Interstitial_ad.RewardVideoActivity;
import com.tnkfactory.pub.sample.banner_ad.BannerActivity;
import com.tnkfactory.pub.sample.feed_ad.FeedActivity;
import com.tnkfactory.pub.sample.feed_ad.FeedRecyclerViewActivity;
import com.tnkfactory.pub.sample.native_ad.NativeActivity;
import com.tnkfactory.pub.sample.native_ad.NativeViewPagerActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private InterstitialAdItem finishAdItem = null;
    private boolean isClickBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initLayout();
        prepareFinishInterstitialAd();
    }

    // Sample layout
    private void initLayout() {
        ArrayList<MainListItem> itemList = new ArrayList();
        // Basic
        itemList.add(MainListItem.HEADER_01);
        itemList.add(MainListItem.BANNER);
        itemList.add(MainListItem.INTERSTITIAL);
        itemList.add(MainListItem.NATIVE);
        itemList.add(MainListItem.FEED);
        itemList.add(MainListItem.VIDEO_REWARD);

        // Custom
        itemList.add(MainListItem.HEADER_02);
        itemList.add(MainListItem.FEED_RECYCLERVIEW);
        itemList.add(MainListItem.NATIVE_VIEW_PAGER);

        ListView listView = findViewById(R.id.list_main);
        MainListAdapter adapter = new MainListAdapter(this, itemList);
        listView.setAdapter(adapter);

        // List Click Event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = null;

                MainListItem item = (MainListItem) view.getTag();
                switch (item) {

                    case BANNER:
                        intent = new Intent(getApplicationContext(), BannerActivity.class);
                        break;
                    case INTERSTITIAL:
                        intent = new Intent(getApplicationContext(), InterstitialActivity.class);
                        break;
                    case NATIVE:
                        intent = new Intent(getApplicationContext(), NativeActivity.class);
                        break;
                    case FEED:
                        intent = new Intent(getApplicationContext(), FeedActivity.class);
                        break;
                    case VIDEO_REWARD:
                        intent = new Intent(getApplicationContext(), RewardVideoActivity.class);
                        break;


                    case FEED_RECYCLERVIEW:
                        intent = new Intent(getApplicationContext(), FeedRecyclerViewActivity.class);
                        break;
                    case NATIVE_VIEW_PAGER:
                        intent = new Intent(getApplicationContext(), NativeViewPagerActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isClickBack) {
            return;
        }

        isClickBack = true;

        if (finishAdItem != null && finishAdItem.isLoaded()) {
            finishAdItem.show();
        } else {
            isClickBack = false;
        }
    }

    // 종료 시 전면 광고 로드
    private void prepareFinishInterstitialAd() {
        InterstitialAdItem adItem = new InterstitialAdItem(this, "TEST_INTERSTITIAL_V_FINISH", new AdListener() {
            @Override
            public void onError(AdItem adItem, AdError error) {
            }

            @Override
            public void onClose(AdItem adItem, int type) {

                if (type == AdListener.CLOSE_EXIT) {
                    MainActivity.this.finish();
                } else {
                    // 아니요 클릭시 광고 재로드
                    prepareFinishInterstitialAd();
                }
            }

            @Override
            public void onClick(AdItem adItem) {
            }

            @Override
            public void onShow(AdItem adItem) {
                isClickBack = false;
            }

            @Override
            public void onLoad(AdItem adItem) {
                finishAdItem = (InterstitialAdItem) adItem;
            }
        });

        adItem.load();
    }

    // Sample list adapter
    public class MainListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<MainListItem> list;

        public MainListAdapter(Context context, ArrayList<MainListItem> list) {
            this.context = context;
            this.list = list;
        }


        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            MainListItem data = list.get(position);
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(data.isHeader() == true ? R.layout.main_list_header : R.layout.main_list_item, null);
            }

            TextView txtItem = view.findViewById(R.id.text);
            txtItem.setText(data.getValue());
            view.setTag(data);

            return view;
        }
    }
}
