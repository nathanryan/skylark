package com.nryan.skylark;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Nathan Ryan x13448212 on 19/02/2017.
 *
 * reference https://www.youtube.com/watch?v=A2_6mI7drVQ
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] titles = {"Bird One",
            "Bird Two",
            "Bird Three",
            "Bird Four",
            "Bird Five",
            "Bird Six",
            "Bird Seven",
            "Bird Eight"};

    private String[] details = {"Bird one details",
            "Bird two details", "Bird three details",
            "Bird four details", "Bird file details",
            "Bird six details", "Bird seven details",
            "Bird eight details"};

    private int[] images = { R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin,
            R.drawable.robin };

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);

        // Set the view to fade in
        //setFadeAnimation(viewHolder.itemView);

        setAnimation(viewHolder.itemView, i);
    }

    protected int mLastPosition = -1;

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > mLastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(1000);
            viewToAnimate.startAnimation(anim);
            mLastPosition = position;
        }
    }
/*
    private final static int FADE_DURATION = 1000; // in milliseconds

    //fade in on itemView load
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
*/
    @Override
    public int getItemCount() {
        return titles.length;
    }
}
