package UCS;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UCS_model {
	
	private ArrayList<String []> tableau;
	private String name;
	private String projectType;
	private String date;
	private File chemin;
	private String total;
	
	public UCS_model(){
		this.tableau = new ArrayList<>();
		
    }

	public ArrayList<String []> getTableau() {
		return tableau;
	}

	public void setTableau(ArrayList<String []> tableau) {
		this.tableau = tableau;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public File getChemin() {
		return chemin;
	}

	public void setChemin(File chemin) {
		this.chemin = chemin;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
