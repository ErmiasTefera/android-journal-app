package com.example.ermia.journalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.MalformedJsonException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.Utils;
import com.example.ermia.journalapp.data.Journal;
import com.example.ermia.journalapp.ui.fragment.HomeFragment;

public class EditJournalActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_ID = "ID";
    public static final String EXTRA_REPLY_UUID = "UUID";
    public static final String EXTRA_REPLY_TITLE = "TITLE";
    public static final String EXTRA_REPLY_CONTENT = "CONTENT";


    Toolbar mToolBar;
    EditText mJournalTitleView;
    EditText mJournalContentView;

    Intent mReplyIntent;

    Journal mJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        mToolBar = findViewById(R.id.toolbar_journal_edit);

        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, mReplyIntent);
                finish();
            }
        });

        mReplyIntent = new Intent();

        mJournalTitleView = findViewById(R.id.et_journal_detail_title);
        mJournalContentView = findViewById(R.id.et_journal_detail_content);


        Intent intent = getIntent();
        long journalId = intent.getLongExtra(HomeFragment.INTENT_EXTRA_JOURNAL_ID,0);
        String journalUuid = intent.getStringExtra(HomeFragment.INTENT_EXTRA_JOURNAL_UUID);
        String title = intent.getStringExtra(HomeFragment.INTENT_EXTRA_JOURNAL_TITLE);
        String content = intent.getStringExtra(HomeFragment.INTENT_EXTRA_JOURNAL_CONTENT);

        mJournal = new Journal();
        mJournal.setId(journalId);
        mJournal.setUuid(journalUuid);
        mJournal.setTitle(title);
        mJournal.setContent(content);

        mJournalTitleView.setText(title);
        mJournalContentView.setText(content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_journal_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update_journal) {
            updateJournal();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateJournal() {

        if (TextUtils.isEmpty(mJournalTitleView.getText())) {
            Utils.showMessage(mJournalContentView, "Please enter title");
        } else {
            String title = mJournalTitleView.getText().toString();
            String content = mJournalContentView.getText().toString();


            mReplyIntent.putExtra(EXTRA_REPLY_ID, mJournal.getId());
            mReplyIntent.putExtra(EXTRA_REPLY_UUID, mJournal.getUuid());
            mReplyIntent.putExtra(EXTRA_REPLY_TITLE, title);
            mReplyIntent.putExtra(EXTRA_REPLY_CONTENT, content);

            setResult(RESULT_OK, mReplyIntent);
            finish();
        }
    }
}
