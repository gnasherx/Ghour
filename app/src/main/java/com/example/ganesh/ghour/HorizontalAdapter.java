package com.example.ganesh.ghour;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    int[] images = {R.drawable.signup_password_disabled, R.drawable.signup_name_disabled,
            R.drawable.signup_password_disabled};
    String[] text = {"Camera", "Safe", "You"};
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

        viewholder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        v.getContext().startActivity(new Intent(v.getContext(), CameraActivity.class));
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
        private TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textview);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

        }
    }
}
