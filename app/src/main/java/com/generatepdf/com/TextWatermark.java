package com.generatepdf.com;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;

public class TextWatermark implements IEventHandler {
    Color lime, blue;
    PdfFont helvetica;
    protected TextWatermark() throws IOException {
        helvetica = PdfFontFactory.createFont(FontConstants.HELVETICA);
        lime = new DeviceCmyk(0.208f, 0, 0.584f, 0);
        blue = new DeviceCmyk(0.445f, 0.0546f, 0, 0.0667f);
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdf.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(), page.getResources(), pdf);
        pdfCanvas.saveState()
                .setFillColor(pageNumber % 2 == 1 ? lime : blue)
                .rectangle(pageSize.getLeft(), pageSize.getBottom(),
                        pageSize.getWidth(), pageSize.getHeight())
                .fill().restoreState();
        if (pageNumber > 1) {
            pdfCanvas.beginText()
                    .setFontAndSize(helvetica, 10)
                    .moveText(pageSize.getWidth() / 2 - 120, pageSize.getTop() - 20)
                    .showText("The Strange Case of Dr. Jekyll and Mr. Hyde")
                    .moveText(120, -pageSize.getTop() + 40)
                    .showText(String.valueOf(pageNumber))
                    .endText();
        }
        pdfCanvas.release();
    }
}
