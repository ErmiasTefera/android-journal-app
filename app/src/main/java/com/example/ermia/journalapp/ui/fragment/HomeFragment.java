package com.example.ermia.journalapp.ui.fragment;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.Utils;
import com.example.ermia.journalapp.data.Journal;
import com.example.ermia.journalapp.ui.ClickListener;
import com.example.ermia.journalapp.ui.JournalViewModel;
import com.example.ermia.journalapp.ui.RecyclerTouchListener;
import com.example.ermia.journalapp.ui.activity.JournalDetailActivity;
import com.example.ermia.journalapp.ui.activity.NewJournalActivity;
import com.example.ermia.journalapp.ui.adapter.JournalListAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final int NEW_JOURNAL_ACTIVITY_REQUEST_CODE = 1;
    public static final int JOURNAL_DETAIL_ACTIVITY_REQUEST_CODE = 2;
    public static final int JOURNAL_EDIT_ACTIVITY_REQUEST_CODE = 3;

    public static final String INTENT_EXTRA_JOURNAL_ID = "JOURNAL ID";
    public static final String INTENT_EXTRA_JOURNAL_TITLE = "JOURNAL TITLE";
    public static final String INTENT_EXTRA_JOURNAL_CONTENT = "JOURNAL CONTENT";


    View mRootView;

    private JournalListAdapter mAdapter;
    private JournalViewModel mJournalViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mJournalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);

        mJournalViewModel.getAllJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable final List<Journal> journals) {
                // Update the cached copy of the journals in the adapter.
                mAdapter.setJournals(journals);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);


        FloatingActionButton mFab = mRootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewActivity();
            }
        });

        RecyclerView mRecyclerView = mRootView.findViewById(R.id.recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new JournalListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Journal journal = mAdapter.mJournals.get(position);
                showDetailActivity(journal);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return mRootView;
    }

    //shows add new activity
    private void showAddNewActivity() {
        Intent intent = new Intent(getActivity(), NewJournalActivity.class);
        startActivityForResult(intent, NEW_JOURNAL_ACTIVITY_REQUEST_CODE);
    }

    //shows journal detail activity
    private void showDetailActivity(Journal journal) {
        Intent intent = new Intent(getActivity(), JournalDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_JOURNAL_ID, journal.getId());
        intent.putExtra(INTENT_EXTRA_JOURNAL_TITLE, journal.getTitle());
        intent.putExtra(INTENT_EXTRA_JOURNAL_CONTENT, journal.getContent());

        startActivityForResult(intent, JOURNAL_DETAIL_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_JOURNAL_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            addNewJournal(data);
        } else if (requestCode == JOURNAL_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            updateJournal(data);
        }
    }

    private void addNewJournal(Intent data) {
        String journalTitle = data.getStringExtra(NewJournalActivity.EXTRA_REPLY_TITLE);
        String journalContent = data.getStringExtra(NewJournalActivity.EXTRA_REPLY_CONTENT);

        Journal journal = new Journal();

        journal.setTitle(journalTitle);
        journal.setContent(journalContent);
        journal.setDate(Utils.getCurrentDateAsString());

        mJournalViewModel.insert(journal);

        Utils.showMessage(mRootView, "Journal Added");
    }

    private void updateJournal(Intent data) {
        int journalId = data.getIntExtra(JournalDetailActivity.EXTRA_REPLY_ID, 0);
        String journalTitle = data.getStringExtra(JournalDetailActivity.EXTRA_REPLY_TITLE);
        String journalContent = data.getStringExtra(JournalDetailActivity.EXTRA_REPLY_CONTENT);

        Journal journal = new Journal();

        journal.setId(journalId);
        journal.setTitle(journalTitle);
        journal.setContent(journalContent);
        journal.setDate(Utils.getCurrentDateAsString());

        mJournalViewModel.update(journal);

        Utils.showMessage(mRootView, "Journal Updated");
    }
}
