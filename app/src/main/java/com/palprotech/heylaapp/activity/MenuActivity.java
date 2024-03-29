package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

/**
 * Created by Narendar on 24/10/17.
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getName();
    private RelativeLayout vBooking;
    private RelativeLayout vCategory;
    private RelativeLayout vChangeCity;
    private RelativeLayout vWishList;
    private RelativeLayout vShare;
    private RelativeLayout vAboutUs;
    private RelativeLayout vRateUs;
    private RelativeLayout vSignOut;
    private RelativeLayout vSettings;
    private RelativeLayout profileLayout;
    private ImageView vUserImage;
    private TextView profileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        vBooking = (RelativeLayout) findViewById(R.id.booking_history_img);
        vBooking.setOnClickListener(this);
        vCategory = (RelativeLayout) findViewById(R.id.category_img);
        vCategory.setOnClickListener(this);
        vChangeCity = (RelativeLayout) findViewById(R.id.change_city_img);
        vChangeCity.setOnClickListener(this);
        vWishList = (RelativeLayout) findViewById(R.id.wishlist_img);
        vWishList.setOnClickListener(this);
        vShare = (RelativeLayout) findViewById(R.id.share_img);
        vShare.setOnClickListener(this);
        vAboutUs = (RelativeLayout) findViewById(R.id.about_us_img);
        vAboutUs.setOnClickListener(this);
        vSettings = (RelativeLayout) findViewById(R.id.settings_img);
        vSettings.setOnClickListener(this);
        vRateUs = (RelativeLayout) findViewById(R.id.rate_us_img);
        vRateUs.setOnClickListener(this);
        profileLayout = (RelativeLayout) findViewById(R.id.info_layout);
        profileLayout.setOnClickListener(this);
        vSignOut = (RelativeLayout) findViewById(R.id.sign_out_img);
        vSignOut.setOnClickListener(this);
        profileName = findViewById(R.id.profile_name);
        profileName.setText(PreferenceStorage.getFullName(this));
        vUserImage = (ImageView) findViewById(R.id.profile_img);
        String url = PreferenceStorage.getUserPicture(this);
        String getSocialUrl = PreferenceStorage.getSocialNetworkProfileUrl(this);
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.get().load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
        } else if (((getSocialUrl != null) && !(getSocialUrl.isEmpty()))) {
            Picasso.get().load(getSocialUrl).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
        }
        findViewById(R.id.menu_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == vBooking) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent homeIntent = new Intent(getApplicationContext(), BookingHistoryActivity.class);
                startActivity(homeIntent);
            } else {
                guestLoginAlert();
            }
        }
        if (v == vCategory) {
            Intent homeIntent = new Intent(getApplicationContext(), SetUpPreferenceActivity.class);
            startActivity(homeIntent);
        }
        if (v == vChangeCity) {
            Intent homeIntent = new Intent(getApplicationContext(), SelectCityActivity.class);
            startActivity(homeIntent);
        }
        if (v == vSettings) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent homeIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(homeIntent);
            }
            else {
                guestLoginAlert();
            }
        }
        if (v == vWishList) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent homeIntent = new Intent(getApplicationContext(), WishListActivity.class);
                startActivity(homeIntent);
            } else {
                guestLoginAlert();
            }
        }
        if (v == vShare) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Get Heyla app and win some exciting rewards. https://goo.gl/JTmdEX");
            startActivity(Intent.createChooser(i, "Share via"));
        }
        if (v == vAboutUs) {
//            Intent homeIntent = new Intent(getApplicationContext(), BookingActivity.class);
//            startActivity(homeIntent);
        }
        if (v == vRateUs) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.palprotech.heylaapp&hl=en");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == vSignOut) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MenuActivity.this);
            alertDialogBuilder.setTitle("Signout");
            alertDialogBuilder.setMessage("Do you really want to signout?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    doLogout();
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.show();
        }
        if (v == profileLayout) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                startPersonDetailsActivity(-1);
            } else {
                guestLoginAlert();
            }
        }
    }

    public void guestLoginAlert() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MenuActivity.this);
        alertDialogBuilder.setTitle("Login");
        alertDialogBuilder.setMessage("Log in to Access");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                doLogout();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    public void doLogout() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        this.finish();
    }

    public void startPersonDetailsActivity(long id) {
        Intent homeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(homeIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            profileName.setText(PreferenceStorage.getFullName(this));
            String url = PreferenceStorage.getUserPicture(this);
            String getSocialUrl = PreferenceStorage.getSocialNetworkProfileUrl(this);
            if (((url != null) && !(url.isEmpty()))) {
                Picasso.get().load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
            } else if (((getSocialUrl != null) && !(getSocialUrl.isEmpty()))) {
                Picasso.get().load(getSocialUrl).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
            }
        }
    }
}
