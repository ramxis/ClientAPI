package kave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


public class IoUtils {
	public static final String EMPTY_FILE = "The file is empty or contains no valid feedback.";
	public static final String NO_ZIP = "No valid zip file.";
	public List<ModelDescriptor> index; 
	private File dataDir;
	private File tmpDir;
	private File indexFile;
	private String Path;

	public IoUtils(File tmpDir, File dataDir, String Path) {

		this.dataDir = dataDir;
		this.tmpDir = tmpDir;
		this.Path = Path + "\\" + "index.json";
		

	}

	public void enforceFolders() throws IOException {
		if (!dataDir.exists()) {

			FileUtils.forceMkdir(dataDir);
		}
		if (!tmpDir.exists()) {
			FileUtils.forceMkdir(tmpDir);
		}
		
		throw new RuntimeException("test e before using");
		
	}
	
	// all index related functions
	private boolean indexFileExits() {
		if (new File(this.Path).exists()) {

			return true;
		}
		return false;

	}


	public boolean addIndex(ModelDescriptor fileDescriptor) throws JSONException, IOException {
		List<ModelDescriptor> jsonArray = new ArrayList<ModelDescriptor>();
		jsonArray = getAll();
		if(indexFileExits())
		{
			if(!(jsonArray.contains(fileDescriptor)))
			{
				jsonArray.add(fileDescriptor);
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
			else
			{
				//descriptor for updated file exists: do nothing
				return true;
			}
		}
		else
		{	//index file does not exist:create it
			
			jsonArray.add(fileDescriptor);
			Gson gson = new Gson();
			String json = gson.toJson(jsonArray);
			if (!dataDir.exists())
			{
				dataDir.mkdir();
			}
			try {
				FileWriter writer = new FileWriter(Path);
				writer.write(json);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
	}


	public boolean removeIndex(ModelDescriptor delFileDescriptor) throws IOException {
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		tmp = getAll();
		if(tmp.contains(delFileDescriptor))
		{
			tmp.remove(delFileDescriptor);
			Gson gson = new Gson();
			String json = gson.toJson(tmp);
			try {
				FileWriter writer = new FileWriter(Path);
				writer.write(json);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

		}
		else
		{
			return false;
		}
	}


	// all file related functions
	public boolean removeFile(ModelDescriptor delFileDescriptor) throws JSONException, IOException {
		String tmpFilePath = dataDir.getPath() +"\\"+ delFileDescriptor.getname() + "-" + delFileDescriptor.getversion()
		+ ".zip";

		File tmpFile = new File(tmpFilePath);
		if (tmpFile.exists()) {
			tmpFile.delete();
			removeIndex(delFileDescriptor);
			return true;
		}
		return false;
	}




	public File tmpAdd(ModelDescriptor modelDesc, byte[] filebyte) throws IOException {
		String filename = modelDesc.getname();
		String fileversion = modelDesc.getversion();
		String fileName = filename + "-" + fileversion + ".zip";
		File file = new File(tmpDir.getPath() + "\\" + fileName);
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
		File file = tmpAdd(modelDesc,filebyte);
		String fileName = filename + "-" + fileversion + ".zip"; 
		File dataFile = new File(dataDir.getPath() + "\\" + fileName);
		//FileUtils.moveFile(file, dataFile);//for production comment in
		FileUtils.copyFile(file, dataFile);
		FileUtils.forceDelete(file);
		return addIndex(modelDesc);

	}


	public List<ModelDescriptor> getAll() throws IOException {
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		if(indexFileExits())
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
