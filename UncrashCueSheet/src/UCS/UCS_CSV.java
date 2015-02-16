package UCS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class UCS_CSV {
	
	private static String entry;
	
	
	private static String ref; // condition : debute par ANW
	private static String ref_int;
	
	private static String ref_title_push;
	private static String ref_title_push_save;
	
	private static HttpClient client;
	private static HttpGet request ;
	
	private static boolean flag_secteur2 = false;
	private static boolean flag_test2 = false;
	
	private static HttpResponse response ;
	
	private static BufferedReader rd;
	
	private static String line ;
	
	private static String ref_base;

	private static String id;
	private static String link;
	private static String isrc;

	private static String currentLine;
	//private static String ls = System.getProperty("line.separator");
	
	private static File f_mod;
	private static Reader fr;
	private static BufferedReader br;
	
	private static FileWriter fw;
	private static BufferedWriter bw;
	
	private static String [] elems;
	
	private static String intro = "\n" +
			                      "\"Production Title:\",\"%s\"\n" +
                                  "\"Production Company:\",\"SATELLITE MULTIMEDIA\"\n" +
                                  "\"Broadcaster/Channel:\",\"%s\"\n" +
                                  "\"Tx Date:\",\"%s %s\"\n\n" +
                                  "\"Title\",\"Composer\",\"Publisher\",\"Sub Publisher\",\"Ref\",\"ISRC\",\"In\",\"Out\",\"Duration\",\"Notes\"\n";
	
	public static String brand = "Audio Network Limited (PRS)";
	
	private static String temps_in;
	private static String temps_out;
	private static String temps_diff;
	
	private static ArrayList<String> prs_ = new ArrayList<>();

	public static void main(String [] args) {
					
		if (args.length == 1){

			File f = new File (args[0]);
			
			if(f.isDirectory()){
				System.out.println(args[0] + " est un répertoire, analyse de son contenu ...");
				for(File f_temp : f.listFiles() ){
					if (f_temp.isFile() && f_temp.toString().endsWith("edl")){
						System.out.println("conversion de " + f_temp);
					    convertir(f_temp); 
					}
				}	
			}
			else{
				System.out.println(args[0] + " est un fichier ...");
				if (f.toString().endsWith("edl")){
					System.out.println("conversion de " + f);
					
					convertir(f);
				}
			}
		}
		else {
			System.out.println("problème avec la ligne de commande");
			convertir(new File("/home/autor/Desktop/EDL/FINAL EDL test.edl"));
		}
	}
	
	public static void convertir(File f_edl){
		
		
		try {
			
			f_mod = new File (f_edl.toString().replace(".edl", ".csv"));
			fr = new FileReader(f_edl);
			br = new BufferedReader(fr);
			fw = new FileWriter(f_mod);
			bw = new BufferedWriter(fw);
	
            while ((currentLine = br.readLine()) != null) {
            	
            	if(currentLine.startsWith("TITLE")){
            		elems =  currentLine.split(":")[1].split("_");
            		bw.write(String.format(intro, elems[3], elems[2], elems[0], elems[1]));
            		
            	}
            	
                if(currentLine.contains("FROM CLIP NAME: ANW")){
                	
                	entry = currentLine.split("FROM CLIP NAME: ")[1];
                	
					ref = String.format("%s/%s", entry.split("_")[0], entry.split("_")[1]);
					ref_int = String.format("%s/%d", entry.split("_")[0], Integer.parseInt(entry.split("_")[1]));
					ref_title_push = Arrays.asList(entry.split("_")[2].split("\\.")[0].split("-")).stream().collect(Collectors.joining(" "));
					ref_title_push_save = ref_title_push;
					
					ref_base = ref.split("/")[0];

					surfer(ref_title_push);
					surfer2();
					
					bw.write(String.format("\"%s\",\"%s\",\"%s\",\"\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"\"\n\n",
							               ref_title_push_save,
							               prs_.stream().collect(Collectors.joining(" / ")),
							               brand,
							               ref_int,
							               isrc,
							               temps_in,
							               temps_out,
							               temps_diff
							               ));
                }
				
				if(currentLine.contains("C        ")){
					
					String t0 = currentLine.substring(57).split(" ")[0];
					String t1 = currentLine.substring(57).split(" ")[1];
					
					int t0_h = Integer.parseInt(t0.split(":")[0]);
					int t0_m = Integer.parseInt(t0.split(":")[1]);
					int t0_s = Integer.parseInt(t0.split(":")[2]);
					int t0_f = Integer.parseInt(t0.split(":")[3]);
					int t1_h = Integer.parseInt(t1.split(":")[0]);
					int t1_m = Integer.parseInt(t1.split(":")[1]);
					int t1_s = Integer.parseInt(t1.split(":")[2]);
					int t1_f = Integer.parseInt(t1.split(":")[3]);
					
					
					int r_s;
					int r_m;
					int r_h;
					
					t0_s = t0_f > 15 ? t0_s + 1 : t0_s;
					t1_s = t1_f > 15 ? t1_s + 1 : t1_s;
					
					if (t0_s <= t1_s) {
						r_s = t1_s - t0_s;
					}
					else {
						r_s = t1_s + 60 - t0_s;
						t0_m ++;
					}
					
					if (t0_m <= t1_m) {
						r_m = t1_m - t0_m;
					}
					else {
						r_m = t1_m + 60 - t0_m;
						t0_h ++;
					}
					r_h = t1_h - t0_h;
					
					temps_in = String.format("%02d:%02d:%02d", t0_h, t0_m, t0_s);
					temps_out = String.format("%02d:%02d:%02d", t1_h, t1_m, t1_s);
					temps_diff = r_h == 0 ? String.format("%d:%02d", r_m, r_s) : String.format("%d:%02d:%02d",r_h, r_m, r_s);
	
				}	
				//bw.write(currentLine + ls);
				
			}
			bw.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		
	}
	
	public static void surfer(String ref_ttl){
		
		id = null;
		link = null;
		isrc = null;
		
		System.out.println(String.format("http://www.audionetwork.com/show-production-results.aspx?stype=4&keywords=%s", ref_base));
		
		client = HttpClientBuilder.create().build();

		try {
			request = new HttpGet(String.format("http://www.audionetwork.com/show-production-results.aspx?stype=4&keywords=%s", ref_base));
			response = client.execute(request);
			
			
			System.out.println(response.toString());
			
			rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			    String line = "";
				while ((line = rd.readLine()) != null) {
					if (! flag_test2 && line.contains(ref_ttl)){
						id = line.split("soundPreview")[1].substring(3, 8);
						flag_test2 = true;
					}
					else if (flag_test2 && line.contains(String.format("%s.aspx", id))){
						link = line.split("\"")[1];
						flag_test2 = false;
						break;
						
					}
				}
				if (link == null){
				    surfer_alt();
				}
				
				

		}catch ( IOException e) {
			// TODO Bloc catch généré automatiquement
			System.out.println("Exeption dans HTTPGet");
		}	
	}
	
	public static void surfer2(){
		
		try{
		
			request = new HttpGet(String.format("http://www.audionetwork.com%s", link));
			response = client.execute(request);
			
			rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

		    line = "";
			while ((line = rd.readLine()) != null) {
				
				if (line.contains(ref_title_push_save)){
					flag_secteur2 = true;
				}
				
				
				if(flag_secteur2 && line.contains("(PRS)")){
					prs_.clear();
					for(String s : line.split("<a href")){
						if (s.contains("PRS")){
							prs_.add(s.split(">")[1].split("<")[0]);
						}
						
					}
				}
				
				else if (flag_secteur2 && line.contains("ISRC")){
					isrc = line.split("</")[2].split("span>")[1];
					flag_secteur2 = false;
					break;
				}
			}

			
			System.out.println(isrc + "\n");
		}catch ( IOException e) {
			// TODO Bloc catch généré automatiquement

		}

	}
	
	public static void surfer_alt(){
		//ref_alt = ref.split("/")[1].substring(0,1);
		ref_title_push = Arrays.asList(ref_title_push.split(" ")).subList(0, ref_title_push.split(" ").length -1).stream().collect(Collectors.joining(" "));
		//surfer(String.format("%s/%s", ref.split("/")[0], ref_alt));
		System.out.println("alt : " + ref_title_push);
		surfer(ref_title_push);

	}
	
}
