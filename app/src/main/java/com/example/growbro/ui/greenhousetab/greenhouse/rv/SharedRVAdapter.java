package com.example.growbro.ui.greenhousetab.greenhouse.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class SharedRVAdapter extends RecyclerView.Adapter<SharedRVAdapter.ViewHolder> {

    private ArrayList<String> friends;
    final private SharedRVAdapter.OnListItemClickListener onListItemClickListener;

    public SharedRVAdapter(SharedRVAdapter.OnListItemClickListener onListItemClickListener) {
        this.friends = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
    }

    public SharedRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_shared, parent, false);
        return new SharedRVAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(SharedRVAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(friends.get(position));
    }

    public int getItemCount() {
        return friends.size();
    }

    public void setItemList(ArrayList<String> sharedUsers) {
        if (sharedUsers != null)
            friends = sharedUsers;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Chip name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sharedChip);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}

