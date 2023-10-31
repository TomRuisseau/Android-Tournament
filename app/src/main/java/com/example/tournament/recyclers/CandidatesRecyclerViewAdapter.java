package com.example.tournament.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tournament.MainActivity;
import com.example.tournament.R;
import com.example.tournament.dataClasses.Candidate;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CandidatesRecyclerViewAdapter extends RecyclerView.Adapter<CandidatesRecyclerViewAdapter.MyViewHolder>{
    Context context;
    List<Candidate> candidates;


    public CandidatesRecyclerViewAdapter(Context context, List<Candidate> candidates){
        this.context = context;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public CandidatesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where the view is inflated
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_candidate, parent, false);
        return new CandidatesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatesRecyclerViewAdapter.MyViewHolder holder, int position) {
        // This is where the data is set based on the position
        Candidate candidate = candidates.get(position);
        holder.textViewNameCandidate.setText(candidate.getName());
        String countText = "It has been elected has favorite " + (candidate.getCount() == 1 ? "once." : candidate.getCount() + " times.");
        holder.textViewCountCandidate.setText(countText);
        String url = candidate.getImageUrl();
        String type = candidate.getType();
        switch (type){
            case "movies":
            case "tv shows":
                Picasso.get()
                        .load(url)
                        .resize(2000, 3000)
                        .centerCrop()
                        .into(holder.imageViewCandidate);
                break;

            case "games":
                Picasso.get()
                        .load(url)
                        .resize(1920, 1080)
                        .centerCrop()
                        .into(holder.imageViewCandidate);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // This is where the views are declared

        ImageView imageViewCandidate;
        TextView textViewNameCandidate, textViewCountCandidate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewCandidate = itemView.findViewById(R.id.imageViewCandidate);
            textViewNameCandidate = itemView.findViewById(R.id.textViewNameCandidate);
            textViewCountCandidate = itemView.findViewById(R.id.textViewCountCandidate);
        }
    }
}
