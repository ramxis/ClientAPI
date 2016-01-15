package kave;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientAPI {
	private final File downloadFolder;
	public ClientAPI(File downloadFolder)
			throws IOException {
		this.downloadFolder = downloadFolder;
		
		enforceFolders();
	}
	private void enforceFolders() throws IOException {
		if (!downloadFolder.exists()) {
			// File CustomFolder = new File("E:\\Github\\Upload\\");
			FileUtils.forceMkdir(downloadFolder);
		}
	}
	
	public boolean deleteFile(String deleteURI, String fileName, String Version)
	{
		SerializeModel	serializer = new SerializeModel();
		Client client = Client.create();
		WebResource webResource = client.resource(deleteURI);
		ModelDescriptor modelDesc = new ModelDescriptor(fileName, Version);
		String jsonString = serializer.SerializeModelDesc(modelDesc);
		//client
		 ClientResponse response = webResource.type("application/json").delete(ClientResponse.class, jsonString);
	    if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}
	       

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);
		return true;
	}
	public boolean saveFile(File file, String filename) throws IOException
	{
		if (downloadFolder.exists())
		{
			File fileSaved = new File(downloadFolder.getAbsolutePath()+"\\"+filename);
			FileUtils.copyFile(file, fileSaved);
			return true;
		}
		return false;
	}
	
	public File downloadFile(String downloadURI, String fileName, String Version, boolean index) throws IOException
	{
		
		Client client = Client.create();
		String downloadFile = fileName;
		ClientResponse response;
		if(index)
		{
			downloadFile = downloadFile + ".json";
			WebResource webResource = client.resource(downloadURI+downloadFile);
			
			  response = webResource.type("application/json").get(ClientResponse.class);
		    if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		}
		else
		{
			downloadFile = downloadFile+ "-" + Version + ".zip";
			WebResource webResource = client.resource(downloadURI+downloadFile);
			
			 response = webResource.type("application/zip").get(ClientResponse.class);
		    if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		       
		}
		

		
		 File file = response.getEntity(File.class);
		 saveFile(file,downloadFile);
		 return file;
	}
	
	public boolean  UploadFile(String uploadURI, String fileObj, String newName, String Version) throws IOException
	{
		if(newName != null)
		{
			UploadObject testObject;
			SerializeModel	serializer = new SerializeModel();
			Client client = Client.create();
			WebResource webResource = client.resource(uploadURI);
			FileInputStream fileInputStream=null;
			//IO
			File file = new File(fileObj);
			byte[] bFile = new byte[(int) file.length()];
			  //convert file into array of bytes
		    fileInputStream = new FileInputStream(file);
		    fileInputStream.read(bFile);
		    fileInputStream.close();
		    ModelDescriptor modelDesc = new ModelDescriptor(newName, Version);
		    testObject = new UploadObject(modelDesc,bFile);
		    String jsonString = serializer.SerializeUploadObj(testObject);
		    ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonString);
			    if (response.getStatus() != 200) 
			    {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}
			    System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);
			return true;
		}
		else
		{
			UploadObject testObject;
			SerializeModel	serializer = new SerializeModel();
			Client client = Client.create();
			WebResource webResource = client.resource(uploadURI);
			FileInputStream fileInputStream=null;
			//IO
			File file = new File(fileObj);
			newName = file.getName();
			newName = newName.substring(0,newName.indexOf('.'));
			byte[] bFile = new byte[(int) file.length()];
			  //convert file into array of bytes
		    fileInputStream = new FileInputStream(file);
		    fileInputStream.read(bFile);
		    fileInputStream.close();
		    ModelDescriptor modelDesc = new ModelDescriptor(newName, Version);
		    testObject = new UploadObject(modelDesc,bFile);
		    String jsonString = serializer.SerializeUploadObj(testObject);
		    ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonString);
			    if (response.getStatus() != 200) 
			    {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}
			    System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);
			return true;
		}
		
	}
}
