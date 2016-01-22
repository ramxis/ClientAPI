package kave;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//check
public class ModelDescriptor {
	private String name;
	private String version;

	public ModelDescriptor(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public ModelDescriptor() {
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
	@Override
    public boolean equals(Object o)
    {
		 //boolean retVal = false;
		 ModelDescriptor tmp;

        if (o != null && o instanceof ModelDescriptor)
        {
        	tmp =  (ModelDescriptor) o;
            if(this.name.equals(tmp.name) && this.version.equals(tmp.version))
            return true;
                   
        }
        return false;
        
    }
	@Override public int hashCode() 
	{ 
	final int prime = 31; 
	int result = 1; 
	result = prime * result + ((name == null) ? 0 : name.hashCode()); 
	result = prime * result + ((version == null) ? 0 : version.hashCode()); 
	return result; 
	}


	

}
