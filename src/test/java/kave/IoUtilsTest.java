package kave;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class IoUtilsTest {
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public TemporaryFolder root = new TemporaryFolder();

	private IoUtils sut;
	private File dataDir;
	private File indexFile;
	private String fileObj,fileObj2,fileObj3;

	@Before
	public void setup() {

		/*dataDir = new File("E:\\Github\\Upload");
		dataDir.mkdir();
		indexFile = new File(dataDir +"\\"+ "index.json");
		sut = new IoUtils(tmp.getRoot(), dataDir, "E:\\Github\\Upload");*/
		
		indexFile = new File(root.getRoot() +"\\"+ "index.json");
		sut = new IoUtils(tmp.getRoot(),root.getRoot(),root.getRoot().getPath());
		dataDir = root.getRoot();
		
		
		fileObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		fileObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		fileObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
	}

	
	@Test
	public void removeSomeExistingModelDesc() throws IOException {
		prepareIndex(md("1", "1"), md("2", "1"));
		sut.removeIndex(md("2", "1"));
		assertIndex(md("1", "1"));
	}

	@Test
	public void removeFinalModelDesc() throws IOException {
		prepareIndex(md("2", "1"));
		sut.removeIndex(md("2", "1"));
		assertIndex();
	}
	@Test
	public void removeNonExistingModelDesc() throws IOException {
		prepareIndex(md("1", "1"), md("2", "1"));
		assertFalse(sut.removeIndex(md("3", "1")));
	}
	
	@Test
	public void addDuplicateToIndex() throws IOException, JSONException {
		prepareIndex(md("1", "1"));
		sut.addIndex(md("1", "1"));
		assertIndex(md("1", "1"));
	}

	@Test
	public void addingSomethingToIndex() throws IOException, JSONException {
		prepareIndex(md("1", "1"));
		sut.addIndex(md("3", "1"));
		assertIndex(md("1", "1"), md("3", "1"));
	}
	
	@Test
	public void uploadNewFileTest() throws IOException, JSONException {
		
		// IO
		File file = new File(fileObj);
		String newName = file.getName();
		String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor uds = md(newName,Version);
		byte[] fileBytes = fileContent(file);
		sut.addFile(uds, fileBytes);
		assertUpload(uds,fileBytes);
	}
	
	@Test
	public void updateExistingFileTest() throws IOException, JSONException, InterruptedException {
		
		// IO
		File file = new File(fileObj);
		String newName = file.getName();
		String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor uds = md(newName,Version);
		byte[] fileBytes = fileContent(file);
		sut.addFile(uds, fileBytes);
		File uploadedfile = getFileHandle(uds,dataDir.getAbsolutePath());
		long t1 = uploadedfile.lastModified();
		TimeUnit.SECONDS.sleep(1);//for inspecting visually that files have been overwritten
		sut.addFile(uds, fileBytes);
		File updatedFile = getFileHandle(uds,dataDir.getAbsolutePath());
		long t2 = updatedFile.lastModified();
		assertDuplicate(t1,t2,uds);
		
	}
	
	@Test
	public void addtmpFileTest() throws IOException, JSONException {
		File file = new File(fileObj);
		String newName = file.getName();
		String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor uds = md(newName,Version);
		byte[] fileBytes = fileContent(file);
		sut.tmpadd(uds, fileBytes);
		assertTrue(assertTmp(uds));
		
	}

	@Test
	public void removeExistingFileTest() throws IOException, JSONException {
		// IO
		File file = new File(fileObj);
	    String newName = file.getName();
	    String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor delFileDescriptor = md(newName,Version);
		byte[] fileBytes = fileContent(file);
		sut.addFile(delFileDescriptor, fileBytes);
		sut.removeFile(delFileDescriptor);
		assertDelete(delFileDescriptor);
	}
	
	@Test
	public void removeNonExistingFileTest() throws IOException, JSONException {
		// IO
		File file = new File(fileObj);
		String newName = file.getName();
	    String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor uds = md(newName,Version);
		byte[] fileBytes = fileContent(file);
		sut.addFile(uds, fileBytes);
		ModelDescriptor delFileDescriptor = md("3", "1");
		assertDeleteNonExisting(delFileDescriptor);
		
		
	}


	
	private void assertDeleteNonExisting(ModelDescriptor delFileDescriptor) throws JSONException, IOException {
		// TODO Auto-generated method stub
		assertFalse(sut.removeFile(delFileDescriptor));//remove function should return false for non existing file
		assertFalse(sut.removeIndex(delFileDescriptor));// should return false for non existing index
	}


	private void assertDelete(ModelDescriptor uds) throws IOException {
		// TODO Auto-generated method stub
		File rFileHandle = getFileHandle(uds,dataDir.getAbsolutePath());
		assertIndex();// filedescriptor should be deleted
		assertFalse(rFileHandle.exists()); // fileshould be deleted
		
	}


	private void assertDuplicate(long origional, long updated, ModelDescriptor md) throws IOException
	{
		assertIndex(md); // only one model descriptor should exist
		assertNotEquals(origional,updated);
	}

	private void prepareIndex(ModelDescriptor... mds) throws IOException {
		List<ModelDescriptor> expecteds = Lists.newArrayList(mds);
		String json = new Gson().toJson(expecteds);
		Files.write(json, indexFile, Charset.defaultCharset());
	}

	private void assertIndex(ModelDescriptor... mds) throws IOException {
		List<ModelDescriptor> expecteds = Lists.newArrayList(mds);
		List<ModelDescriptor> actuals = readActualIndex();
		Assert.assertEquals(expecteds, actuals);
	}
	
	private void assertUpload(ModelDescriptor uploadDesc, byte[] fileBytes) throws IOException {
		// TODO Auto-generated method stub
		assertIndex(uploadDesc);// check correct index is created
		File uploadedFile = getFileHandle(uploadDesc, dataDir.getAbsolutePath());
		assertFile(uploadedFile,fileBytes);//check file contents are not changed
		assertFalse(assertTmp(uploadDesc)); // check that tmp file is deleted
		
	}
	private boolean assertTmp(ModelDescriptor uploadDesc) {
		// TODO Auto-generated method stub
		//to check file in tmp folder
		File tmpFile = getFileHandle(uploadDesc,tmp.getRoot().getAbsolutePath());
		return tmpFile.exists();
	}


	private void assertFile(File file, byte[] expectedContent) throws IOException {
		// TODO Auto-generated method stub
		if(file.exists())
		{
			byte [] actualContent = getFileContent(file.getAbsolutePath());
			assertArrayEquals(expectedContent, actualContent);
		}
		
		
	}

	private List<ModelDescriptor> readActualIndex() throws IOException {
		String json = FileUtils.readFileToString(indexFile);
		List<ModelDescriptor> mds = new Gson().fromJson(json, new TypeToken<List<ModelDescriptor>>() {
		}.getType());
		return mds;
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

	



	private File getFileHandle(ModelDescriptor md, String directoryPath)
	{
		File fHandle = new File(directoryPath +"\\"+md.getname()+ "-" + md.getversion() + ".zip");
		return fHandle;
	}
	

	


	
	
	public byte[] getFileContent(String fileLocation) throws IOException {
		FileInputStream fileInputStream = null;
		// IO
		File file = new File(fileLocation);
		byte[] bFile = new byte[(int) file.length()];
		// convert file into array of bytes
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		return bFile;
	}

}
