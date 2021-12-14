package com.generatepdf.com;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;

public class TransparentImage implements IEventHandler {
    protected PdfExtGState gState;
    protected Image img;
    public TransparentImage(Image img) {
        this.img = img;
        gState = new PdfExtGState().setFillOpacity(0.2f);
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.getLastContentStream(), page.getResources(), pdf);
        pdfCanvas.saveState().setExtGState(gState);
        Canvas canvas = new Canvas(pdfCanvas, pdf, page.getPageSize());
        canvas.add(img
                .scaleAbsolute(pageSize.getWidth(), pageSize.getHeight()));
        pdfCanvas.restoreState();
        pdfCanvas.release();
    }
}
