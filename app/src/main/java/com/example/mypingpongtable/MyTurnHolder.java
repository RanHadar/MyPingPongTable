package com.example.cspingpong;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyTurnHolder extends RecyclerView.ViewHolder{

    ImageView mImageView;
    TextView mTextView1;
    TextView mTextView2;
    private ImageView mDeleteImage;
    ImageView mShareImage;


    MyTurnHolder(@NonNull final View itemView,final ItemClickListener mListener) {
        super(itemView);
        this.mImageView = itemView.findViewById(R.id.imgViewTurn1);
        this.mTextView1 = itemView.findViewById(R.id.txtViewTime1);
        this.mTextView2 = itemView.findViewById(R.id.txtAgainst1);
        this.mDeleteImage = itemView.findViewById(R.id.deleteImg);
        this.mShareImage = itemView.findViewById(R.id.shareImg);


        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mListener.onDeleteClick(itemView,position);
                    }
                }
            }
        });

    }

}
