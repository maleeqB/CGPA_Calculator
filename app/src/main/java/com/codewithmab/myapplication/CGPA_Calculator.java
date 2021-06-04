package com.codewithmab.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;

public class CGPA_Calculator extends AppCompatActivity {
    LinearLayout cgpaCalcLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cgpa_calculator);

        cgpaCalcLinearLayout = findViewById(R.id.cgpaCalcLinearLayout);
        popupInstructions();

    }

    void popupInstructions(){
        CardView instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, null);
        TextView instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(getString(R.string.app_info));
        cgpaCalcLinearLayout.addView(instructionCardView);

        instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, null);
        instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(HtmlCompat.fromHtml(getString(R.string.instruction_details), HtmlCompat.FROM_HTML_MODE_LEGACY));
        cgpaCalcLinearLayout.addView(instructionCardView);

        instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, null);
        instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(HtmlCompat.fromHtml(getString(R.string.instruction_addcourse), HtmlCompat.FROM_HTML_MODE_LEGACY));
        cgpaCalcLinearLayout.addView(instructionCardView);
    }
}

