package com.psquaredsoftware.ygomobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.psquaredsoftware.ygomobile.R;

public class DonationController extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.donatelayout);
        findViewById(R.id.paypalbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ygomobiledonate%40gmail.com&item_name=To+improve/support+YGOMobile+server+hosting.&currency_code=USD&source=url"));
                startActivity(searchIntent);
            }
        });

    }
}
