package com.codewithmab.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("About");
        Element whatsappElement = new Element();
        whatsappElement.setIconDrawable(R.mipmap.whatsapp);
        whatsappElement.setTitle("WhatsApp");

        whatsappElement.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=+2348164408811";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });
        Element buyMeACoffee = new Element();
        buyMeACoffee.setIconDrawable(R.mipmap.coffee);
        buyMeACoffee.setTitle("Buy Me A Coffee");

        buyMeACoffee.setOnClickListener(v -> {
            String url = "https://www.buymeacoffee.com/maleeqB";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });
        View aboutPage = new AboutPage(this)
                .setDescription(HtmlCompat.fromHtml(getString(R.string.about_me), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .isRTL(false)
                .enableDarkMode(false)
                .setImage(R.mipmap.maleeqb)
                .addItem(buyMeACoffee)
                .addGroup("Connect with us")
                .addEmail("malikbello084@gmail.com")
                .addItem(whatsappElement)
                .addTwitter("mabelo_89")
                .addGitHub("maleeqB")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        //copyRightsElement.setIconDrawable(R.drawable.copyright_foreground);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(v -> Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show());
        return copyRightsElement;
    }
}
