/**
* @author  Dhwanil Shah
* @version 1.0
* @since   2018-05-18 
*/

package com.cnhind.framework.test.api;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

/**
 * 
 * Provides methods for reading data from Excel files
 *
 */
public class ExcelReader {
	/**
	 * Loads specified Excel file and returns data from specified sheet
	 * @param fileName Name of the Excel (*.xlsx, *.xls) file to load. The file should be placed in actual test project's "src/test/resources/" directory
	 * @param sheetName Name of the sheet from which the data needs to be fetched
	 * @return Array of arrays with first dimension being rows and second dimension being cell data for each row
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public static String[][] getExcelData(String fileName, String sheetName) throws EncryptedDocumentException, InvalidFormatException, IOException {
		String dataSets[][] = null;
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(fileName);

		// Create Workbook instance holding reference to .xlsx or .xls file
		Workbook workbook = WorkbookFactory.create(resourceStream);

		// Get first/desired sheet from the workbook
		Sheet sheetToUse = workbook.getSheet(sheetName);

		// count number of active rows
		int totalRows = sheetToUse.getLastRowNum();
		// count number of active columns in row
		int totalColumns = sheetToUse.getRow(0).getLastCellNum();
		// Create array of rows and column
		dataSets = new String[totalRows][totalColumns];
		
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
		
        boolean firstTime = true;
        int rowCounter = 0;
        for (Row row: sheetToUse) {
        	// skip the first row which is expected to contain column headers
        	if (firstTime == false) {
        		for (int columnCounter = 0; columnCounter < totalColumns; columnCounter++) {
        			Cell cell = row.getCell(columnCounter, MissingCellPolicy.RETURN_BLANK_AS_NULL);
	                String cellValue = dataFormatter.formatCellValue(cell);
	                
	                dataSets[rowCounter][columnCounter] = cellValue;
	            }
	            
	            rowCounter++;
        	}
            
        	firstTime = false;
        }

        return dataSets;
	}
}
