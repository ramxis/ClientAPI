package kave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.metal.MetalFileChooserUI.FilterComboBoxRenderer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOExceptionWithCause;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientAPI<T> implements IClientAPI {
	private final File downloadFolder;
	private String baseUrl;
	private IHttpUtils<T> http;
	private Result<String> response;
	private Result<File> fileContent;

	@SuppressWarnings("unchecked")
	public ClientAPI(String baseUrl, File downloadFolder) throws IOException {
		this.baseUrl = baseUrl;
		this.downloadFolder = downloadFolder;
		http = new HttpUtils();
		enforceFolders();
	}

	public ClientAPI(File downloadFolder) throws IOException {
		this.downloadFolder = downloadFolder;
		http = new HttpUtils();
		enforceFolders();
	}

	private void enforceFolders() throws IOException {
		if (!downloadFolder.exists()) {
			// File CustomFolder = new File("E:\\Github\\Upload\\");
			FileUtils.forceMkdir(downloadFolder);
		}
	}

	private Result<String> deleteFile(String deleteURI, String fileName, String Version) {
		SerializeModel serializer = new SerializeModel();

		ModelDescriptor modelDesc = new ModelDescriptor(fileName, Version);
		String jsonString = serializer.SerializeModelDesc(modelDesc);
		// client
		response = http.delete(jsonString, deleteURI);
		if (response.isOk())
			return response;
		else
			return response;

	}

	private boolean saveFile(File file, String filename) throws IOException {
		if (downloadFolder.exists()) {
			File fileSaved = new File(downloadFolder.getAbsolutePath() + "\\" + filename);
			FileUtils.copyFile(file, fileSaved);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private Result<File> downloadFile(String downloadURI, String fileName, String Version) throws IOException {

		String downloadFile = fileName;

		/*if (index) {
			downloadFile = downloadFile + ".json";
			fileContent =  (Result<File>) http.download(downloadURI + downloadFile);

		} else {*/
			downloadFile = downloadFile + "-" + Version + ".zip";
			fileContent =  (Result<File>) http.download(downloadURI + downloadFile);

		//}

		//File file = fileContent.getContent();
		//if (index)
			//saveFile(file, downloadFile);
		// saveFile(file, downloadFile);
		return fileContent;
	}

	private Result<String> UploadFile(String uploadURI, ModelDescriptor fileDescriptor, File file) throws IOException {

		UploadObject testObject;
		SerializeModel serializer = new SerializeModel();
		// Client client = Client.create();
		// WebResource webResource = client.resource(uploadURI);
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		// convert file into array of bytes
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		testObject = new UploadObject(fileDescriptor, bFile);
		String jsonString = serializer.SerializeUploadObj(testObject);
		response = http.upload(jsonString, uploadURI);
		if (response.isOk())
			return response;
		else
			return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#upload(kave.ModelDescriptor, java.io.File)
	 */
	@Override
	public Result<String> upload(ModelDescriptor fileDescriptor, File file, CollisionHandling handling) throws IOException {
		switch (handling) {
		case OVERWRITE:
			return UploadFile(baseUrl + "upload/", fileDescriptor, file);
		// ClientAPI-0.0.1-SNAPSHOT/

		case THROW_EXECPTION:
			UploadFile(baseUrl + "upload2/", fileDescriptor, file);
			if (response.getErrorMessage().equals("THROW_EXECPTION")) {
				IOException e = new IOException();
				throw e;
			}

		default:
			return UploadFile(baseUrl + "upload/", fileDescriptor, file); // default
		// is
		// overwrite
		// existing

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#getIndex()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ModelDescriptor> getIndex() throws IOException {
		
		
		fileContent =  (Result<File>) http.getIndex(baseUrl + "static/index.json");
		File file = fileContent.getContent();
		saveFile(file, "index.json");
		List<ModelDescriptor> tmp = new ArrayList<ModelDescriptor>();
		
		if (file.exists()) {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(file));

			tmp = gson.fromJson(reader, new TypeToken<List<ModelDescriptor>>() {
			}.getType());
		}

		return tmp;
	}

	private Result<File> downloadFile(String fileName, String Version) throws IOException {
		
		return downloadFile(baseUrl + "static/", fileName, Version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#download(kave.ModelDescriptor)
	 */
	@Override
	public Result<File> download(ModelDescriptor md) throws IOException {
		return downloadFile(md.getname(), md.getversion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#deleteFile(kave.ModelDescriptor)
	 */
	@Override
	public Result<String> delete(ModelDescriptor md) {
		return deleteFile(baseUrl + "delete/", md.getname(), md.getversion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kave.IClientAPI#saveFile
	 */
	@Override
	public boolean saveFile(ModelDescriptor md, File file) throws IOException {
		String fileName = md.getname() + "-" + md.getversion() + ".zip";
		return saveFile(file, fileName);
	}

}
