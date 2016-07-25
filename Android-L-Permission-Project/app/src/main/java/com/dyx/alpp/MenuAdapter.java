package com.dyx.alpp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * project name：Android-L-Permission-Project
 * class describe：
 * create person：dayongxin
 * create time：16/7/25 下午10:34
 * alter person：dayongxin
 * alter time：16/7/25 下午10:34
 * alter remark：
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<String> list;
    public OnRvClickListener mOnRvClickListener;

    public interface OnRvClickListener {
        void onItemClick(int pos);
    }

    public void setmOnRvClickListener(OnRvClickListener mOnRvClickListener) {
        this.mOnRvClickListener = mOnRvClickListener;
    }

    public MenuAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position));
        if (mOnRvClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnRvClickListener.onItemClick(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
