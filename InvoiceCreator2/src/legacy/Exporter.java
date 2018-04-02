package legacy;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.zeshanaslam.InvoiceCreator.Main;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.*;
import legacy.objects.InputObject;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Exporter {

    private int type;
    private String date1;
    private String date2;

    public Exporter(int type, String date1, String date2) {
        this.type = type;
        this.date1 = date1;
        this.date2 = date2;

        if (type == 1 || type == 2) {
            exportExcel();
        }

        if (type == 0 || type == 2) {
            exportPDF();
        }
    }

    private void exportExcel() {
        DataDB dataDB = new DataDB();
        try {
            File file = new File(date1 + " - " + date2 + ".xls");

            WritableWorkbook wworkbook = Workbook.createWorkbook(file);

            WritableSheet wsheet = wworkbook.createSheet("First Sheet", 0);
            WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), 15, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat = new WritableCellFormat(wfontStatus);

            wsheet.addCell(new Label(0, 0, "Date", cellFormat));
            wsheet.addCell(new Label(1, 0, "Store", cellFormat));
            wsheet.addCell(new Label(2, 0, "Model Number", cellFormat));
            wsheet.addCell(new Label(3, 0, "Serial Number", cellFormat));
            wsheet.addCell(new Label(4, 0, "Description", cellFormat));
            wsheet.addCell(new Label(5, 0, "Status", cellFormat));
            wsheet.addCell(new Label(6, 0, "Sub-total", cellFormat));
            wsheet.addCell(new Label(7, 0, "Price", cellFormat));

            NumberFormat currencyFormat = new NumberFormat("[$$-409]###,###.00", NumberFormat.COMPLEX_FORMAT);
            WritableCellFormat lCurrencyFormat = new WritableCellFormat(currencyFormat);

            List<InputObject> dataList = dataDB.getDates(date1, date2);
            for (int i = 0; i < dataList.size(); i++) {
                InputObject inputObject = dataList.get(i);

                wsheet.addCell(new Label(0, i + 1, inputObject.date));
                wsheet.addCell(new Label(1, i + 1, inputObject.store));
                wsheet.addCell(new Label(2, i + 1, inputObject.model));
                wsheet.addCell(new Label(3, i + 1, inputObject.serial));
                wsheet.addCell(new Label(4, i + 1, inputObject.desc));

                // sub total
                DecimalFormat df = new DecimalFormat("#.00");
                double total = Double.parseDouble(inputObject.price) / 1.13D;
                double sub = Double.parseDouble(inputObject.price) - Double.parseDouble(df.format(total));

                wsheet.addCell(new Label(5, i + 1, String.valueOf(sub), lCurrencyFormat));
                wsheet.addCell(new Label(6, i + 1, inputObject.price, lCurrencyFormat));
                wsheet.addCell(new Label(7, i + 1, inputObject.status));

            }

            wworkbook.write();
            wworkbook.close();

            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportPDF() {
        DataDB dataDB = new DataDB();
        ConfigLoader configLoader = Main.configLoader;

        configLoader.updateConfig(configLoader.getString("Name"), configLoader.getString("Address"), configLoader.getString("Postal"), configLoader.getString("Email"), configLoader.getString("Phone"), configLoader.getString("Name1"), configLoader.getString("Address1"), configLoader.getString("Postal1"), configLoader.getString("Email1"), configLoader.getString("Phone1"), String.valueOf(configLoader.getInt("InvoiceNO") + 1), configLoader.getString("Sales"), configLoader.getString("Tax"));
        try {
            File file = new File(date1 + " - " + date2 + ".pdf");

            Document document = new Document();

            Paragraph p = new Paragraph("Invoice", new Font(Font.FontFamily.UNDEFINED, 30, Font.BOLD));
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(15);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Calendar cal = Calendar.getInstance();

            Paragraph p11 = new Paragraph(configLoader.getString("Name") + ":", new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD));
            Paragraph p22 = new Paragraph(configLoader.getString("Address") + "\n" + configLoader.getString("Postal") + "\n" + configLoader.getString("Email") + "\n" + configLoader.getString("Phone"), new Font(Font.FontFamily.UNDEFINED, 8));
            p11.setSpacingAfter(0);
            p11.setLeading((float) 0.2);
            p11.setMultipliedLeading((float) 0.8);
            p22.setSpacingAfter(0);
            p22.setLeading((float) 0.2);
            p22.setMultipliedLeading((float) 0.8);

            PdfPCell cell = new PdfPCell();
            cell.addElement(p11);
            cell.addElement(p22);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPadding(0);
            cell.setBorder(PdfPCell.NO_BORDER);

            PdfPTable info = new PdfPTable(2);
            info.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            info.addCell(cell);
            info.addCell(getCell("Inovice No.: " + configLoader.getInt("InvoiceNO")
                    + "\nDate: " + dateFormat.format(cal.getTime())
                    + "\nSales: " + configLoader.getString("Sales"), PdfPCell.ALIGN_RIGHT, 1));
            info.addCell(blankCell());
            info.addCell(blankCell());
            info.addCell(getCell("Bill To:", PdfPCell.ALIGN_LEFT, 1));
            info.addCell(blankCell());
            info.addCell(getCell(configLoader.getString("Name1") + ":", PdfPCell.ALIGN_LEFT, 1));
            info.addCell(blankCell());
            info.addCell(getCell(configLoader.getString("Address") + "\n" + configLoader.getString("Postal") + "\n" + configLoader.getString("Email") + "\n" + configLoader.getString("Phone"), PdfPCell.ALIGN_LEFT, 0));
            info.addCell(blankCell());
            info.setSpacingAfter(20);

            PdfPTable table = new PdfPTable(7);

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(getCellHeader("Date"));
            table.addCell(getCellHeader("Store"));
            table.addCell(getCellHeader("Model Number"));
            table.addCell(getCellHeader("Serial Number"));
            table.addCell(getCellHeader("Description"));
            table.addCell(getCellHeader("Status"));
            table.addCell(getCellHeader("Subtotal"));
            table.setHeaderRows(1);
            table.setSpacingAfter(5);

            // Cell widths
            table.setWidths(new int[]{50, 30, 70, 70, 100, 40, 50});
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.WHITE);
            }
            double sub = 0;
            double tax = 0;

            List<InputObject> dataList = dataDB.getDates(date1, date2);
            for (int i = 0; i < dataList.size(); i++) {
                table.addCell(getCellTable(dataList.get(i).date));
                table.addCell(getCellTable(dataList.get(i).store));
                table.addCell(getCellTable(dataList.get(i).model));
                table.addCell(getCellTable(dataList.get(i).serial));
                table.addCell(getCellTable(dataList.get(i).desc));
                table.addCell(getCellTable(dataList.get(i).status));
                table.addCell(getCellTable("$" + dataList.get(i).price));
                sub = sub + Double.parseDouble(dataList.get(i).price);

                double total = Double.parseDouble(dataList.get(i).price) * configLoader.getDouble("Tax");
                tax = tax + total;
            }

            table.addCell(blankCell());
            table.addCell(blankCell());
            table.addCell(blankCell());
            table.addCell(blankCell());
            table.addCell(blankCell());

            table.addCell(getCellTablFooter("Subtotal\nHST\nTotal Amt"));
            table.addCell(getCellTablFooter("$" + sub + "\n$" + new Num().round(tax, 2) + "\n$" + new Num().round(sub + tax, 2)));

            Paragraph footer = new Paragraph("Please contact us for more information about payment options.\nThank you for your business.", new Font(Font.FontFamily.UNDEFINED, 10));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingAfter(30);

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            document.add(p);
            document.add(info);
            document.add(table);
            document.add(footer);
            document.close();

            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell getCell(String text, int alignment, int type) {
        PdfPCell cell = null;

        if (type == 0) {
            cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 8)));
        } else {
            cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
        }

        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell blankCell() {
        PdfPCell cell = new PdfPCell(new Phrase(" ", new Font(Font.FontFamily.UNDEFINED, 8)));
        cell.setPadding(0);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getCellTable(String text) {
        return new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 8)));
    }

    private PdfPCell getCellTablFooter(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 8)));
        return cell;
    }

    private PdfPCell getCellHeader(String text) {
        return new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 10)));
    }
}
