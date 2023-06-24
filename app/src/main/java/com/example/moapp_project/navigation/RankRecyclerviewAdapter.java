package com.example.moapp_project.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moapp_project.MemberInfo;
import com.example.moapp_project.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankRecyclerviewAdapter extends RecyclerView.Adapter<RankRecyclerviewAdapter.ViewHolder> {
    private List<MemberInfo> userList;

    public RankRecyclerviewAdapter(List<MemberInfo> userList) {
        this.userList = userList;

        // userList를 user.getScore() 값을 기준으로 내림차순 정렬
        Collections.sort(userList, new Comparator<MemberInfo>() {
            @Override
            public int compare(MemberInfo user1, MemberInfo user2) {
                return Integer.compare(user2.getScore(), user1.getScore());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberInfo user = userList.get(position);
        holder.userNameTextView.setText(user.getEmail());
        holder.ageTextView.setText(String.valueOf(user.getScore()));
        holder.rankTextView.setText(String.valueOf(position+1));

        Context context = holder.itemView.getContext(); // 컨텍스트 가져오기

        if (position == 0) {
            holder.rankImage.setImageDrawable(context.getResources().getDrawable(R.drawable.gold));
        } else if (position == 1) {
            holder.rankImage.setImageDrawable(context.getResources().getDrawable(R.drawable.silver));
        } else if (position == 2) {
            holder.rankImage.setImageDrawable(context.getResources().getDrawable(R.drawable.bronze));
        } else {
            holder.rankImage.setImageDrawable(context.getResources().getDrawable(R.drawable.begin));
        }
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView ageTextView;

        TextView rankTextView;

        ImageView rankImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.dayitemTextView);
            ageTextView = itemView.findViewById(R.id.timetextview);
            rankTextView=itemView.findViewById(R.id.rankTextView);
            rankImage=itemView.findViewById(R.id.rankimage);
        }
    }
}
