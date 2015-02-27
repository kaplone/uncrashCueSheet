package UCS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

import javax.sound.midi.Patch;

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
			
			File f_test = new File(System.getProperty("user.dir") + "/modele_sdrm_head_foot.xls");
			fs = new NPOIFSFileSystem(f_test);
			
			
			HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
			Sheet sheet = wb.getSheetAt(0);
		
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
		    
		    System.out.println("\n\n______Terminé______\n\n");
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}	

    }

}
