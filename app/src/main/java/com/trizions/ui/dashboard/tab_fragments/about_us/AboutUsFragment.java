package com.trizions.ui.dashboard.tab_fragments.about_us;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.model.common.SliderItem;
import com.trizions.utils.Const;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.welcomeMessage)
    TextView welcomeMessage;

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.imageSlider)
    SliderView sliderView;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnAboutUsListener mCallback;
    SharedPreferences pref;

    public AboutUsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnAboutUsListener) context;
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showWelcomeMessage();
        setUpAdvertisementRecyclerView(); // Load static image urls
    }

    void showWelcomeMessage(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String user_name = "";
        if (user_name != null && user_name.length() > 0)
            user_name = String.valueOf(user_name.charAt(0)).toUpperCase() + user_name.subSequence(1, user_name.length());
        else
            user_name = "";
        if(timeOfDay < 12){
            welcomeMessage.setText("Good Morning " + user_name);
        }else if(timeOfDay < 16){
            welcomeMessage.setText("Good Afternoon " + user_name);
        }else if(timeOfDay < 21){
            welcomeMessage.setText("Good Evening " + user_name);
        }else {
            welcomeMessage.setText("Good Night " + user_name);
        }
    }

    public void setUpAdvertisementRecyclerView() {
        try {
            SliderAdapter adapter = new SliderAdapter(getActivity());

            SliderItem sliderItem1 = new SliderItem();
            sliderItem1.setDescription("Products & Services");
            sliderItem1.setImageUrl("http://www.payorrelations.com/resources/banner_productsservices.jpg");
            adapter.addItem(sliderItem1);

            SliderItem sliderItem2 = new SliderItem();
            sliderItem2.setDescription("Projects");
            sliderItem2.setImageUrl("https://thumbs.dreamstime.com/z/internet-things-iot-banner-connectivity-device-concept-network-spider-web-network-connections-internet-things-iot-banner-173174029.jpg");
            adapter.addItem(sliderItem2);

            SliderItem sliderItem3 = new SliderItem();
            sliderItem3.setDescription("About us");
            sliderItem3.setImageUrl("https://i1.wp.com/madisonpac.com/wp-content/uploads/2020/02/Banner-Contact-Us-1.jpg");
            adapter.addItem(sliderItem3);

            sliderView.setSliderAdapter(adapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();

            sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
                @Override
                public void onIndicatorClicked(int position) {
                    Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
                    Toast.makeText(getActivity(), "onIndicatorClicked " + sliderView.getCurrentPagePosition(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public static class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

        private Context context;
        private List<SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapter(Context context) {
            this.context = context;
        }

        public void renewItems(List<SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_image_slider_layout, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SliderItem sliderItem = mSliderItems.get(position);

            viewHolder.textViewDescription.setText(sliderItem.getDescription());
            viewHolder.textViewDescription.setTextSize(16);
            viewHolder.textViewDescription.setTextColor(Color.WHITE);
            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageUrl())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);

            viewHolder.itemView.setOnClickListener(v -> {
                Log.d("Value ==> ", "This is item in position ==> " + position);
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            ImageView imageGifContainer;
            TextView textViewDescription;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
        }
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public interface OnAboutUsListener {
        void onShowAllCategories(String message);
        void onShowResults(String searchText);
    }
}
