package com.generatepdf.com;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.pdf.PdfDocument;
import android.widget.Toast;

import com.itextpdf.layout.Document;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class StaticPdfActivity extends AppCompatActivity {
    Context mContext;
    String file_name_path = "";
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,

    };
    @SuppressLint("StaticFieldLeak")
    static List<StudentModel> studentModelList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticpdf);
        mContext=this;
        gettingData();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!hasPermissions(StaticPdfActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(StaticPdfActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        File file = new File(Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath(), "pdfsdcard_location");
        if (!file.exists()) {
            file.mkdir();
        }
    }
    private void gettingData() {
        try {
            System.out.println("Print...........getdata....0000............");

            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getSerializableExtra("studentList") != null) {

                    studentModelList = (List<StudentModel>) intent.getSerializableExtra("studentList");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clickStaticPDF(View view) {
        createpdf();
    }
    private void createpdf() {
        try {

            System.out.println("sOrder..........start to create pdf..........");
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            Rect bounds = new Rect();
            System.out.println("sOrder..........start to create pdf...bounds......." + bounds);

            int pageWidth = 595;
            int pageheight = 842;
            int pathHeight = 2;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String createdDate = simpleDateFormat.format(calendar.getTime());

            final String fileName = "StudentDetails";
            file_name_path = "/pdfsdcard_location/" + fileName + ".pdf";
            Paint paint = new Paint();
            paint.setTextSize(8);
            Paint paint2 = new Paint();
            Paint paint3 = new Paint();
            Paint paint4 = new Paint();
            Paint paint5 = new Paint();
            Paint cell = new Paint();
            Rect canvasBounds = new Rect();

            paint3.setARGB(350, 254, 0, 0);
            paint3.setTextAlign(Paint.Align.CENTER);
            paint3.setTextSize(12);
            paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint3.setColor(Color.BLACK);

            paint4.setARGB(350, 254, 0, 0);
            paint4.setTextAlign(Paint.Align.CENTER);
            paint4.setTextSize(9);
            paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint4.setColor(Color.BLACK);


            paint5.setARGB(350, 254, 0, 0);
            paint5.setTextAlign(Paint.Align.CENTER);
            paint5.setTextSize(8);
            paint5.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint5.setColor(Color.BLACK);
            Path path = new Path();

            path.lineTo(pageWidth, pathHeight);
            paint2.setColor(Color.GRAY);
            paint2.setStrokeWidth(1f);
            paint2.setStyle(Paint.Style.STROKE);

            PrintAttributes printAttrs = new PrintAttributes.Builder().
                    setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                    setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                    setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 300, 300)).
                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                    build();
            PdfDocument myPdfDocument = new PrintedPdfDocument(this, printAttrs);


            android.graphics.pdf.PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageheight, 1).create();
            //PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(convertHighet, convertWidth, 1).create();
            PdfDocument.Page documentPage = myPdfDocument.startPage(myPageInfo);
            //PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(convertHighet, convertWidth, 1).create();
            System.out.println("sOrder..........start to create pdf...myPageInfo......." + myPageInfo);

            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
            // Table table = new Table(2);
            Canvas canvas = documentPage.getCanvas();
            int y = 25; // x = 10,
            //int x = (canvas.getWidth() / 2);
            int x = 10;




            Paint paintBoldHeader = new Paint();
            paintBoldHeader.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintBoldHeader.setColor(Color.BLACK);
            paintBoldHeader.setTextSize(20);

            Paint paintBold = new Paint();
            paintBold.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paintBold.setColor(Color.BLACK);
            paintBold.setTextSize(8);



            Paint paintLineDash = new Paint();
            path.lineTo(pageWidth, pathHeight);
            paintLineDash.setColor(Color.GRAY);
            paintLineDash.setStyle(Paint.Style.STROKE);
            paintLineDash.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
            paintLineDash.setStrokeWidth(1);
            paintLineDash.setAntiAlias(true);
            path.moveTo(x, y);

            canvas.drawText("Date : ", 400, y, paintBold);
            if (createdDate.contains(" ")) {
                String[] add = createdDate.split(" ");
                canvas.drawText(add[0], 470, y, paint);

            }

            y += paint.descent() - paint.ascent();
            canvas.drawText("", x, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("School Name : ", x, y, paintBold);
            canvas.drawText("ABC", 120, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("University Name : ", x, y, paintBold);
            canvas.drawText("JAC ", 120, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("School Address : ", x, y, paintBold);
            canvas.drawText("Jharkhand ", 120, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("School Contact Numebr :  ", x, y, paintBold);
            canvas.drawText("111133223333 ", 120, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("School Email Id :", x, y, paintBold);
            canvas.drawText("abc9534@gmail.com ", 120, y, paint);

            y += paint.descent() - paint.ascent();
            canvas.drawText("", x, y, paint);

            y += paint.descent() - paint.ascent();
            canvas.drawText("SI. No. ", 30, y, paintBold);
            canvas.drawText("Roll Number", 70, y, paintBold);
            canvas.drawText("Name", 120, y, paintBold);
            canvas.drawText("Class", 290, y, paintBold);
            canvas.drawText("Section", 340, y, paintBold);
            canvas.drawText("Gender", 380, y, paintBold);
            canvas.drawText("Total Marks", 420, y, paintBold);

            y += paint.descent() - paint.ascent();
            canvas.drawText("", x, y, paint);
            canvas.drawLine(30, y, myPageInfo.getPageWidth() - 30, y, paintBold);

            if (studentModelList != null) {
                if (studentModelList.size() > 0) {
                    for (int i = 0; i < studentModelList.size(); i++) {
                        String s = String.valueOf(i);
                        try {
                            if (studentModelList != null) {
                                if (studentModelList.get(i).getName() == null || studentModelList.get(i).getName().trim().length() == 0 || studentModelList.get(i).getName().equalsIgnoreCase("null")) {

                                    if (studentModelList.get(i).getName().length() > 40) {
                                        long length = studentModelList.get(i).getName().length();
                                        String name1 = studentModelList.get(i).getName().substring(0, 40);
                                        String name2 = studentModelList.get(i).getName().substring(40, (int) length);


                                        canvas.drawText(s, 35, y, paint);
                                        canvas.drawText(name1, 70, y, paint);
                                        y += paint.descent() - paint.ascent();
                                        canvas.drawText("", x, y, paint);
                                        y += paint.descent() - paint.ascent();
                                        canvas.drawText(name2, 70, y, paint);
                                        y += paint.descent() - paint.ascent();
                                        canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);

                                    } else {

                                        y += paint.descent() - paint.ascent();
                                        canvas.drawText(s, 35, y, paint);
                                        canvas.drawText(studentModelList.get(i).getRollNumber(), 70, y, paint);
                                        canvas.drawText(studentModelList.get(i).getName(), 120, y, paint);
                                        canvas.drawText(studentModelList.get(i).getClasses(), 290, y, paint);
                                        canvas.drawText(studentModelList.get(i).getSection(), 340, y, paint);
                                        canvas.drawText(studentModelList.get(i).getGender(), 380, y, paint);
                                        canvas.drawText(studentModelList.get(i).getTotalMarks(), 420, y, paint);

                                        y += paint.descent() - paint.ascent();
                                        canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);

                                    }

                                } else {
                                    System.out.println("sOder.........6..00..........PoductList.....getProductName....." + studentModelList.get(i).getName());

                                    if (studentModelList.get(i).getName().length() > 40) {
                                        y += paint.descent() - paint.ascent();
                                        canvas.drawText(s, 35, y, paint);
                                        canvas.drawText(studentModelList.get(i).getRollNumber(), 70, y, paint);
                                        canvas.drawText(studentModelList.get(i).getName(), 120, y, paint);
                                        canvas.drawText(studentModelList.get(i).getClasses(), 290, y, paint);
                                        canvas.drawText(studentModelList.get(i).getSection(), 340, y, paint);
                                        canvas.drawText(studentModelList.get(i).getGender(), 380, y, paint);
                                        canvas.drawText(studentModelList.get(i).getTotalMarks(), 420, y, paint);

                                        y += paint.descent() - paint.ascent();
                                        canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);

                                    } else {
                                        y += paint.descent() - paint.ascent();
                                        canvas.drawText(s, 35, y, paint);
                                        canvas.drawText(studentModelList.get(i).getRollNumber(), 70, y, paint);
                                        canvas.drawText(studentModelList.get(i).getName(), 120, y, paint);
                                        canvas.drawText(studentModelList.get(i).getClasses(), 290, y, paint);
                                        canvas.drawText(studentModelList.get(i).getSection(), 340, y, paint);
                                        canvas.drawText(studentModelList.get(i).getGender(), 380, y, paint);
                                        canvas.drawText(studentModelList.get(i).getTotalMarks(), 420, y, paint);

                                        y += paint.descent() - paint.ascent();
                                        canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);

                                    }

                                }
                            } else {
                                System.out.println("sOder.........6............PoductList.....getProductName....." + studentModelList.get(i).getName());

                                if (studentModelList.get(i).getName().length() > 40) {
                                    y += paint.descent() - paint.ascent();
                                    canvas.drawText(s, 35, y, paint);
                                    canvas.drawText(studentModelList.get(i).getRollNumber(), 70, y, paint);
                                    canvas.drawText(studentModelList.get(i).getName(), 120, y, paint);
                                    canvas.drawText(studentModelList.get(i).getClasses(), 290, y, paint);
                                    canvas.drawText(studentModelList.get(i).getSection(), 340, y, paint);
                                    canvas.drawText(studentModelList.get(i).getGender(), 380, y, paint);
                                    canvas.drawText(studentModelList.get(i).getTotalMarks(), 420, y, paint);

                                    y += paint.descent() - paint.ascent();
                                    canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);


                                } else {

                                    y += paint.descent() - paint.ascent();
                                    canvas.drawText(s, 35, y, paint);
                                    canvas.drawText(studentModelList.get(i).getRollNumber(), 70, y, paint);
                                    canvas.drawText(studentModelList.get(i).getName(), 120, y, paint);
                                    canvas.drawText(studentModelList.get(i).getClasses(), 290, y, paint);
                                    canvas.drawText(studentModelList.get(i).getSection(), 340, y, paint);
                                    canvas.drawText(studentModelList.get(i).getGender(), 380, y, paint);
                                    canvas.drawText(studentModelList.get(i).getTotalMarks(), 420, y, paint);

                                    y += paint.descent() - paint.ascent();
                                    canvas.drawLine(25, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);


                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            //blank space
            y += paint.descent() - paint.ascent();
            canvas.drawText("", x, y, paint);
            y += paint.descent() - paint.ascent();
            canvas.drawText("", x, y, paint);


            canvas.drawLine(30, y, myPageInfo.getPageWidth() - 30, y, paintLineDash);

            y += paint.descent() - paint.ascent();
            canvas.drawText("School Name", 40, y, paint);
            // canvas.drawLine(400, 40, 0, 0, paintLineDash);

            canvas.drawText("Signature of Principal", 400, y, paintBold);

            documentPage.getCanvas();
            myPdfDocument.finishPage(documentPage);
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/pdfsdcard_location/";
            File file = new File(directory_path);
            //  file.mkdirs();

            if (file.exists()) {
                file.mkdirs();
            }

            String targetPdf = directory_path + fileName + ".pdf";
            File filePath = new File(targetPdf);
            try {
                myPdfDocument.writeTo(new FileOutputStream(filePath));

                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();

            }
            // close the document
            myPdfDocument.close();
            viewPdfFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void viewPdfFile() {
        File file = new File(Environment.getExternalStorageDirectory()+ File.separator +file_name_path);
        System.out.println("sOrder..........pdf view....file..."+file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("\"application/pdf\"");
        intent.setAction(Intent.ACTION_SEND);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_ACTIVITY_NO_HISTORY |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        stopLockTask();
        try {
            startActivity(intent);
            System.out.println("sOrder..........pdf view.......");
        }catch (ActivityNotFoundException e) {
            Toast.makeText(StaticPdfActivity.this, "PDFCreator ActivityNotFoundException:" + e, Toast.LENGTH_SHORT).show();
            System.out.println("sOrder..........pdf view......." + e);

        }        catch (Exception e){
            Toast.makeText(StaticPdfActivity.this, "Exception:" + e, Toast.LENGTH_SHORT).show();


        }

    }
}
