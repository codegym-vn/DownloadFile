package com.codegym.downloadfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 1/4/2018.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mList;
    private OnItemClickListener onItemClickListener;

    public SongAdapter(Context mContext, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    public void setItems(List<Song> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Song song = mList.get(position);
        holder.binData(song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(song);
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(Song item);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDownload;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDownload = itemView.findViewById(R.id.tvDownload);
        }
        private void binData(Song song) {
            tvDownload.setText(song.getName());
        }
    }
}
