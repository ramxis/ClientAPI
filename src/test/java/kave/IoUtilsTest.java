package kave;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IoUtilsTest {
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public TemporaryFolder root = new TemporaryFolder();


	private IoUtils sut;
	private File dataDir;
	@Before
	public void setup() {

		dataDir = new File("E:\\Github\\Upload");
		//sut = new IoUtils(tmp.getRoot(),root.getRoot(),root.getRoot().getPath());
		sut = new IoUtils(tmp.getRoot(),dataDir,"E:\\Github\\Upload");
	}
	@Test
	public void removeIndexTest() throws IOException {
		ModelDescriptor modelDesc ;
		modelDesc = new ModelDescriptor("Upload", "beta");
		assertTrue("Index does not exits in the index File", sut.removeIndex(modelDesc));
		modelDesc = new ModelDescriptor("Upload", "1.0");
		assertTrue("Index does not exits in the index File", sut.removeIndex(modelDesc));//remove existing index

		modelDesc = new ModelDescriptor("falsetestupload", "1.0");//try and remove index which does not exit
		assertTrue("Index does not exits in the index File", sut.removeIndex(modelDesc));
		modelDesc = new ModelDescriptor("testupload", "falseversion");
		assertTrue("Index does not exits in the index File", sut.removeIndex(modelDesc));
		modelDesc = new ModelDescriptor("falsetestupload", "falseversion");
		assertTrue("Index does not exits in the index File", sut.removeIndex(modelDesc));

	}
	
	@Test
	public void addIndex() throws IOException, JSONException {
		ModelDescriptor modelDesc ;
		modelDesc = new ModelDescriptor("Upload", "beta");//check duplicate index addition
		assertTrue("index File does not exits", sut.addIndex(modelDesc));
		

	}
	@Test
	public void chkDuplicateIndex() throws IOException, JSONException {
		addIndex();
		ModelDescriptor modelDesc ;
		modelDesc = new ModelDescriptor("Upload", "beta");//check duplicate index addition
		assertTrue("index File does not exits", sut.addIndex(modelDesc));
		

	}
	@Test
	public void addUniqueIndex() throws IOException, JSONException {
		ModelDescriptor modelDesc ;
		modelDesc = new ModelDescriptor("Upload", "1.0");//check unique index addition
		assertTrue("index File does not exits ", sut.addIndex(modelDesc));
		modelDesc = new ModelDescriptor("testupload", "1.0");
		assertTrue("index File does not exits ", sut.addIndex(modelDesc));
		

	}
	@Test
	public void addnewFileTest() throws IOException, JSONException
	{
		String fileObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		String fileObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		String fileObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
		FileInputStream fileInputStream=null;
		//IO
		File file = new File(fileObj);
		//File file = new File(fileObj2);
		//File file = new File(fileObj3);
		String newName = file.getName();
		String Version = "beta";
		newName = newName.substring(0,newName.indexOf('.'));
		byte[] bFile = new byte[(int) file.length()];
		  //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    ModelDescriptor modelDesc = new ModelDescriptor(newName, Version);
	    assertTrue(sut.addFile(modelDesc, bFile));
	}
	@Test
	public void updateExistingFileTest() throws IOException, JSONException
	{
		addnewFileTest();
		String fileObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		String fileObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		String fileObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
		FileInputStream fileInputStream=null;
		//IO
		File file = new File(fileObj);
		//File file = new File(fileObj2);
		//File file = new File(fileObj3);
		String newName = file.getName();
		String Version = "beta";
		newName = newName.substring(0,newName.indexOf('.'));
		byte[] bFile = new byte[(int) file.length()];
		  //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    ModelDescriptor modelDesc = new ModelDescriptor(newName, Version);
	    assertTrue(sut.addFile(modelDesc, bFile));
	}
	
	@Test
	public void addtmpFileTest() throws IOException, JSONException
	{
		String fileObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		String fileObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		String fileObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
		FileInputStream fileInputStream=null;
		//IO
		File file = new File(fileObj);
		//File file = new File(fileObj2);
		//File file = new File(fileObj3);
		String newName = file.getName();
		String Version = "beta";
		newName = newName.substring(0,newName.indexOf('.'));
		byte[] bFile = new byte[(int) file.length()];
		  //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    ModelDescriptor modelDesc = new ModelDescriptor(newName, Version);
	    File chkfile = sut.tmpadd(modelDesc, bFile);
	    assertTrue(chkfile.exists());
	}
	
	@Test
	public void removeFileTest() throws IOException, JSONException {
		ModelDescriptor modelDesc ;
		modelDesc = new ModelDescriptor("Upload", "beta");
		assertTrue(sut.removeFile(modelDesc));
		modelDesc = new ModelDescriptor("Upload", "1.0");
		assertTrue(sut.removeFile(modelDesc));
		modelDesc = new ModelDescriptor("testupload", "1.0");
		assertTrue(sut.removeFile(modelDesc));
	}
	
	@Test
	public void FileContentsAreNotChanged() throws IOException, JSONException {
		String sourcefile = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		addnewFileTest();//make sure the file is uploaded;
		String fileDescriptor = "Upload-beta.zip";
		String uploadFile = dataDir.getPath() +"\\"+fileDescriptor;
		File file = new File(uploadFile);
		assertTrue("File does not exist in data folder!",file.exists());
		
		byte[] sourceFileContent = getFileContent(sourcefile);
		byte[] uploadFileContent = getFileContent(uploadFile);
		assertArrayEquals(sourceFileContent, uploadFileContent);
		
		
	}

	public byte[] getFileContent(String fileLocation) throws IOException
	{
		FileInputStream fileInputStream=null;
		//IO
		File file = new File(fileLocation);
		byte[] bFile = new byte[(int) file.length()];
		  //convert file into array of bytes
	    fileInputStream = new FileInputStream(file);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    return bFile;
	}
	
}
