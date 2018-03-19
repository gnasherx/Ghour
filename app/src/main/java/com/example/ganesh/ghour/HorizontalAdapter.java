package com.example.ganesh.ghour;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    int[] images = {R.drawable.design_camera, R.drawable.design_blood,
            R.drawable.design_safe,R.drawable.design_you};
    int[] circleImages= {R.drawable.design_camera_circle,R.drawable.design_blood_circle,
    R.drawable.design_safe_circel,R.drawable.design_you_circle};
    int[] designIcons= {R.drawable.design_camera_icon,R.drawable.design_blood_icon,R.drawable.design_safe_icon
    ,R.drawable.design_profile_icon};
    String[] text = {"Camera","Blood","Safe", "You"};
    private Activity activity;


    public HorizontalAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.single_horizontal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorizontalAdapter.ViewHolder viewholder, final int position) {
        viewholder.imageView.setImageResource(images[position]);
        viewholder.textView.setText(text[position].toUpperCase());
        viewholder.imageviewCircle.setImageResource(circleImages[position]);
        viewholder.mIconImageBtn.setImageResource(designIcons[position]);

        viewholder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        v.getContext().startActivity(new Intent(v.getContext(), CameraActivity.class));
                        break;
                    case 1:
                        v.getContext().startActivity(new Intent(v.getContext(), BloodWebViewActivity.class));
                        break;
                    case 2:
                        v.getContext().startActivity(new Intent(v.getContext(), MarkYouSafeActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public int getItemCount() {
        return images.length;
    }

    //viewhlder to display horozpmtal card
    protected class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private RelativeLayout relativeLayout;
        private ImageView imageView;
        private ImageView imageviewCircle;
        private ImageButton mIconImageBtn;


        private TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageviewCircle=(ImageView)itemView.findViewById(R.id.imageViewCircle);
            mIconImageBtn=(ImageButton)itemView.findViewById(R.id.icon_image_btn) ;
            textView = (TextView) itemView.findViewById(R.id.textview);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.horizontal_relativeLayout);

        }
    }
}
