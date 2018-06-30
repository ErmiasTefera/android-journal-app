package com.example.ermia.journalapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.data.Journal;
import com.example.ermia.journalapp.ui.fragment.HomeFragment;

public class JournalDetailActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_ID = "ID";
    public static final String EXTRA_REPLY_TITLE = "TITLE";
    public static final String EXTRA_REPLY_CONTENT = "CONTENT";



    Toolbar mToolBar;
    TextView mJournalTitleView;
    TextView mJournalContentView;

    Intent mReplyIntent;

    Journal mJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail);

        mToolBar = findViewById(R.id.toolbar_journal_detail);
        mToolBar.setTitle("");

        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, mReplyIntent);
                finish();
            }
        });


        mJournalTitleView = findViewById(R.id.tv_journal_detail_title);
        mJournalContentView = findViewById(R.id.tv_journal_detail_content);

        mReplyIntent = new Intent();

        Intent intent = getIntent();
        int journalId = intent.getIntExtra(HomeFragment.INTENT_EXTRA_JOURNAL_ID, 0);
        String title = intent.getStringExtra(HomeFragment.INTENT_EXTRA_JOURNAL_TITLE);
        String content = intent.getStringExtra(HomeFragment.INTENT_EXTRA_JOURNAL_CONTENT);

        mJournal = new Journal();
        mJournal.setId(journalId);
        mJournal.setTitle(title);
        mJournal.setContent(content);

        mJournalTitleView.setText(title);
        mJournalContentView.setText(content);

    }

    //shows journal detail activity
    private void showEditActivity() {
        Intent intent = new Intent(this, EditJournalActivity.class);
        intent.putExtra(HomeFragment.INTENT_EXTRA_JOURNAL_ID, mJournal.getId());
        intent.putExtra(HomeFragment.INTENT_EXTRA_JOURNAL_TITLE, mJournal.getTitle());
        intent.putExtra(HomeFragment.INTENT_EXTRA_JOURNAL_CONTENT, mJournal.getContent());

        startActivityForResult(intent, HomeFragment.JOURNAL_EDIT_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_journal_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_journal) {
            showEditActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HomeFragment.JOURNAL_EDIT_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            updateJournal(data);
        }
    }


    //
    private void updateJournal(Intent data) {
        String title = data.getStringExtra(EXTRA_REPLY_TITLE);
        String content = data.getStringExtra(EXTRA_REPLY_CONTENT);

        mReplyIntent.putExtra(EXTRA_REPLY_ID, mJournal.getId());
        mReplyIntent.putExtra(EXTRA_REPLY_TITLE, title);
        mReplyIntent.putExtra(EXTRA_REPLY_CONTENT, content);

        setResult(RESULT_OK, mReplyIntent);
        finish();
    }



}
