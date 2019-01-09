package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeDashboardSlideAdapter;
import com.cryptowallet.deviantx.UI.Adapters.FeaturedCoinsExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.NewsExcDBRAdapter;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeDashboardFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> {


    View view;
    ExchangeDashboardSlideAdapter dashboardSlideAdapter;

    @BindView(R.id.item_picker)
    DiscreteScrollView itemPicker;
    @BindView(R.id.img_user)
    ImageView img_user;
    @BindView(R.id.img_support)
    ImageView img_support;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.rview_featured_coins)
    RecyclerView rview_featured_coins;
    @BindView(R.id.rview_devx_news)
    RecyclerView rview_devx_news;
    @BindView(R.id.rview_gain_loose)
    RecyclerView rview_gain_loose;
    //    @BindView(R.id.magic_indicator)
//    MagicIndicator magic_indicator;
    @BindView(R.id.img_gainers)
    ImageView img_gainers;
    @BindView(R.id.txt_gainers)
    TextView txt_gainers;
    @BindView(R.id.rltv_gainers)
    RelativeLayout rltv_gainers;
    @BindView(R.id.rltv_gainers_view)
    RelativeLayout rltv_gainers_view;
    @BindView(R.id.img_losers)
    ImageView img_losers;
    @BindView(R.id.txt_losers)
    TextView txt_losers;
    @BindView(R.id.rltv_losers)
    RelativeLayout rltv_losers;
    @BindView(R.id.rltv_losers_view)
    RelativeLayout rltv_losers_view;

//    @BindView(R.id.)
//    ;

    LinearLayoutManager linearLayoutHorizantal, linearLayoutHorizantal1, linearLayoutVertical;
    FeaturedCoinsExcDBRAdapter featuredCoinsExcDBRAdapter;
    NewsExcDBRAdapter newsExcDBRAdapter;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;


    ArrayList<String> featuredCoinsList;
    ArrayList<String> newsList;
    ArrayList<String> gainersLoserList, gainersList, loosersList;

//    int[] CHANNELSImage = new int[]{R.drawable.selector_gainers, R.drawable.selector_losers};
//    int[] channelsName = new int[]{R.string.gainers, R.string.losers};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_dashboard_fragment, container, false);
        ButterKnife.bind(this, view);

        linearLayoutHorizantal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutHorizantal1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rview_featured_coins.setLayoutManager(linearLayoutHorizantal);
        rview_devx_news.setLayoutManager(linearLayoutHorizantal1);
        rview_gain_loose.setLayoutManager(linearLayoutVertical);

        featuredCoinsList = new ArrayList<>();
        newsList = new ArrayList<>();
        gainersLoserList = new ArrayList<>();
        gainersList = new ArrayList<>();
        loosersList = new ArrayList<>();


        dashboardSlideAdapter = new ExchangeDashboardSlideAdapter(getActivity());
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(dashboardSlideAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        itemPicker.scrollToPosition(1);

        featuredCoinsExcDBRAdapter = new FeaturedCoinsExcDBRAdapter(getActivity(), featuredCoinsList);
        rview_featured_coins.setAdapter(featuredCoinsExcDBRAdapter);

        newsExcDBRAdapter = new NewsExcDBRAdapter(getActivity(), newsList);
        rview_devx_news.setAdapter(newsExcDBRAdapter);

        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersList, " ", true, false);
        rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);

        rltv_gainers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_gainers.setImageDrawable(getResources().getDrawable(R.drawable.graph_up_selected));
                txt_gainers.setTextColor(getResources().getColor(R.color.yellow));
                rltv_gainers_view.setVisibility(View.VISIBLE);
                img_losers.setImageDrawable(getResources().getDrawable(R.drawable.graph_down_unselected));
                txt_losers.setTextColor(getResources().getColor(R.color.white));
                rltv_losers_view.setVisibility(View.GONE);

                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersList, " ", true, false);
                rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);
                gainerLoserExcDBRAdapter.notifyDataSetChanged();
            }
        });

        rltv_losers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_losers.setImageDrawable(getResources().getDrawable(R.drawable.graph_down_selected));
                txt_losers.setTextColor(getResources().getColor(R.color.yellow));
                rltv_losers_view.setVisibility(View.VISIBLE);
                img_gainers.setImageDrawable(getResources().getDrawable(R.drawable.graph_up_unselected));
                txt_gainers.setTextColor(getResources().getColor(R.color.white));
                rltv_gainers_view.setVisibility(View.GONE);

                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), loosersList, " ", false, false);
                rview_gain_loose.setAdapter(gainerLoserExcDBRAdapter);
                gainerLoserExcDBRAdapter.notifyDataSetChanged();
            }
        });


//        initMagicIndicator();
//        int selectedTab = (getIntent().getIntExtra(CONSTANTS.seletedTab, 0));
//        setAllSelection(selectedTab);
//        setAllSelection(0);
        return view;
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }


//    private void initMagicIndicator() {
//        final CommonNavigator commonNavigator = new CommonNavigator(getActivity());
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return CHANNELSImage == null ? 0 : CHANNELSImage.length;
//            }
//
//            /*  @Override
//              public IPagerTitleView getTitleView(Context context, final int index) {
//                  return new DummyPagerTitleView(context);
//              }*/
//            @Override
//            public IPagerTitleView getTitleView(final Context context, final int index) {
//                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
//
//                // load custom layout
//                View customLayout = LayoutInflater.from(context).inflate(R.layout.exc_gainer_loser_lyt, null);
//                final ImageView titleImg = (ImageView) customLayout.findViewById(R.id.title_img);
//                final TextView titleTxt = (TextView) customLayout.findViewById(R.id.title_text);
//                titleImg.setImageResource(CHANNELSImage[index]);
//                titleTxt.setText(channelsName[index]);
//                commonPagerTitleView.setContentView(customLayout);
//
//                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
//
//                    @Override
//                    public void onSelected(int index, int totalCount) {
//                        titleImg.setSelected(true);
//                        titleTxt.setSelected(true);
//                    }
//
//                    @Override
//                    public void onDeselected(int index, int totalCount) {
//                        titleImg.setSelected(false);
//                        titleTxt.setSelected(false);
//                    }
//
//                    @Override
//                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
////                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
////                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
//                    }
//
//                    @Override
//                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
////                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
////                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
//                    }
//                });
//
//                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setAllSelection(index);
//                    }
//                });
//
//                return commonPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
//                indicator.setReverse(false);
//                float smallNavigatorHeight = context.getResources().getDimension(R.dimen.small_navigator_height);
//                indicator.setLineHeight(UIUtil.dip2px(context, 5));
//                indicator.setTriangleHeight((int) smallNavigatorHeight);
//                indicator.setLineColor(Color.parseColor("#FBB03B"));
//                return indicator;
//            }
//        });
//        magic_indicator.setNavigator(commonNavigator);
//    }

//    private void setAllSelection(int index) {
//        magic_indicator.onPageSelected(index);
//        magic_indicator.onPageScrollStateChanged(index);
//        magic_indicator.onPageScrolled(index, 0, 0);
//    }

}
