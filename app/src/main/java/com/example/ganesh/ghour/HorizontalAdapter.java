package com.example.ganesh.ghour;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ganesh on 02-Oct-17.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Activity activity;
    int[] images = {R.drawable.signup_password_disabled, R.drawable.signup_name_disabled,
            R.drawable.signup_password_disabled};

    String[] text = {"Camera", "Safe", "You"};


    public HorizontalAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.single_horizontal_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder viewholder,final int position) {
        viewholder.imageView.setImageResource(images[position]);
        viewholder.textView.setText(text[position].toUpperCase());

        viewholder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"position:"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    //viewhlder to display horozpmtal card
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayout;
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textview);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}
