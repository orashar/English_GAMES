package com.orashar.games;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MatchingBlocksAdapter extends RecyclerView.Adapter<MatchingBlocksAdapter.MatchingBlocksViewHolder> {

    List<MatchingBlocksItemObject> list = new ArrayList<>();
    OnItemClickListener listener;
    int itemHeight;

    public MatchingBlocksAdapter(List<MatchingBlocksItemObject> list, int itemHeight) {
        this.list = list;
        this.itemHeight = itemHeight;
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    @NonNull
    @Override
    public MatchingBlocksAdapter.MatchingBlocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_blocks_item_layout, parent, false);
        return new MatchingBlocksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingBlocksViewHolder holder, int position) {
        if(list.get(position).isMatched()){
            holder.itemView.setVisibility(View.GONE);
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.text.setText(list.get(position).getText());
            if (list.get(position).isSelected()) {
                holder.text.setBackgroundColor(Color.BLUE);
            } else {
                holder.text.setBackgroundColor(Color.GREEN);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MatchingBlocksViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public MatchingBlocksViewHolder(@NonNull final View itemView) {
            super(itemView);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            lp.setMargins(8, 8, 8, 8);
            itemView.setLayoutParams(lp);
            text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shakeView(itemView, 0, 0);
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
    private void shakeView(View view, int duration, int offset){
        Animation animation = new RotateAnimation(0, 30, 50f, 50f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }
}
