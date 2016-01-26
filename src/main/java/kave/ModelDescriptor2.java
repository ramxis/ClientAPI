package kave;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//check
public class ModelDescriptor2 {
	private String name;
	private String version;

	public ModelDescriptor2(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public ModelDescriptor2() {
		// TODO Auto-generated constructor stub
	}

	public String getname() {
		return this.name;
	}

	public String getversion() {
		return this.version;
	}
	public void setversion(String newVersion) {
	  this.version=newVersion;
	}
	public void setname(String newName) {
		  this.name=newName;
		}
	

	

}
