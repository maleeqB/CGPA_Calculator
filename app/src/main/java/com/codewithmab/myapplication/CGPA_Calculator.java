package com.codewithmab.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CGPA_Calculator extends AppCompatActivity {
    LinearLayout cgpaCalcLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cgpa_calculator);

        cgpaCalcLinearLayout = findViewById(R.id.cgpaCalcLinearLayout);
        popupInstructions();

        popupCourseEntries();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about){
            startActivity(new Intent(CGPA_Calculator.this, AboutActivity.class));
        }
        return true;
    }

    void popupInstructions(){
        CardView instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, cgpaCalcLinearLayout, false);
        TextView instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(getString(R.string.app_info));
        cgpaCalcLinearLayout.addView(instructionCardView);

        instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, cgpaCalcLinearLayout, false);
        instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(HtmlCompat.fromHtml(getString(R.string.instruction_details), HtmlCompat.FROM_HTML_MODE_LEGACY));
        cgpaCalcLinearLayout.addView(instructionCardView);

        instructionCardView = (CardView) LayoutInflater.from(this).inflate(R.layout.instructions, cgpaCalcLinearLayout, false);
        instructionTextView = instructionCardView.findViewById(R.id.instructionTextView);
        instructionTextView.setText(HtmlCompat.fromHtml(getString(R.string.instruction_addcourse), HtmlCompat.FROM_HTML_MODE_LEGACY));
        cgpaCalcLinearLayout.addView(instructionCardView);
    }

    void popupCourseEntries(){
        LinearLayout lin = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.course_entries, cgpaCalcLinearLayout, false);
        cgpaCalcLinearLayout.addView(lin);

        TableLayout tableLayout = lin.findViewById(R.id.tableLayout);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);

        Button addCourseButton = lin.findViewById(R.id.addCourseButton);
        Button confirmAdditionButton = lin.findViewById(R.id.confirmAdditionButton);
        Button computeCGPA = lin.findViewById(R.id.computeCGPA);

        LinearLayout userInputLinearLayout = lin.findViewById(R.id.userInputLinearLayout);
        LinearLayout errorMsgLinearLayout = lin.findViewById(R.id.errorMsgLinearLayout);

        EditText courseCodeInput = lin.findViewById(R.id.courseCodeInput);
        EditText courseUnitInput = lin.findViewById(R.id.courseUnitInput);
        EditText gradeScoredInput = lin.findViewById(R.id.gradeScoredInput);
        addCourseButton.setOnClickListener(v -> {
            addCourseButton.setVisibility(View.GONE);
            userInputLinearLayout.setVisibility(View.VISIBLE);
        });


        confirmAdditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CGPA_Calculator.this);
                if(emptyInputField(courseCodeInput, courseUnitInput, gradeScoredInput)){
                    TextView errorMsgTextView = new TextView(CGPA_Calculator.this);
                    errorMsgTextView.setTextColor(ContextCompat.getColor(CGPA_Calculator.this, R.color.red));
                    errorMsgTextView.setText(getString(R.string.emptyInputErrorMsg));

                    Button errorButton = new Button(CGPA_Calculator.this);
                    errorButton.setText(getString(R.string.ok));

                    //Remove The Input Fields and Display The Error Message
                    userInputLinearLayout.setVisibility(View.GONE);
                    errorMsgLinearLayout.addView(errorMsgTextView);
                    errorMsgLinearLayout.addView(errorButton);
                    errorButton.setOnClickListener((view) -> resetInput());
                } else
                    verifyCourseInput(courseCodeInput, courseUnitInput, gradeScoredInput);
            }

            private void verifyCourseInput(EditText courseCodeInput, EditText courseUnitInput, EditText gradeScoredInput){
                String code, unit, grade, errorMsg = "";
                code = courseCodeInput.getText().toString(); unit = courseUnitInput.getText().toString(); grade = gradeScoredInput.getText().toString().toUpperCase();

                List<String> validGrades = new ArrayList<>();
                Collections.addAll(validGrades, "A","B","C","D","E","F");
                if(!(unit.matches("[0-9]+")) || !(validGrades.contains(grade))){
                    if(!(unit.matches("[0-9]+")))
                        errorMsg = errorMsg.concat(getString(R.string.invalidUnitErrorMsg, unit));
                    if(!(validGrades.contains(grade)))
                        errorMsg = errorMsg.concat(getString(R.string.invalidGradeErrorMsg, grade, TextUtils.join(",",validGrades)));
                    TextView errorMsgTextView = new TextView(CGPA_Calculator.this);
                    errorMsgTextView.setTextColor(ContextCompat.getColor(CGPA_Calculator.this, R.color.red));
                    errorMsgTextView.setText(HtmlCompat.fromHtml(errorMsg, HtmlCompat.FROM_HTML_MODE_LEGACY));

                    Button errorButton = new Button(CGPA_Calculator.this);
                    errorButton.setText(getString(R.string.ok));

                    //Remove The Input Fields and Display The Error Message
                    userInputLinearLayout.setVisibility(View.GONE);
                    errorMsgLinearLayout.addView(errorMsgTextView);
                    errorMsgLinearLayout.addView(errorButton);

                    errorButton.setOnClickListener((view) -> resetInput());
                } else {
                    addCourseToTable(code, unit, grade);
                    removeInput();
                }

            }
            private void removeInput(){
                courseCodeInput.setText(""); courseUnitInput.setText(""); gradeScoredInput.setText("");
                userInputLinearLayout.setVisibility(View.GONE);
                addCourseButton.setVisibility(View.VISIBLE);
            }
            private void resetInput(){
                userInputLinearLayout.setVisibility(View.VISIBLE);
                courseCodeInput.setText(""); courseUnitInput.setText(""); gradeScoredInput.setText("");
                errorMsgLinearLayout.removeAllViews();
            }
            private boolean emptyInputField(EditText courseCodeInput, EditText courseUnitInput, EditText gradeScoredInput){
                return courseCodeInput.getText().toString().equals("") || courseUnitInput.getText().toString().equals("") || gradeScoredInput.getText().toString().equals("");
            }
            private void hideKeyboard(Activity activity) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = activity.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            private void addCourseToTable(String code, String unit, String grade){
                TableRow tableRow = new TableRow(CGPA_Calculator.this);

                TextView courseCodeCell = new TextView(CGPA_Calculator.this);
                TextView courseUnitCell = new TextView(CGPA_Calculator.this);
                TextView courseGradeCell = new TextView(CGPA_Calculator.this);

                Button x_button = (Button) LayoutInflater.from(CGPA_Calculator.this).inflate(R.layout.x_button, cgpaCalcLinearLayout, false);

                courseCodeCell.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                courseUnitCell.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                courseGradeCell.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                x_button.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                courseCodeCell.setText(code);
                courseUnitCell.setText(unit);
                courseGradeCell.setText(grade);
                x_button.setText("x");

                courseCodeCell.setGravity(Gravity.CENTER);
                courseUnitCell.setGravity(Gravity.CENTER);
                courseGradeCell.setGravity(Gravity.CENTER);

                courseCodeCell.setBackground(ContextCompat.getDrawable(CGPA_Calculator.this, R.drawable.border));
                courseUnitCell.setBackground(ContextCompat.getDrawable(CGPA_Calculator.this, R.drawable.border));
                courseGradeCell.setBackground(ContextCompat.getDrawable(CGPA_Calculator.this, R.drawable.border));

                tableRow.addView(courseCodeCell);
                tableRow.addView(courseUnitCell);
                tableRow.addView(courseGradeCell);
                tableRow.addView(x_button);

                tableLayout.addView(tableRow);

                x_button.setOnClickListener((v) -> tableLayout.removeView(tableRow));
            }
        });

        computeCGPA.setOnClickListener(new View.OnClickListener() {
            TextView resultTextView;
            @Override
            public void onClick(View v) {
                resultTextView = lin.findViewById(R.id.resultTextView);
                if(tableLayout.getChildCount() < 2){
                    Toast.makeText(CGPA_Calculator.this,getString(R.string.resultError), Toast.LENGTH_SHORT).show();
                    resultTextView.setText("");
                    resultTextView.setBackground(null);
                    return;
                }

                int totalCourseUnits = 0;
                int totalGradeScored = 0;

                for (int i = 0; i < getAllCourseUnits().size(); i++) {
                    totalCourseUnits+=getAllCourseUnits().get(i);
                    totalGradeScored+=getAllGradeScored().get(i);
                }
                resultTextView.setBackground(ContextCompat.getDrawable(CGPA_Calculator.this, R.drawable.border));
                resultTextView.setText(String.format(Locale.getDefault(), "%.2f",((float)totalGradeScored / (float)totalCourseUnits)));
            }

            private List<Integer> getAllCourseUnits(){
                List<Integer> courseUnits = new ArrayList<>();
                for (int i = 1; i < tableLayout.getChildCount(); i++) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    TextView courseUnit = (TextView) tableRow.getChildAt(1);
                    courseUnits.add(Integer.valueOf(courseUnit.getText().toString()));
                }
                return courseUnits;
            }
            private List<Integer> getAllGradeScored(){
                List<Integer> courseGrades = new ArrayList<>();
                SimpleArrayMap<String, Integer> gradePoints = new SimpleArrayMap<>();
                gradePoints.put("A",5); gradePoints.put("B",4); gradePoints.put("C",3); gradePoints.put("D",2); gradePoints.put("E",1); gradePoints.put("F",0);

                for (int i = 1; i < tableLayout.getChildCount(); i++) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    int courseUnit = Integer.parseInt(((TextView) tableRow.getChildAt(1)).getText().toString());
                    String gradeScored = ((TextView) tableRow.getChildAt(2)).getText().toString();

                    courseGrades.add(courseUnit * gradePoints.get(gradeScored));
                }
                return courseGrades;
            }
        });
    }


}

