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
import util.ConfigLoader;
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
		ConfigLoader configLoader = Main.configLoader;
		
		try {
			File file = new File(System.getProperty("user.home") + "/Desktop/" + date1 + " - " + date2 + ".pdf");
			
			Document document = new Document();
			
			Paragraph p = new Paragraph("Invoice", new Font(Font.FontFamily.UNDEFINED, 30, Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			p.setSpacingAfter(30);

			PdfPTable info = new PdfPTable(2);
			info.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			info.addCell(getCell(configLoader.getString("Name"), PdfPCell.ALIGN_LEFT, 1));
			info.addCell(getCell(configLoader.getString("Name1"), PdfPCell.ALIGN_RIGHT, 1));	
			info.addCell(getCell(configLoader.getString("Address") + "\n" + configLoader.getString("Postal") + "\n" + configLoader.getString("Email") + "\n" + configLoader.getString("Phone"), PdfPCell.ALIGN_LEFT, 0));
			info.addCell(getCell(configLoader.getString("Address1") + "\n" + configLoader.getString("Postal1") + "\n" + configLoader.getString("Email1") + "\n" + configLoader.getString("Phone1"), PdfPCell.ALIGN_RIGHT, 0));			
			info.setSpacingAfter(20);
			
			PdfPTable table = new PdfPTable(8);
			
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(getCellHeader("Date"));
			table.addCell(getCellHeader("Store"));
			table.addCell(getCellHeader("Model Number"));
			table.addCell(getCellHeader("Serial Number"));
			table.addCell(getCellHeader("Description"));
			table.addCell(getCellHeader("Status"));
			table.addCell(getCellHeader("Sub-total"));
			table.addCell(getCellHeader("Price"));
			table.setHeaderRows(1);
			table.setSpacingAfter(5);
			
			// Cell widths
			table.setWidths(new int[] {50, 40, 50, 50, 100, 40, 50, 50});
			PdfPCell[] cells = table.getRow(0).getCells(); 
			for (int j=0;j<cells.length;j++){
				cells[j].setBackgroundColor(BaseColor.WHITE);
			}
			double sub = 0;
			double finalTotal = 0;
			
			List<InputObjectString> dataList = dataDB.getDates(date1, date2);
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
				table.addCell(getCellTable("$" + String.valueOf(new Num().round(total, 2))));
				finalTotal = finalTotal + total;
			}
			
			PdfPTable footer = new PdfPTable(1);
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footer.addCell(getCell("Sub-total: $" + sub, PdfPCell.ALIGN_RIGHT, 0));
			footer.addCell(getCell("Total: $" + finalTotal, PdfPCell.ALIGN_RIGHT, 0));
			
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
	    	cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD)));
	    }
	    
	    cell.setPadding(0);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
	
	private PdfPCell getCellTable(String text) {
	    return new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 8)));
	}
	
	private PdfPCell getCellHeader(String text) {
	    return new PdfPCell(new Phrase(text, new Font(Font.FontFamily.UNDEFINED, 10)));
	}
}
