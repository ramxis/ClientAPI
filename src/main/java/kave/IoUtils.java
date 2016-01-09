package kave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class IoUtils {
	public static final String EMPTY_FILE = "The file is empty or contains no valid feedback.";
	public static final String NO_ZIP = "No valid zip file.";
	public List<ModelDescriptor> index; 
	private File dataDir;
	private File tmpDir;
	private JSONArray json;
	private File indexFile;
	private String Path;
	
	public IoUtils(File tmpDir, File dataDir, String Path) {
		
		this.dataDir = dataDir;
		this.tmpDir = tmpDir;
		this.Path = Path + "index.json";


	}

	// all index related functions
	public boolean indexExits(String indexPath) {
		File tmpFile = new File(indexPath);
		if (tmpFile.exists()) {

			return true;
		}
		return false;

	}

	

	public boolean removeIndex(String jsonString) throws JSONException, IOException {
		/*List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		tmp = getall();
		//ModelDescriptor delModelDesc = DeserializeModelDesc(jsonString);
		if (tmp.contains(delModelDesc))
		{
			tmp.remove(delModelDesc);
			//tmp.indexOf(delModelDesc);
			index = tmp;
			//WriteToindexFile(List<ModelDescriptor> index)
		}
		else
		{
			return false;
		}*/
		
			
		return true;
	}

	

	// all file related functions
	public boolean removeFile(ModelDescriptor delFileDescriptor) {
		String tmpFilePath = dataDir.getPath() + delFileDescriptor.getname() + "-" + delFileDescriptor.getversion()
				+ ".zip";
		// change to a generic naming convention and remove hard coded - and
		// .zip
		File tmpFile = new File(tmpFilePath);
		if (tmpFile.exists()) {
			tmpFile.delete();
			return true;
		}
		return false;
	}

	
	
	
	public File tmpadd(ModelDescriptor modelDesc, byte[] filebyte) throws IOException {
		String filename = "tmp" + modelDesc.getversion();
		String fileversion = "beta";
		String fileName = filename + "-" + fileversion + ".zip";
		File CustomFolder = new File("E:\\Github\\tmpUpload\\");
		if (!CustomFolder.exists()) {
			
			FileUtils.forceMkdir(CustomFolder);
		}
		//File file = new File(tmpDir.getPath() + "\\" + fileName);
		File file = new File("E:\\Github\\tmpUpload\\" + "\\" + fileName);
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(filebyte);
		fop.flush();
		fop.close();
		return file;
	}
	

	public boolean addFile(ModelDescriptor modelDesc, byte[] filebyte) throws IOException, JSONException {
		// TODO Auto-generated method stub
				String filename = modelDesc.getname();
				String fileversion = modelDesc.getversion();
				File file = tmpadd(modelDesc,filebyte);
				String fileName = filename + "-" + fileversion + ".zip"; 
				File dataFile = new File(dataDir.getPath() + "\\" + fileName);
				//FileUtils.moveFile(file, dataFile);
				FileUtils.copyFile(file, dataFile);
				List<ModelDescriptor> jsonArray = getall();
				jsonArray.add(modelDesc);
				Gson gson = new Gson();
		        String json = gson.toJson(jsonArray);
		        try {
		            FileWriter writer = new FileWriter(Path);
		            writer.write(json);
		            writer.close();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        return true;
		
	}
		
      

	public boolean exists(ModelDescriptor modelDesc) throws JSONException {

		for (int i = 0; i < json.length(); i++) {
			if (json.get(i).equals(modelDesc))
				return true;
		}
		return false;
	}

	public void updateJArray(JSONObject obj) {
		json.put(obj);
	}

	public List<ModelDescriptor> addJsonObject(ModelDescriptor modelDesc) {
		JSONObject obj = new JSONObject();
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		tmp.add(modelDesc);
		return tmp;
	}

	// public remove(modeDescritor)
	public List<ModelDescriptor> getall() throws JSONException, IOException {
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>(); ;
		if(indexExits(Path))
		{
			Gson gson = new Gson(); 
			JsonReader reader = new JsonReader(new FileReader(Path));
			
			 tmp = gson.fromJson(reader, new TypeToken<List<ModelDescriptor>>(){}.getType());
			
		}
		else
		{
			this.indexFile = new File(Path);
		}
		return tmp ;
	}

}
