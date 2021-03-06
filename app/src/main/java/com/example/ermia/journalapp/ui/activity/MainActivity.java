package com.example.ermia.journalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ermia.journalapp.R;
import com.example.ermia.journalapp.ui.fragment.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    Toolbar mToolbar;
    GoogleSignInAccount mAccount;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mDrawer = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle.syncState();

        mFragmentManager = getSupportFragmentManager();

        mAccount = GoogleSignIn.getLastSignedInAccount(this);

        mAuth = FirebaseAuth.getInstance();

        //loads appropriate view
        setUpView();

    }

    private void setUpNavigationView() {

        FirebaseUser currentUser = mAuth.getCurrentUser();



        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        ImageView ivUserPicture = headerView.findViewById(R.id.iv_user_picture);
        TextView tvUserName = headerView.findViewById(R.id.tv_user_name);
        TextView tvUserEmail = headerView.findViewById(R.id.tv_user_email);

        assert currentUser != null;
        ivUserPicture.setImageURI(currentUser.getPhotoUrl());
        tvUserName.setText(currentUser.getDisplayName());
        tvUserEmail.setText(currentUser.getEmail());

    }

    private void setUpView() {
        //load home fragment
        if (isUserLoggedIn()) {

            setUpNavigationView();

            Fragment fragment = new HomeFragment();
            replaceFragment(fragment);
        } else  {
            showLogin();
        }
    }

    private void showLogin(){
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isUserLoggedIn(){
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account != null;
    }

    //sign out
    private void signOut() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showLogin();
                    }
                });

        FirebaseAuth.getInstance().signOut();
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
