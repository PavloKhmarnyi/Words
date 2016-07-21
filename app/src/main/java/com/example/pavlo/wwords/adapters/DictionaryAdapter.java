package com.example.pavlo.wwords.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavlo.wwords.R;
import com.example.pavlo.wwords.models.WordsPair;

import java.util.ArrayList;

/**
 * Created by pavlo on 21.07.16.
 */
public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<WordsPair> wordsPairs;

    public DictionaryAdapter(Context context, ArrayList<WordsPair> wordsPairs, OnItemClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.wordsPairs = wordsPairs;
        this.listener = listener;
    }

    public void add(ArrayList<WordsPair> data) {
        wordsPairs.addAll(data);
    }

    public void remove(int position) {
        wordsPairs.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dictionary_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return wordsPairs.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, wordsPairs.get(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView idTextView;
        private TextView englishWordTextView;
        private TextView ukrainianWordTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = (TextView) itemView.findViewById(R.id.idTextView);
            englishWordTextView = (TextView) itemView.findViewById(R.id.englishWordTextView);
            ukrainianWordTextView = (TextView) itemView.findViewById(R.id.ukrainianWordTextView);
        }

        public void bind(Context context, final WordsPair item, final OnItemClickListener listener) {

            int itemPosition = getAdapterPosition() + 1;
            idTextView.setText(itemPosition + ".");
            try {
                englishWordTextView.setText(item.getEnglishWord());
                ukrainianWordTextView.setText(item.getUkrainianWord());
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
