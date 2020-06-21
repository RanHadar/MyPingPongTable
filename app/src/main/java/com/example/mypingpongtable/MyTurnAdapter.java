package com.example.mypingpongtable;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;



public class MyTurnAdapter extends RecyclerView.Adapter<MyTurnHolder>{

    private Context c;
    private ArrayList<MyTurnSlot> myTurns;
    private ItemClickListener mListener;


    MyTurnAdapter(Context c, ArrayList<MyTurnSlot> myTurns) {
        this.c = c;
        this.myTurns = myTurns;
    }

    @NonNull
    @Override
    public MyTurnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_turn,parent, false);
        return new MyTurnHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTurnHolder holder, final int position) {

        holder.mTextView1.setText(myTurns.get(position).getTurnTime());
        holder.mTextView2.setText(myTurns.get(position).getTurnAgainst());
        holder.mImageView.setVisibility(View.INVISIBLE);
        holder.mShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                if(myTurns.get(position).getTurnAgainst().equals("Waiting for an opponent")){
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Play PingPong against me in: "+myTurns.get(position).getTurnTime());
                }
                else{
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Watch me play PingPong against "+ myTurns.get(position).getTurnAgainst().substring(17) + " in: " + myTurns.get(position).getTurnTime());
                }
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                c.startActivity(shareIntent);
            }
        });

        if(!myTurns.get(position).getTurnAgainst().equals("Waiting for an opponent")) {
            holder.mTextView1.setTextColor(Color.BLACK);
            holder.mTextView2.setTextColor(Color.BLACK);
            holder.mImageView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return myTurns.size();
    }

    private int getPosition(){
        return this.getPosition();
    }

    void setItemClickListener(ItemClickListener listener){
        this.mListener = listener;
    }

    void removeGame(int position){
        myTurns.remove(position);
        notifyItemRemoved(position);
    }

}
