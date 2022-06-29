package utils;

import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class DataDrivenStoreUtils {
//	public static void main(String[] args) {
//		getStoreOrderData();
//	}
	static XSSFWorkbook workbook;
	static XSSFSheet sheet;
	static String excelPath = "./data/StoreTestData.xlsx";
	static String SheetName = "sheet1";
	
	@DataProvider(name = "DataForStoreOrder")
	public static Object[][] getStoreOrderData() {
		Object[][] tabArray = null;
		try {
			DataFormatter formatter = new DataFormatter();
			workbook = new XSSFWorkbook(excelPath);			
			sheet = workbook.getSheet(SheetName);
			int startRow = 1;
			int startCol = 0;
			int ci, cj;
			// To Check the no.of rows in a sheet
			int totalRows = sheet.getPhysicalNumberOfRows();
			XSSFRow row = sheet.getRow(0);
			int totalCols = row.getLastCellNum();
			// System.out.println(totalRows+":::"+totalCols);
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i < totalRows; i++, ci++) {
				cj = 0;
				for (int j = startCol; j < totalCols; j++, cj++) {
					Object value = formatter.formatCellValue(sheet.getRow(i).getCell(j));
					tabArray[ci][cj] = value.toString();
				}
			}
		} catch (IOException e) {
			System.out.println(e.getCause());
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
//		for (int row = 0; row < tabArray.length; row++) { 
//			for (int col = 0; col < tabArray[row].length; col++) 
//			{ 
//				System.out.println(tabArray[row][col]); 
//			} 
//		}
		return tabArray;
	}
}
