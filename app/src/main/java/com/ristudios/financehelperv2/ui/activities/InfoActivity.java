package com.ristudios.financehelperv2.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.ristudios.financehelperv2.R;

public class InfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_info);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(R.string.info);
        initMail();
    }

    private void initMail() {
        TextView txtMail = findViewById(R.id.txt_support_mail);
        txtMail.setOnClickListener(v -> {
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"randomintstudios@gmail.com"});
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "FinanceHelperV2 support");
            startActivity(Intent.createChooser(mailIntent, getString(R.string.info_intent_chooser_mail)));
        });
    }
}