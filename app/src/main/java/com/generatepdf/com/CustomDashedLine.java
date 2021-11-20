package com.generatepdf.com;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;

public class CustomDashedLine extends DashedLine {
    public CustomDashedLine(float lineWidth) {
        super(lineWidth);
    }

    @Override
    public void draw(PdfCanvas canvas, Rectangle drawArea) {
        canvas.saveState()
                .setLineWidth(getLineWidth())
                .setStrokeColor(getColor())
                .setLineDash(1, 2, 2)
                .moveTo(drawArea.getX(), drawArea.getY() + getLineWidth() / 2)
                .lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + getLineWidth() / 2)
                .stroke()
                .restoreState();
    }
}