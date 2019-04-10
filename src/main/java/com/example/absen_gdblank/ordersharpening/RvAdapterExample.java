package com.example.absen_gdblank.ordersharpening;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Absen_GdBlank on 10/04/2019.
 */

public class RvAdapterExample extends RecyclerView.Adapter<RvAdapterExample.ViewHolder>{
    public RvAdapterExample() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView){
            super(itemView);

        }
    }

}
