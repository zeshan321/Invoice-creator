package com.zeshanaslam.invoicecreator;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import util.Num;

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
			File file = new File(System.getProperty("user.home") + "/Desktop/" + date1 + " - " + date2 + ".xls");

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

			List<InputObjectString> dataList = dataDB.getDates(date1, date2);
			for (int i = 0; i < dataList.size(); i++) {
				InputObjectString inputObject = dataList.get(i);

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
		try {
			File file = new File(System.getProperty("user.home") + "/Desktop/" + date1 + " - " + date2 + ".pdf");
			
			Document document = new Document();
			
			Paragraph p = new Paragraph("Invoice");
			p.setAlignment(Element.ALIGN_CENTER);
			p.setFont(new Font(Font.FontFamily.UNDEFINED, 30, Font.BOLD));

			PdfPTable info = new PdfPTable(2);
			info.setWidthPercentage(100);
			info.addCell(getCell("123 DUNDAS STREET EAST\nMISSISSAUGA ONTARIO CANADA\nL5A 1w7\nMYELECTRONICS66@GMAIL.COM\n905-272-2777", PdfPCell.ALIGN_LEFT));
			info.addCell(getCell("123 DUNDAS STREET EAST\nMISSISSAUGA ONTARIO CANADA\nL5A 1w7\nMYELECTRONICS66@GMAIL.COM\n905-272-2777", PdfPCell.ALIGN_RIGHT));			
			
			PdfPTable table = new PdfPTable(8);
			
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Date");
			table.addCell("Store");
			table.addCell("Model Number");
			table.addCell("Serial Number");
			table.addCell("Description");
			table.addCell("Status");
			table.addCell("Sub-total");
			table.addCell("Price");
			table.setHeaderRows(1);
			
			// Cell widths
			table.setWidths(new int[] {50, 40, 50, 50, 100, 40, 50, 50});
			PdfPCell[] cells = table.getRow(0).getCells(); 
			for (int j=0;j<cells.length;j++){
				cells[j].setBackgroundColor(BaseColor.WHITE);
			}
			
			List<InputObjectString> dataList = dataDB.getDates(date1, date2);
			for (int i = 0; i < dataList.size(); i++) {
				table.addCell(dataList.get(i).date);
				table.addCell(dataList.get(i).store);
				table.addCell(dataList.get(i).model);
				table.addCell(dataList.get(i).serial);
				table.addCell(dataList.get(i).desc);
				table.addCell(dataList.get(i).status);
				table.addCell(dataList.get(i).price);
				
				double total = Double.parseDouble(dataList.get(i).price) * 1.13;
				table.addCell(String.valueOf(new Num().round(total, 2)));
			}
			
			PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open();
			document.add(p);
			document.add(info);
			document.add(table);
			document.close();

			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PdfPCell getCell(String text, int alignment) {
	    PdfPCell cell = new PdfPCell(new Phrase(text));
	    cell.setPadding(0);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
}
