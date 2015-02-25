package UCS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

public class XLS_SDRM {

	public static void export_xls(UCS_model modele){

		
		
		  NPOIFSFileSystem fs = null;
		  FileOutputStream fileOut = null;
		  
		  int premiere_ligne = 13;
		  int derniere_ligne = 37;
		  int curr_ligne = 13;
		  
		  int [] cases = new int [] {1, 2, 4, 5};
		  
		try {
			
			
		
			fs = new NPOIFSFileSystem(new File("sources/modele_sdrm_head_foot.xls"));
			HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
			Sheet sheet = wb.getSheetAt(0);
			
//			//FileInputStream obtains input bytes from the image file
//		    InputStream inputStream = new FileInputStream("/home/autor/Desktop/EDL/pied.jpg");
//		    //Get the contents of an InputStream as a byte[].
//		    byte[] bytes = IOUtils.toByteArray(inputStream);
//		    //Adds a picture to the workbook
//		    int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
//		    //close the input stream
//		    inputStream.close();
//		 
//		    //Returns an object that handles instantiating concrete classes
//		    CreationHelper helper = wb.getCreationHelper();
//		 
//		    //Creates the top-level drawing patriarch.
//		    Drawing drawing = sheet.createDrawingPatriarch();
//		 
//		    //Create an anchor that is attached to the worksheet
//		    ClientAnchor anchor = helper.createClientAnchor();
//		    //set top-left corner for the image
//		    anchor.setAnchorType(3);
//		    anchor.setDx1(0);
//		    anchor.setDy1(100);
//		    
			
			Row row = sheet.getRow(7);
			Cell cell = row.getCell(3);
		    cell.setCellValue(modele.getName());
		    
		    row = sheet.getRow(10);
			cell = row.getCell(2);
		    cell.setCellValue(modele.getTotal());
		    
		    for (String [] st : modele.getTableau()){
		    	row = sheet.getRow(curr_ligne);
		    	
		    	row.setHeightInPoints(8 * (st[2].split("\n").length + 1));
		    	
		    	for(int i = 0; i < 4; i++ ){
		    		cell = row.getCell(cases[i]);
			    	cell.setCellType(Cell.CELL_TYPE_STRING);
				    cell.setCellValue(st[i]);	
		    	}
		    	curr_ligne ++;
		    }
	    
		 // Write the output to a file
 			fileOut = new FileOutputStream(modele.getChemin().toString().replace(".edl", ".xls"));
 			
// 			//Creates a picture
//		    Picture pict = drawing.createPicture(anchor, pictureIdx);
//		  //Reset the image to the original size
//		    pict.resize();

 			
 		    wb.write(fileOut);
	
			
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		  
		  try {
			fs.close();
		    fileOut.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}	

    }

}
