package com.generatepdf.com;


import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

public class HeaderHandler implements IEventHandler {
    protected PdfFormXObject template;
    protected PdfFont font;


    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfPage page = docEvent.getPage();
        int pageNum = docEvent.getDocument().getPageNumber(page);

        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText();
        canvas.setFontAndSize(font, 12);
        canvas.beginMarkedContent(PdfName.Artifact);
        canvas.moveText(34, 575);
        canvas.showText("Test");
        canvas.moveText(703, 0);
        canvas.showText(String.format("Page %d of", pageNum));
        canvas.endText();
        canvas.stroke();
        canvas.addXObjectAt(template, 0, 0);
        canvas.endMarkedContent();
        canvas.release();
    }
}

