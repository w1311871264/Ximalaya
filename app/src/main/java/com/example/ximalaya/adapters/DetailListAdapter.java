package com.example.ximalaya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mdetailData = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_detail, viewGroup, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder innerHolder, final int position) {
        //找到控件 设置数据
        View itemView = innerHolder.itemView;
        //顺序ID
        TextView orderTv = itemView.findViewById(R.id.item_id_text);
        //标题
        TextView titleTv = itemView.findViewById(R.id.detail_item_title);
        //播放量
        TextView playCountTv = itemView.findViewById(R.id.detail_item_count);
        //时长
        TextView durationTv = itemView.findViewById(R.id.detail_item_duration);
        //更新时间
        TextView updateDataTv = itemView.findViewById(R.id.detail_update_time_text);


        //设置数据
        int i = position+1;
        Track track = mdetailData.get(position);
        orderTv.setText(i+"");
        titleTv.setText(track.getTrackTitle());
        playCountTv.setText(track.getPlayCount()+"");
        durationTv.setText(track.getDuration()+"");
        String updateTimeText = mSimpleDateFormat.format(track.getUpdatedAt());
        updateDataTv.setText(updateTimeText);

        //设置item点击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {

                    mItemClickListener.onItemClick(mdetailData, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdetailData.size();
    }

    public void setData(List<Track> tracks) {
        mdetailData.clear();
        mdetailData.addAll(tracks);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface ItemClickListener{
        void onItemClick(List<Track> mdetailData, int position);
    }
}
