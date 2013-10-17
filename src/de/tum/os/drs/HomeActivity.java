package de.tum.os.drs;

import java.util.Locale;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import de.tum.os.drs.helpers.OAuthHelper;
import de.tum.os.drs.scanner.frags.IBarcodeScanner;
import de.tum.os.drs.scanner.frags.RentFragment;

public class HomeActivity extends FragmentActivity implements IBarcodeScanner {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    RentFragment rentFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    String tokenKey = "oAuthTokenKey";
    String token = null;
    SharedPreferences prefs;
    int PICK_ACCOUNT_REQUEST = 999;

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;
    static final int CAPTURE_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awaiting_login_layout);

        tryToAuthenticate();


//        setContentView(R.layout.activity_main);
//
//
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the app.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    private void tryToAuthenticate() {
        // Check for token
        prefs = getPreferences(MODE_PRIVATE);
        token = prefs.getString(tokenKey, null);
        if (token == null) {
            Intent accountPicker = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
            startActivityForResult(accountPicker, PICK_ACCOUNT_REQUEST);
        } else {

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Google account picker result
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(this, "Account Name=" + accountName, 3000).show();

            AsyncTask<Void, Void, String> getTokenTask = new AsyncTask<Void, Void, String>() {
                String token = null;

                @Override
                protected String doInBackground(Void... params) {
                    try {
                        token = GoogleAuthUtil.getToken(HomeActivity.this, accountName, "oauth2:" + OAuthHelper.GOOGLE_EMAIL + " " + OAuthHelper.GOOGLE_USERINFO);
                    } catch (UserRecoverableAuthException e) {
                        startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                    } catch (Exception e) {
                        Log.w("DRS", e.getMessage() + e.getCause());
                    }
                    return token;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null && result.length()>0) {
                        Toast.makeText(HomeActivity.this, "Token=" + result, 3000).show();
                    }
                }

            };
            getTokenTask.execute();

            return;
        }
    }

    private void switchToMainLayout() {
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public void scanBarcode(Fragment callingFrag) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);


        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new RentFragment();
            if (position == 0) {
                rentFragment = (RentFragment) fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    
}
