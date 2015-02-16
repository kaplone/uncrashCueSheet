package UCS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;

public class UCS{
	
	static String currentLine;
	static String ls = System.getProperty("line.separator");
	
	public static void main(String [] args) {
		
		if (args.length == 1){
			File f = new File (args[0]);
			if(f.isDirectory()){
				for(File f_temp : f.listFiles() ){
					if (f_temp.isFile() && f_temp.toString().endsWith(".edl") && ! f_temp.toString().endsWith("_mod.edl")){
					    convertir(f_temp);
					}
				}	
			}
			else{
				if (f.toPath().endsWith(".edl") && ! f.toPath().endsWith("_mod.edl")){
				    convertir(f);
				}
			}
		}
		else {
			System.out.println("problème avec la ligne de commande");
		}
	}
		
	public static void convertir(File f){
		try {
			File f_mod = new File (f.toString().replace(".edl", "_mod.edl"));
			Reader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
			FileWriter fw = new FileWriter(f_mod);
			BufferedWriter bw = new BufferedWriter(fw);
			
			while ((currentLine = br.readLine()) != null) {
				
				if(currentLine.contains("C        ")){
					
					String [] elems = currentLine.split(":");

					elems[3] = String.format("%02d", Integer.parseInt(elems[3].substring(0, 5)) / 4000) + " " + elems[3].substring(5);
					elems[6] = String.format("%02d", Integer.parseInt(elems[6].substring(0, 5)) / 4000) + " " + elems[6].substring(5);
					
					currentLine = String.join(":", elems);	
				}	
				bw.write(currentLine + ls);
				
			}
			bw.close();
 
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}	
	}
}
