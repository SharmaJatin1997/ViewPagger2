package com.saahayak.saahayak.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.saahayak.saahayak.R;
import com.saahayak.saahayak.response.BannerVedioPojo;
import com.saahayak.saahayak.response.PopularServicePojo;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter2 extends RecyclerView.Adapter<HomeViewPagerAdapter2.ViewHolder> {
    Context context;
    List<BannerVedioPojo.$detail> bannervedioList;
    Select select;

    public interface Select{
        void onClick(int position,String path);
    }

    public HomeViewPagerAdapter2(Context context, List<BannerVedioPojo.$detail> bannervedioList, Select select) {
        this.context = context;
        this.bannervedioList = bannervedioList;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.viewpager_img,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(bannervedioList.get(position).getPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.onClick(position,bannervedioList.get(position).getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannervedioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
}
