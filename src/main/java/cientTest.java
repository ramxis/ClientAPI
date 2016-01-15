import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import kave.ClientAPI;
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
			String DownloadURl = "http://127.0.0.1:8080/static/";
			String TestUploadObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
			String TestUploadObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
			String TestUploadObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
			String fileName = "testupload";
			String Version = "beta";
			FileInputStream fileInputStream=null;
			File downDir = new File("E:\\Github\\download\\");
			ClientAPI test = new ClientAPI(downDir);
			
			
			
			test.downloadFile(DownloadURl, "index", null, true);
			test.downloadFile(DownloadURl, fileName, Version, false);
			//test.UploadFile(UploadURl,TestUploadObj,null,"beta");
			/*test.UploadFile(UploadURl,TestUploadObj,null,"beta");
			test.UploadFile(UploadURl,TestUploadObj,null,"1.0");
			test.UploadFile(UploadURl,TestUploadObj,fileName,"beta");
			test.UploadFile(UploadURl,TestUploadObj,fileName,"1.0");*/
			
			/*test.deleteFile(DeleteURl,"Upload","beta");
			test.deleteFile(DeleteURl,"Upload","beta");
			test.deleteFile(DeleteURl,"Upload","1.0");
			test.deleteFile(DeleteURl,fileName,"beta");
			test.deleteFile(DeleteURl,fileName,"1.0");*/
		    
		    
			
			
		}
		catch (Exception e) {

			e.printStackTrace();

		  }
	}
	
	
}
