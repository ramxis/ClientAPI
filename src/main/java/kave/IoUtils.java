package kave;
/**
 * Copyright 2016 - Muhammad Rameez
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
 *    
 */
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
		List<ModelDescriptor> jsonArray = getAll();
		if (indexFileExits()) {
			if (!(jsonArray.contains(fileDescriptor))) {
				jsonArray.add(fileDescriptor);
				writeIndex(jsonArray);
				return true;

			} else {
				// descriptor for updated file exists: do nothing
				return true;
			}
		} else { // index file does not exist:create it

			jsonArray.add(fileDescriptor);
			if (!dataDir.exists()) {
				dataDir.mkdir();
			}
			writeIndex(jsonArray);
			return true;
		}
	}
	
	public boolean detectCollision(ModelDescriptor fileDescriptor) throws JSONException, IOException {
		List<ModelDescriptor> jsonArray = getAll();
		if(jsonArray.contains(fileDescriptor))
		{
			return true; // if file exists collision is detected
		}
		else
			return false;
	}

	private void writeIndex(List<ModelDescriptor> index) {
		Gson gson = new Gson();
		String json = gson.toJson(index);

		try {
			FileWriter writer = new FileWriter(Path);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean removeIndex(ModelDescriptor delFileDescriptor) throws IOException {
		List<ModelDescriptor> tmp = getAll();
		if (tmp.contains(delFileDescriptor)) {
			tmp.remove(delFileDescriptor);
			writeIndex(tmp);
			return true;

		} else {
			return false;
		}
	}

	// all file related functions
	public boolean removeFile(ModelDescriptor delFileDescriptor) throws JSONException, IOException {
		File tmpFile = getFileName(dataDir, delFileDescriptor);
		if (tmpFile.exists()) {
			tmpFile.delete();
			removeIndex(delFileDescriptor);
			return true;
		}
		return false;
	}

	private static File writeTmp(byte[] filebyte, File file) throws IOException {
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(filebyte);
		fop.flush();
		fop.close();
		return file;
	}

	public boolean addFile(ModelDescriptor modelDesc, byte[] filebyte) throws IOException, JSONException {
		// TODO Auto-generated method stub
		File tmpFile = getFileName(tmpDir, modelDesc);
		File dataFile = getFileName(dataDir, modelDesc);
		File file = writeTmp(filebyte, tmpFile);
		// FileUtils.moveFile(file, dataFile);//for production comment in
		FileUtils.copyFile(file, dataFile);
		FileUtils.forceDelete(file);
		return addIndex(modelDesc);

	}

	private static File getFileName(File dir, ModelDescriptor modelDesc) {
		String filename = modelDesc.getname();
		String fileversion = modelDesc.getversion();
		String fileName = filename + "-" + fileversion + ".zip";
		return new File(dir.getPath() + "\\" + fileName);
	}

	public List<ModelDescriptor> getAll() throws IOException {
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		if (indexFileExits()) {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(Path));

			tmp = gson.fromJson(reader, new TypeToken<List<ModelDescriptor>>() {
			}.getType());

		} else {
			this.indexFile = new File(Path);
		}
		return tmp;
	}

}
