package com.generatepdf.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String file_name_path= "";
    int PERMISSION_ALL = 5;
    public static final String[][] DATA = {
            {"John Edward Jr.", "AAA"},
            {"Pascal Einstein W. Alfi", "BBB"},
    };
    String[] PERMISSIONS = {

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    @SuppressLint("StaticFieldLeak")
    public static String studentJson="";
    @SuppressLint("StaticFieldLeak")
    public static List<StudentModel> studentModels=new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static ListView list_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_item=findViewById(R.id.list_item);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        final String fileName = "Student List";

        file_name_path = fileName + ".pdf";
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, file_name_path);

        if (!file.exists()) {
            file.mkdir();
        }

        gettingStudentDetails();
    }

    public void generateStaticPDF(View view) {
       /// createpdf();
    }

    public void generateDynamicPDF(View view)  {
        try {
            // this method used by iText7  library
            createPdf();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Cell getCell(String text, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(10);
        cell.setTextAlignment(alignment);
        //  cell.setBorder(Border.NO_BORDER);
        return cell;
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


    private void gettingStudentDetails(){
        String json = null;
        try {
            InputStream inputStream = MainActivity.this.getAssets().open("studentDetails.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            try {
                JSONObject jsonObject=new JSONObject(json);
                JSONObject json1=jsonObject.getJSONObject("data");
                JSONArray jsonArray=json1.getJSONArray("Students");
                studentJson=String.valueOf(jsonArray);
                callingStudentList();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Information Getting Null", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callingStudentList(){
        try{
            if(studentJson==null||studentJson.trim().length()==0||studentJson.equalsIgnoreCase("null")){
                return;
            }
            studentModels=new ArrayList<>();
            list_item.setAdapter(null);
            JSONArray jsonArray=new JSONArray(studentJson);
            if(jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    studentModels.add(new StudentModel(jsonObject.getString("Roll_Number"),jsonObject.getString("Name"),jsonObject.getString("Class"),
                            jsonObject.getString("Section"),jsonObject.getString("Gender"),jsonObject.getString("Total_Marks")));
                }
                StudentAdapter studentAdapter=new StudentAdapter(MainActivity.this,studentModels,0);
                list_item.setAdapter(studentAdapter);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void createPdf() throws IOException{
        try {
            final String fileName = "Student List";
            file_name_path = fileName + ".pdf";
            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath, file_name_path);

            com.itextpdf.kernel.pdf.PdfWriter pdfWriter = new PdfWriter(file);
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.addNewPage();
            com.itextpdf.layout.Document document = new Document(pdfDocument, PageSize.A4);
            document.setMargins(36, 36, 36, 36);


            Style headerBold = new Style();
            PdfFont font = PdfFontFactory.createFont(FontConstants.COURIER);
            headerBold.setFont(font).setFontSize(20).setBold();
            headerBold.setFontColor(com.itextpdf.kernel.colors.Color.convertCmykToRgb(DeviceCmyk.BLACK));


            Style normal = new Style();
            PdfFont font2 = PdfFontFactory.createFont(FontConstants.COURIER);
            normal.setFont(font2).setFontSize(10);
            normal.setFontColor(com.itextpdf.kernel.colors.Color.convertCmykToRgb(DeviceCmyk.BLACK));

            Style normalBold = new Style();
            PdfFont font3 = PdfFontFactory.createFont(FontConstants.COURIER);
            normalBold.setFont(font3).setFontSize(10).setBold();
            normalBold.setFontColor(com.itextpdf.kernel.colors.Color.convertCmykToRgb(DeviceCmyk.BLACK));


            Style nlBold = new Style();
            PdfFont font4 = PdfFontFactory.createFont(FontConstants.COURIER);
            nlBold.setFont(font4).setFontSize(12).setBold();
            nlBold.setFontColor(com.itextpdf.kernel.colors.Color.convertCmykToRgb(DeviceCmyk.BLACK));


            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String createdDate = simpleDateFormat.format(calendar.getTime());
            String dates = createdDate;

            Drawable d = getDrawable(R.drawable.welcome_friends);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image image = new Image(imageData);
            document.add(image);

            Paragraph header = new Paragraph("Student List").addStyle(headerBold).setTextAlignment(TextAlignment.CENTER);
            document.add(header);
            LineSeparator dashedLine = new LineSeparator(new CustomDashedLine(2f));
            document.add((IBlockElement) dashedLine);
            Paragraph p1 = new Paragraph("" ).addStyle(normalBold).setTextAlignment(TextAlignment.LEFT);
            p1.add(new Tab());
            p1.addTabStops(new TabStop(1050, TabAlignment.RIGHT));
            if (dates.contains(" ")) {
                String[] add = dates.split(" ");
                p1.add("Date :" + add[0]);

            }
            document.add(p1);

            Paragraph p0 = new Paragraph("School Name : ").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p0.add(new Tab());
            p0.addTabStops(new TabStop(160, TabAlignment.CENTER));
            p0.add("ABC");
            document.add(p0);
            Paragraph p00 = new Paragraph("University Name : ").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p00.add(new Tab());
            p00.addTabStops(new TabStop(100, TabAlignment.CENTER));
            p00.add("JAC");
            document.add(p00);
            Paragraph p001 = new Paragraph("School Address : ").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p001.add(new Tab());
            p001.addTabStops(new TabStop(100, TabAlignment.CENTER));
            p001.add("Jharkhand");
            document.add(p001);
            Paragraph p002 = new Paragraph("School Contact Numebr : ").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p002.add(new Tab());
            p002.addTabStops(new TabStop(100, TabAlignment.CENTER));
            p002.add("111133223333");
            document.add(p002);
            Paragraph p003 = new Paragraph("School Email Id : ").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p003.add(new Tab());
            p003.addTabStops(new TabStop(100, TabAlignment.CENTER));
            p003.add("abc9534@gmail.com");
            document.add(p003);

            float colimnWidth[] = {50f, 100f, 400f, 100f, 100f, 100f, 100f};
            Table table = new Table(colimnWidth);

            table.addCell(getCell("SI. No.", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Roll_Number", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Name", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Class", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Section", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Gender", TextAlignment.CENTER).addStyle(normalBold));
            table.addCell(getCell("Total Marks", TextAlignment.CENTER).addStyle(normalBold));

            if (studentModels != null) {
                if (studentModels.size() > 0) {
                    for (int i = 0; i < studentModels.size(); i++) {
                        String s = String.valueOf(i);

                        try {

                            if (studentModels != null) {
                                if (studentModels.get(i).getName() == null || studentModels.get(i).getName().trim().length() == 0 || studentModels.get(i).getName().equalsIgnoreCase("null")) {

                                    if (studentModels.get(i).getName().length() > 40) {
                                        long length = studentModels.get(i).getName().length();
                                        String name1 = studentModels.get(i).getName().substring(0, 40);
                                        String name2 = studentModels.get(i).getName().substring(40, (int) length);
                                        table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(name1, TextAlignment.LEFT).addStyle(normal));
                                        table.addCell(getCell(name2, TextAlignment.CENTER).addStyle(normal));

                                    } else {
                                        table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getName(), TextAlignment.LEFT).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getRollNumber(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getClasses(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getSection(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getGender(), TextAlignment.CENTER).addStyle(normal));
                                       /* NumberToWordsConverter ntw = new NumberToWordsConverter(); // directly implement this
                                        String return_val_in_english = ntw.convert(number);*/
                                        table.addCell(getCell(studentModels.get(i).getTotalMarks(), TextAlignment.CENTER).addStyle(normal));

                                    }

                                } else {
                                    if (studentModels.get(i).getName().length() > 40) {

                                        table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getName(), TextAlignment.LEFT).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getRollNumber(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getClasses(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getSection(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getGender(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getTotalMarks(), TextAlignment.CENTER).addStyle(normal));

                                    } else {
                                        table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getName(), TextAlignment.LEFT).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getRollNumber(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getClasses(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getSection(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getGender(), TextAlignment.CENTER).addStyle(normal));
                                        table.addCell(getCell(studentModels.get(i).getTotalMarks(), TextAlignment.CENTER).addStyle(normal));

                                    }
                                }
                            } else {
                                if (studentModels.get(i).getName().length() > 40) {

                                    long length = studentModels.get(i).getName().length();
                                    String name1 = studentModels.get(i).getName().substring(0, 40);
                                    String name2 = studentModels.get(i).getName().substring(40, (int) length);
                                    table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getName(), TextAlignment.LEFT).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getRollNumber(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getClasses(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getSection(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getGender(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getTotalMarks(), TextAlignment.CENTER).addStyle(normal));

                                } else {
                                    table.addCell(getCell(s, TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getName(), TextAlignment.LEFT).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getRollNumber(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getClasses(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getSection(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getGender(), TextAlignment.CENTER).addStyle(normal));
                                    table.addCell(getCell(studentModels.get(i).getTotalMarks(), TextAlignment.CENTER).addStyle(normal));

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            document.add(table);

            Paragraph p10 = new Paragraph("For").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            document.add(p10);
            Paragraph p100 = new Paragraph("");
            document.add(p100);
            Paragraph p1001 = new Paragraph("");
            document.add(p1001);
            Paragraph p15 = new Paragraph("School Name").setTextAlignment(TextAlignment.LEFT).addStyle(normal);
            p15.add(new Tab());
            p15.addTabStops(new TabStop(1050, TabAlignment.RIGHT));
            p15.add("Signature of Principal");
            document.add(p15);

            document.add((IBlockElement) dashedLine);
            document.close();
            //   Toast.makeText(mContext, "PDF Created", Toast.LENGTH_SHORT).show();

            System.out.println("sOrder.....PDF Created....file...." + file);
            System.out.println("sOrder.....PDF Created.....pdfWriter..." + pdfWriter);

            viewPdfFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void viewPdfFile() {
        try {
            String pdfPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath +"/"+ file_name_path);
            System.out.println("sOrder.....PDF Created....file..2.."+file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent1= Intent.createChooser(intent, "Open File");

            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}