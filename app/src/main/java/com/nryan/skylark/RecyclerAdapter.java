package com.nryan.skylark;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nathan Ryan x13448212 on 19/02/2017.
 * <p>
 * reference https://www.youtube.com/watch?v=A2_6mI7drVQ
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public Button addBird;
    protected int mLastPosition = -1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String[] titles = {"Robin",
            "Black Bird",
            "Blue Tit",
            "Goldfinch",
            "Sparrow",
            "Sparrowhawk",
            "Crow",
            "Wood Pigeon",
            "Wren",
            "Hen Harrier"};
 /*   private String[] details = {"The European robin (Erithacus rubecula), known simply as the robin or robin redbreast in the British Isles, is a small insectivorous passerine bird, specifically a chat, that was formerly classified as a member of the thrush family (Turdidae) but is now considered to be an Old World flycatcher",
            "The commonest and most widespread member of the thrush family In Ireland. The males's all black plumage and bright yellow bill is unmistakable, however females are much browner, with speckles on the upper breast and at first glance resemble a Song Thrush.",
            "A colourful, noisy, active little bird, commonly seen in gardens, especially at nut feeders and will use nestboxes. Bright blue crown, nape colllar, wings and tail and yellow underside.",
            "Smaller than a Chaffinch, this brightly-coloured finch has become a familiar sight at garden nut feeders in recent years.",
            "Sturdy relative of the finches, with large head and bill. Dark brown upperparts with heavy dark streaking, grey underparts.",
            " A small bird of prey (raptor) with broad wings with blunt wing tips and a long tail. Small hooked bill suitable for eating meat. Tail is banded in all plumages with four or five bands.",
            "Slightly larger than a Rook. Ages and sexes are similar in appearance. The head, throat and breast are black, as are the wings and tail.",
            " The largest of the pigeons in Ireland with a proportionally long tail and small head. A full breast. Easily identified in flight by large white wing bands traversing the upper wing."}; */
    private int[] images = {R.drawable.robin,
            R.drawable.blackbird,
            R.drawable.bluetit,
            R.drawable.goldfinch,
            R.drawable.sparrow,
            R.drawable.sparrowhawk,
            R.drawable.crow,
            R.drawable.pigeon,
            R.drawable.wren,
            R.drawable.henharrier};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.itemTitle.setText(titles[i]);
        //viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);

        addBird = (Button) viewHolder.itemView.findViewById(R.id.addBtn);

        addBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference birdRef = databaseReference.child("Birds");
                birdRef.child(user.getUid()).push().setValue(titles[i]);

                Snackbar.make(v, titles[i] + " has been marked as seen",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setAnimation(viewHolder.itemView, i);
    }

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > mLastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(1000);
            viewToAnimate.startAnimation(anim);
            mLastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
