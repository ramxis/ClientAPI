import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import kave.DeserializeModel;
import kave.ModelDescriptor;
import kave.UploadObject;
import kave.SerializeModel;
public class cientTest {
	public static void main(String[] args) {
		try {
			ModelDescriptor modelDesc;
			UploadObject testObject;
			SerializeModel	serializer = new SerializeModel();
						
			String UploadURl = "http://127.0.0.1:8080/upload";
			String DeleteURl = "http://127.0.0.1:8080/delete";
			String TestUploadObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
			String TestUploadObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
			String TestUploadObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
			String fileName = "testupload";
			String Version = "beta";
			FileInputStream fileInputStream=null;
			
			/*UploadFile(UploadURl,TestUploadObj,null,"beta");
			UploadFile(UploadURl,TestUploadObj,null,"1.0");
			UploadFile(UploadURl,TestUploadObj,fileName,"beta");
			UploadFile(UploadURl,TestUploadObj,fileName,"1.0");*/
			
			deleteFile(DeleteURl,"Upload","beta");
			/*deleteFile(DeleteURl,"Upload","1.0");
			deleteFile(DeleteURl,fileName,"beta");
			deleteFile(DeleteURl,fileName,"1.0");*/
		    
		    
			
			
		}
		catch (Exception e) {

			e.printStackTrace();

		  }
	}
	 //for testing only
	static boolean  UploadFile(String uploadURI, String fileObj, String newName, String Version) throws IOException
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
	
	boolean UploadFile(String uploadURI, ModelDescriptor fileDescriptor, File file) throws IOException
	{
		UploadObject testObject;
		SerializeModel	serializer = new SerializeModel();
		Client client = Client.create();
		WebResource webResource = client.resource(uploadURI);
		FileInputStream fileInputStream=null;
		byte[] bFile = new byte[(int) file.length()];
		  //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    testObject = new UploadObject(fileDescriptor,bFile);
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
	
	static boolean deleteFile(String deleteURI, String fileName, String Version)
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
	
}
