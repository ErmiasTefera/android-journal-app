package com.example.ermia.journalapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.Utils;
import com.example.ermia.journalapp.data.Journal;

import java.util.List;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.JournalViewHolder> {

    private final LayoutInflater mInflater;
    public List<Journal> mJournals; // Cached copy of journals

    public JournalListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_journal, parent, false);

        return new JournalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        Journal current = mJournals.get(position);
        holder.tvJournalTitle.setText(current.getTitle());
        holder.tvJournalDate.setText(Utils.getFormattedDateString(current.getDate()));

    }

    public void setJournals(List<Journal> journals) {
        mJournals = journals;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mJournals has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mJournals != null)
            return mJournals.size();
        else return 0;
    }

    class JournalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvJournalTitle;
        private final TextView tvJournalDate;

        private JournalViewHolder(View itemView) {
            super(itemView);
            tvJournalTitle = itemView.findViewById(R.id.tv_item_journal_title);
            tvJournalDate = itemView.findViewById(R.id.tv_item_journal_date);
        }
    }
}