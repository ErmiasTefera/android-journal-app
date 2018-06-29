package com.example.ermia.journalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.Utils;

public class NewJournalActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_TITLE = "TITLE";
    public static final String EXTRA_REPLY_CONTENT = "CONTENT";


    Toolbar mToolBar;
    EditText mJournalTitleView;
    EditText mJournalContentView;

    Intent mReplyIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal);

        mToolBar = findViewById(R.id.toolbar_new_journal);
        mToolBar.setTitle("");

        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, mReplyIntent);
                finish();
            }
        });

        mJournalTitleView = findViewById(R.id.et_new_journal_title);
        mJournalContentView = findViewById(R.id.et_new_journal_content);


        mReplyIntent = new Intent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_journal_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_journal) {
            addJournal();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addJournal() {

        if (TextUtils.isEmpty(mJournalTitleView.getText())) {
            Utils.showMessage(mJournalContentView, "Please enter title");
        } else {
            String title = mJournalTitleView.getText().toString();
            String content = mJournalContentView.getText().toString();

            mReplyIntent.putExtra(EXTRA_REPLY_TITLE, title);
            mReplyIntent.putExtra(EXTRA_REPLY_CONTENT, content);
            setResult(RESULT_OK, mReplyIntent);
            finish();
        }

    }
}
