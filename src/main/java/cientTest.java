import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
			String Version = "2.0";
			FileInputStream fileInputStream=null;
			//CLient
			Client client = Client.create();
			//WebResource webResource = client.resource(UploadURl);
			WebResource webResource = client.resource(DeleteURl);
			
			//IO
			File file = new File(TestUploadObj);
			fileName = file.getName();
			fileName = fileName.substring(0,fileName.indexOf('.'));
			byte[] bFile = new byte[(int) file.length()];
			  //convert file into array of bytes
		    fileInputStream = new FileInputStream(file);
		    fileInputStream.read(bFile);
		    fileInputStream.close();
		    modelDesc = new ModelDescriptor(fileName, Version);
		    testObject = new UploadObject(modelDesc,bFile);
		    /*String jsonString = serializer.SerializeUploadObj(testObject);
		    		    
		    //client
		    ClientResponse response = webResource.type("application/json")
		 		   .post(ClientResponse.class, jsonString);
		    if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}*/
		    
		    String jsonString2 = serializer.SerializeModelDesc(modelDesc);
		    
		    //client
		    ClientResponse response = webResource.type("application/json").delete(ClientResponse.class, jsonString2);
		    if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		       

			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
			
			
		}
		catch (Exception e) {

			e.printStackTrace();

		  }
	}
	
}
