package kave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOExceptionWithCause;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientAPI implements IClientAPI {
	private final File downloadFolder;
	private String baseUrl;

	public ClientAPI(String baseUrl, File downloadFolder) throws IOException {
		this.baseUrl = baseUrl;
		this.downloadFolder = downloadFolder;

		enforceFolders();
	}

	public ClientAPI(File downloadFolder) throws IOException {
		this.downloadFolder = downloadFolder;

		enforceFolders();
	}

	private void enforceFolders() throws IOException {
		if (!downloadFolder.exists()) {
			// File CustomFolder = new File("E:\\Github\\Upload\\");
			FileUtils.forceMkdir(downloadFolder);
		}
	}

	private boolean deleteFile(String deleteURI, String fileName, String Version) {
		SerializeModel serializer = new SerializeModel();
		Client client = Client.create();
		WebResource webResource = client.resource(deleteURI);
		ModelDescriptor modelDesc = new ModelDescriptor(fileName, Version);
		String jsonString = serializer.SerializeModelDesc(modelDesc);
		// client
		ClientResponse response = webResource.type("application/json").delete(ClientResponse.class, jsonString);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		return true;
	}

	private boolean saveFile(File file, String filename) throws IOException {
		if (downloadFolder.exists()) {
			File fileSaved = new File(downloadFolder.getAbsolutePath() + "\\" + filename);
			FileUtils.copyFile(file, fileSaved);
			return true;
		}
		return false;
	}

	private File downloadFile(String downloadURI, String fileName, String Version, boolean index) throws IOException {

		Client client = Client.create();
		String downloadFile = fileName;
		ClientResponse response;
		if (index) {
			downloadFile = downloadFile + ".json";
			WebResource webResource = client.resource(downloadURI + downloadFile);

			response = webResource.type("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
		} else {
			downloadFile = downloadFile + "-" + Version + ".zip";
			WebResource webResource = client.resource(downloadURI + downloadFile);

			response = webResource.type("application/zip").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

		}

		File file = response.getEntity(File.class);
		if (index)
			saveFile(file, downloadFile);
		//saveFile(file, downloadFile);
		return file;
	}

	
	private boolean UploadFile(String uploadURI, ModelDescriptor fileDescriptor, File file) throws IOException {
		UploadObject testObject;
		SerializeModel serializer = new SerializeModel();
		Client client = Client.create();
		WebResource webResource = client.resource(uploadURI);
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		// convert file into array of bytes
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		testObject = new UploadObject(fileDescriptor, bFile);
		String jsonString = serializer.SerializeUploadObj(testObject);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonString);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#upload(kave.ModelDescriptor, java.io.File)
	 */
	@Override
	public boolean upload(ModelDescriptor fileDescriptor, File file, CollisionHandling handling) throws IOException {
		switch (handling) {
		case OVERWRITE:
			return UploadFile(baseUrl + "upload/", fileDescriptor, file);
			//ClientAPI-0.0.1-SNAPSHOT/
			
		case THROW_EXECPTION:
			List<ModelDescriptor> updatedIndex = getIndex();
			if(updatedIndex.contains(fileDescriptor))
			{
				IOException e = new IOException();
				throw e;
			}
				
			else
				return UploadFile(baseUrl + "upload/", fileDescriptor, file);
		default:
			return UploadFile(baseUrl + "upload/", fileDescriptor, file); //default is overwrite existing
			
			
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#getIndex()
	 */
	@Override
	public List<ModelDescriptor> getIndex() throws IOException {
		// TODO Auto-generated method stub
		downloadFile(baseUrl + "static/", "index", null, true);//update/download index file from server
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		File file = new File(downloadFolder.getAbsolutePath() + "\\" + "index.json");
		if(file.exists())
		{
			Gson gson = new Gson(); 
			JsonReader reader = new JsonReader(new FileReader(file));

			tmp = gson.fromJson(reader, new TypeToken<List<ModelDescriptor>>(){}.getType());
		}
		
		return tmp;
	}

	private File downloadFile(String fileName, String Version) throws IOException {
		boolean index = false;
		return downloadFile(baseUrl + "static/", fileName, Version, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#download(kave.ModelDescriptor)
	 */
	@Override
	public File download(ModelDescriptor md) throws IOException {
		return downloadFile(md.getname(), md.getversion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#deleteFile(kave.ModelDescriptor)
	 */
	@Override
	public void delete(ModelDescriptor md) {
		deleteFile(baseUrl + "delete/", md.getname(), md.getversion());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#saveFile
	 */
	@Override
	public boolean saveFile(ModelDescriptor md, File file) throws IOException {
		String fileName = md.getname() + "-" + md.getversion() + ".zip";
		return saveFile(file,fileName);
	}
//remove later
	public void upload2(ModelDescriptor2 umd2, File someFile) throws IOException {
		// TODO Auto-generated method stub
		
		UploadObject2 testObject2;
		SerializeModel2 serializer = new SerializeModel2();
		Client client = Client.create();
		WebResource webResource = client.resource(baseUrl + "upload/");
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) someFile.length()];
		// convert file into array of bytes
		fileInputStream = new FileInputStream(someFile);
		fileInputStream.read(bFile);
		fileInputStream.close();
		testObject2 = new UploadObject2(umd2, bFile);
		String jsonString = serializer.SerializeUploadObj(testObject2);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonString);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		
		
	}
	
	
}
