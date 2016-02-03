package kave;

import static kave.CollisionHandling.OVERWRITE;
import static kave.CollisionHandling.THROW_EXECPTION;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import org.openqa.selenium.remote.JsonException;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ClientAPITest {

	private String BaseUrl;
	private String fileName ;
	private File downDir;
	private IClientAPI actualClient;
	private String Urltomcat;
	private String TestUploadObj;
	private Result<String> uploadSuccessful;
	private Result<String> deleteSuccessful;
	private Result<File> fileContent;
	private Result<File> fakeIndex;
	
	
	@Rule
	public TemporaryFolder root = new TemporaryFolder();
	@Rule
	public TemporaryFolder tmpF = new TemporaryFolder();
	@Rule
	public TemporaryFolder downloadDir = new TemporaryFolder();
	@Mock
	private IClientAPI mockedClient;
	@Mock
	private IHttpUtils http;
	
	

	
	@Before
	public void setup() throws InterruptedException, Exception {
		
		TestUploadObj = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		//http = mock(HttpUtils.class);
		initMocks(this);
		
		uploadSuccessful = new Result<String>();
		deleteSuccessful = new Result<String>();
		fileContent = new Result<File>();
		fakeIndex = new Result<File>();
		setupResults();
		SetupFileContent(md("1", "2"));
		createFakeIndex();
		
		//when(mockedClient.upload(any(ModelDescriptor.class), any(File.class),eq(OVERWRITE))).thenReturn(uploadSuccessful);
		
		when(http.upload(anyString(), anyString())).thenReturn(uploadSuccessful);
		when(http.delete(anyString(), anyString())).thenReturn(deleteSuccessful);
		when(http.download(anyString())).thenReturn(fileContent);
		when(http.getIndex(anyString())).thenReturn(fakeIndex);
		//when(mockedClient.upload(any(ModelDescriptor.class), any(File.class),eq(THROW_EXECPTION))).thenThrow(new IOException());
		//when(mockedClient.delete(any(ModelDescriptor.class))).thenReturn(deleteSuccessful);
		//when(mockedClient.delete(md("dummy", "2"))).thenThrow(new RuntimeException());
		
		BaseUrl = "http://127.0.0.1:8080/";
		//downDir = new File("F:\\Github\\download\\");
		downDir = downloadDir.getRoot();
		
		Urltomcat = "ClientAPI-1/";
		
		
	}
	
	private void createFakeIndex() throws IOException {
		// TODO Auto-generated method stub
		
		List<ModelDescriptor> index = new ArrayList<ModelDescriptor>();
		ModelDescriptor md1 = md("1","2");
		ModelDescriptor md2 = md("3","4");
		ModelDescriptor md3 = md("5","6");
		index.add(md1);
		index.add(md2);
		index.add(md3);
		Gson gson = new Gson();
		String json = gson.toJson(index);
		File tmp = tmpF.newFile("index.json");
		try {
			FileWriter writer = new FileWriter(tmp.getAbsolutePath());
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		fakeIndex.setContent(tmp);
		fakeIndex.setOk(true);
		
	}

	private void setupResults() {
		// TODO Auto-generated method stub
		uploadSuccessful.setOk(true);
		uploadSuccessful.setErrorMessage("Success!");
		
		deleteSuccessful.setOk(true);
		deleteSuccessful.setErrorMessage("deleteSuccessful!");
		
		
	}
	
	private void SetupFileContent(ModelDescriptor downloadDesc) throws IOException
	{
		File file = getFileFor(downloadDesc);
		file.createNewFile();
		fileContent.setContent(file);
		fileContent.setOk(true);
		
		
	}

	@Test
	public void uploadNewFileJetty() throws IOException, JSONException {
		
		PrepareClient(BaseUrl, downDir);
		UploadObject umd = prepareUploadObject();
		Result<String> result=actualClient.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		assertNewIndex(result);
		
				
	}
	
	
	@Test
	public void getIndexTest() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		//PrepareClient(BaseUrl, downDir);
		actualClient.getIndex();
		File indexFile = new File(downDir+"\\index.json");
		assertTrue(indexFile.exists());
				
	}
	
	@Test
	public void uploadNewFileTomcat() throws IOException, JSONException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		UploadObject umd = prepareUploadObject();
		Result<String> result = actualClient.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		//Result<String> result = mockedClient.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		assertNewIndex(result);
			
	}
	
	
	
	

	@Test(expected=IOException.class)
	public void uploadDuplicateFile() throws IOException, JSONException {
		
		PrepareClient(BaseUrl, downDir);
		//PrepareClient(BaseUrl+Urltomcat, downDir);
		UploadObject umd = prepareUploadObject();
		//actualClient.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		mockedClient.upload(umd.getModelDesc(),getFileFor(umd.getModelDesc()), OVERWRITE);
		UploadObject umd2 = prepareUploadObject();
		//actualClient.upload(umd2.getModelDesc(),getFileFor(umd2.getModelDesc()), THROW_EXECPTION);
		mockedClient.upload(umd2.getModelDesc(),getFileFor(umd2.getModelDesc()), THROW_EXECPTION);
			
	}
	@Test
	public void downLoadAllFilesTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		//PrepareClient(BaseUrl, downDir);
		List<ModelDescriptor> index = actualClient.getIndex();
		for (ModelDescriptor md : index) {
			SetupFileContent(md);
			Result<File> output = actualClient.download(md);
			actualClient.saveFile(md, output.getContent());
		}
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	@Test
	public void downLoadSingleFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		List<ModelDescriptor> index = actualClient.getIndex();//make sure server has index
		//get initial server index:making sure client has an index
		//for comparison
		ModelDescriptor umd = md("1", "2");
		// make sure the file exists on server:move a new file in place on server
		Result<File> output = actualClient.download(umd);
		actualClient.saveFile(umd, output.getContent());
		assertFileExists(umd);
		
		List<ModelDescriptor> clientIndex = getClientIndex();
			
		assertCompareIndex(clientIndex);
			
	}
	
	@Test(expected=RuntimeException.class)
	public void downLoadNonExistingFileTomcat() throws IOException {
		
		PrepareClient(BaseUrl+Urltomcat, downDir);
		List<ModelDescriptor> index = actualClient.getIndex();
		ModelDescriptor umd = md("dummyFile", "doesNotExist2");
		
		Result<File> output = actualClient.download(umd);
		actualClient.saveFile(umd, output.getContent());
		
		List<ModelDescriptor> actualIndex = getClientIndex();
			
		assertCompareIndex(actualIndex);
			
	}
	
	@Test
	public void deleteFile() throws IOException {
		
		//PrepareClient(BaseUrl+Urltomcat, downDir);
		PrepareClient(BaseUrl, downDir);
		ModelDescriptor umd = md("1", "2");
	    // make sure the file exists on server:move a new file in place on server & update server index
		//Result<String> response = actualClient.delete(umd);
		Result<String> response = mockedClient.delete(umd);
		assertDelete(response);
			
	}
	
	@Test(expected=RuntimeException.class)
	public void deleteNonExistingFile() throws IOException {
		
		//PrepareClient(BaseUrl+Urltomcat, downDir);
		PrepareClient(BaseUrl, downDir);
		ModelDescriptor umd = md("dummy", "2"); // file should not exist on server
	   // Result<String> response = actualClient.delete(umd);
		 Result<String> response = mockedClient.delete(umd);
	
			
	}
	
	

	private void assertDelete(Result<String> response) throws JsonException, IOException {
		// TODO Auto-generated method stub
		assertTrue(response.isOk());
		assertEquals(response.getErrorMessage(), "deleteSuccessful!");
		
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
		List<ModelDescriptor> eIndex = actualClient.getIndex();
		Assert.assertEquals(eIndex, actualIndex);
		
	}

	
	private void PrepareClient(String URL, File downDir) throws IOException
	{
		actualClient = new ClientAPI(URL, downDir);
	}
	
	private void assertNewIndex(Result<String> result) throws IOException {
		Assert.assertTrue(result.isOk());
		Assert.assertEquals("Success!", result.getErrorMessage());
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
		return new File(downDir.getAbsoluteFile() + "\\" + md.getname() + "-" + md.getversion() + ".zip");
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
