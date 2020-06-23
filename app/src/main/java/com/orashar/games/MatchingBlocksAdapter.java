package com.orashar.games;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        void onItemClick(int position);
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
        public MatchingBlocksViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            itemView.setLayoutParams(lp);
            text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
