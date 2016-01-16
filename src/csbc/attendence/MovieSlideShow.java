/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csbc.attendence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTransition;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.util.List;

public class MovieSlideShow {

    /**
     * The resulting PDF file.
     */
    public static final String RESULT = "e:\\movie_slides.pdf";

    /**
     * Path to the resources.
     */
    /**
     * Page event to set the transition and duration for every page.
     */
    class TransitionDuration extends PdfPageEventHelper {

        public void onStartPage(PdfWriter writer, Document document) {
            writer.setTransition(new PdfTransition(PdfTransition.DISSOLVE, 3));
            writer.setDuration(5);
        }

    }

    /**
     * Creates a PDF with information about the movies
     *
     * @param filename the name of the PDF file that will be created.
     * @throws DocumentException
     * @throws IOException
     * @throws SQLException
     */
    public void createPdf(String filename)
            throws IOException, DocumentException, SQLException {
    	// Create a database connection

        // step 1
        Document document = new Document(PageSize.A5.rotate());
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        writer.setViewerPreferences(PdfWriter.PageModeFullScreen);
        writer.setPageEvent(new TransitionDuration());
        // step 3
        document.open();
        // step 4

        Image img;
        PdfPCell cell;

        String path = "e:\\imgpath\\";

        String files = null;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        int counter = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".bmp") || files.endsWith(".BMP")) {
                    counter++;
                }
            }
        }
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{1, 4});
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".bmp") || files.endsWith(".BMP")) {
                    System.out.println(listOfFiles[i].getAbsolutePath());
                    img = Image.getInstance(listOfFiles[i].getAbsolutePath());
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(img);
                    table.addCell(String.valueOf(listOfFiles[i].getAbsolutePath()));
                    table.addCell("Image Path:");
                    table.addCell(String.valueOf(listOfFiles[i].getAbsolutePath()));
                    document.add(table);
                }
            }
        }

        // step 5
        document.close();
        // Close the database connection

    }

    /**
     * Main method creating the PDF.
     *
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public static void main(String[] args)
            throws IOException, SQLException, DocumentException {
        new MovieSlideShow().createPdf(RESULT);
    }
}
