package kave;

import static kave.CollisionHandling.OVERWRITE;
import static kave.CollisionHandling.THROW_EXECPTION;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.remote.JsonException;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ClientAPITest {

	private String BaseUrl;
	private String fileName ;
	private File downDir;
	private IClientAPI test;
	private String Urltomcat;
	private String TestUploadObj;
	private static EmbeddedServer server;
	
	@Rule
	public TemporaryFolder root = new TemporaryFolder();
	@Rule
	public TemporaryFolder downloadDir = new TemporaryFolder();
	
	@BeforeClass
	public static void setupBeforeClass() throws InterruptedException, Exception{
		//server = new EmbeddedServer();
		//server.start();
		
	}
	
	@AfterClass
	public static void teardownAfterClass() throws Exception{
		//server.stop();
	}
	
	@Before
	public void setup() throws InterruptedException, Exception {
		//TestUploadObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		TestUploadObj = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		
		
		
		BaseUrl = "http://127.0.0.1:8080/";
		//downDir = new File("E:\\Github\\download\\");
		downDir = downloadDir.getRoot();
		//test = new ClientAPI(BaseUrl, downDir);
		Urltomcat = "ClientAPI-1/";
		
		
	}
	
	@Test
	public void uploadNewFileJetty() throws IOException, JSONException {
		
		PrepareClient(BaseUrl, downDir);
		UploadObject umd = prepareUploadObject();
		test.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		//assertNewIndex(umd.getModelDesc());
		/*PrepareClient(BaseUrl, downDir);
		File someFile = new File(TestUploadObj); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile, OVERWRITE);
		
		assertNewIndex(umd);*/
			
	}
	
	
	@Test
	public void getIndexTest() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		test.getIndex();
		File indexFile = new File(downDir+"\\index.json");
		assertTrue(indexFile.exists());
				
	}
	
	@Test
	public void uploadNewFileTomcat() throws IOException, JSONException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		UploadObject umd = prepareUploadObject();
		test.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		assertNewIndex(umd.getModelDesc());
			
	}
	
	@Test
	public void uploadNewFileJetty2() throws IOException, JSONException {
		
		//PrepareClient(BaseUrl, downDir);
		File someFile = new File(TestUploadObj); // get from somewhere
		fileName = someFile.getName().substring(0,someFile.getName().indexOf('.'));
		ModelDescriptor2 umd2 = new ModelDescriptor2(fileName, "1.0");
		ClientAPI test2 = new ClientAPI(BaseUrl, downDir);
		test2.upload2(umd2, someFile);
		
			
	}
	
	

	@Test(expected=IOException.class)
	public void uploadDuplicateFileTomcat() throws IOException, JSONException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		UploadObject umd = prepareUploadObject();
		test.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		UploadObject umd2 = prepareUploadObject();
		test.upload(umd2.getModelDesc(),getFileFor(umd2.getModelDesc()), THROW_EXECPTION);
		
			
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
		List<ModelDescriptor> index = test.getIndex();//make sure server has index
		//get initial server index:making sure client has an index
		//for comparison
		ModelDescriptor umd = md("1", "2");
		// make sure the file exists on server:move a new file in place on server
		File output = test.download(umd);
		test.saveFile(umd, output);
		assertFileExists(umd);
		
		List<ModelDescriptor> clientIndex = getClientIndex();
			
		assertCompareIndex(clientIndex);
			
	}
	
	@Test(expected=RuntimeException.class)
	public void downLoadNonExistingFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		List<ModelDescriptor> index = test.getIndex();
		ModelDescriptor umd = md("dummyFile", "doesNotExist2");
		
		File output = test.download(umd);
		test.saveFile(umd, output);
		
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	@Test
	public void deleteFile() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		ModelDescriptor umd = md("Upload", "1");
	    // make sure the file exists on server:move a new file in place on server & update server index
		test.delete(umd);
		List<ModelDescriptor> clientIndex = getClientIndex();
		assertDelete(clientIndex);
			
	}
	
	private void assertDelete(List<ModelDescriptor> clientIndex) throws JsonException, IOException {
		// TODO Auto-generated method stub
		List<ModelDescriptor> serverIndex = test.getIndex(); //after deletion
		assertNotEquals(serverIndex, clientIndex);
		
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

	
	private void PrepareClient(String URL, File downDir) throws IOException
	{
		test = new ClientAPI(URL, downDir);
	}
	
	private void assertNewIndex(ModelDescriptor... mds) throws IOException {
		List<ModelDescriptor> expecteds = Lists.newArrayList(mds);
		List<ModelDescriptor> actuals = test.getIndex();
		Assert.assertEquals(expecteds, actuals);
	}
	
	private UploadObject prepareUploadObject() throws IOException, JSONException
	{
		ModelDescriptor md = md("1", "2");
		getFileFor(md).createNewFile();
		assertFileExists(md);
		File file = getFileFor(md);
		byte[] fileBytes = fileContent(file);
		UploadObject ubm = new UploadObject(md, fileBytes);
		return ubm;
	}
	
	
	private void assertFileExists(ModelDescriptor md) {
		assertTrue(getFileFor(md).exists());
	}
	private File getFileFor(ModelDescriptor md) {
		return new File(downloadDir.getRoot() + "\\" + md.getname() + "-" + md.getversion() + ".zip");
	}
	
	private ModelDescriptor md(String name, String version) {
		return new ModelDescriptor("n" + name, "v" + version);
	}

	private byte[] fileContent(File file) throws IOException, JSONException {

		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		// convert file into array of bytes
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		return bFile;
	}


}
