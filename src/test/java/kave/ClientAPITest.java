package kave;

import static kave.CollisionHandling.OVERWRITE;
import static kave.CollisionHandling.THROW_EXECPTION;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.remote.JsonException;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ClientAPITest {
	@Rule
	public TemporaryFolder downloadDir = new TemporaryFolder();
	private String BaseUrl;
	private String BaseUrltomcat;
	private String TestUploadObj;
	private String TestUploadObj2;
	private String TestUploadObj3;
	private String TestUploadObj4;
	private String fileName ;
	private String Version; 
	private File downDir;
	private IClientAPI test;
	private String Urltomcat;
	
	@Before
	public void setup() throws IOException {
		TestUploadObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		TestUploadObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		
		
		Version = "1.0";
		BaseUrl = "http://127.0.0.1:8080/";
		//downDir = new File("E:\\Github\\download\\");
		downDir = downloadDir.getRoot();
		//test = new ClientAPI(BaseUrl, downDir);
		Urltomcat = "ClientAPI-1/";
		
	}
	
	@Test
	public void uploadNewFileJetty() throws IOException {
		
		PrepareClient(BaseUrl, downDir);
		File someFile = new File(TestUploadObj2); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile, OVERWRITE);
		
		assertNewIndex(umd);
			
	}
	
	
	@Test
	public void getIndexTest() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		test.getIndex();
		File indexFile = new File(downDir+"\\index.json");
		assertTrue(indexFile.exists());
				
	}
	
	@Test
	public void uploadNewFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		File someFile = new File(TestUploadObj2); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile, OVERWRITE);
		assertNewIndex(umd);
			
	}
	
	@Test(expected=IOException.class)
	public void uploadDuplicateFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		File someFile = new File(TestUploadObj2); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile, OVERWRITE);
		test.upload(umd, someFile,THROW_EXECPTION);
		
			
	}
	@Test
	public void downLoadAllFilesTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		List<ModelDescriptor> index = test.getIndex();
		for (ModelDescriptor md : index) {
			File output = test.download(md);
			test.saveFile(md, output);
		}
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	@Test
	public void downLoadSingleFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		File someFile = new File(TestUploadObj2); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		fileName = "Unique"+fileName;
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile,THROW_EXECPTION);
		File output = test.download(umd);
		test.saveFile(umd, output);
		
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	@Test(expected=RuntimeException.class)
	public void downLoadNonExistingFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		List<ModelDescriptor> index = test.getIndex();
		fileName = "DummyFile";
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		
		File output = test.download(umd);
		test.saveFile(umd, output);
		
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	private List<ModelDescriptor> getClientIndex() throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<ModelDescriptor> Index = new ArrayList<ModelDescriptor>();
		File indexFile = new File (downDir.getAbsolutePath()+"\\index.json");
		if(indexFile.exists())
		{
			Gson gson = new Gson(); 
			JsonReader reader = new JsonReader(new FileReader(indexFile.getAbsolutePath()));

			Index = gson.fromJson(reader, new TypeToken<List<ModelDescriptor>>(){}.getType());

		}
		
		return Index ;
	}

	private void assertCompareIndex(List<ModelDescriptor> actualIndex) throws JsonException, IOException {
		// TODO Auto-generated method stub
		List<ModelDescriptor> eIndex = test.getIndex();
		Assert.assertEquals(eIndex, actualIndex);
		
	}

	
	
	private void dummyUpload() throws IOException
	{
		test = new ClientAPI(BaseUrl+Urltomcat, downDir);
		File someFile = new File(TestUploadObj); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		//test.upload(umd, someFile, OVERWRITE);
	}
	
	private void PrepareClient(String URL, File downDir) throws IOException
	{
		test = new ClientAPI(URL, downDir);
	}
	
	private void assertNewIndex(ModelDescriptor... mds) throws IOException {
		List<ModelDescriptor> expecteds = Lists.newArrayList(mds);
		List<ModelDescriptor> actuals = test.getIndex();
		Assert.assertEquals(expecteds, actuals);
	}

}
