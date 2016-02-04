package kave;
/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 *    - Muhammad Rameez
 */
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
