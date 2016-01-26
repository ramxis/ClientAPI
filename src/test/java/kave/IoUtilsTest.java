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
	

	@Before
	public void setup() throws JSONException, IOException {

		/*
		 * dataDir = new File("E:\\Github\\Upload"); dataDir.mkdir(); indexFile
		 * = new File(dataDir +"\\"+ "index.json"); sut = new
		 * IoUtils(tmp.getRoot(), dataDir, "E:\\Github\\Upload");
		 */

		indexFile = new File(root.getRoot() + "\\" + "index.json");
		sut = new IoUtils(tmp.getRoot(), root.getRoot(), root.getRoot().getPath());
		dataDir = root.getRoot();

		

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
		ModelDescriptor md = md("1", "2");
		prepareIndex(md);
		getFileFor(md).createNewFile();
		assertFileExists(md);
		File file = getFileFor(md);
		byte[] fileBytes = fileContent(file);
		sut.addFile(md, fileBytes);
		assertUpload(md, fileBytes);
	}

	@Test
	public void updateExistingFileTest() throws IOException, JSONException, InterruptedException {

		// IO
		ModelDescriptor md = md("1", "2");
		prepareIndex(md);
		getFileFor(md).createNewFile();
		assertFileExists(md);
		File uploadedfile = getFileFor(md);
		byte[] fileBytes = fileContent(uploadedfile);
		sut.addFile(md, fileBytes);
		long t1 = uploadedfile.lastModified();
		TimeUnit.SECONDS.sleep(1);// for inspecting visually that files have
									// been overwritten
		sut.addFile(md, fileBytes);
		File updatedFile = getFileFor(md);
		long t2 = updatedFile.lastModified();
		assertDuplicate(t1, t2, md);

	}

	@Test
	public void addtmpFileTest() throws IOException, JSONException {
		
		ModelDescriptor md = md("1", "2");
		prepareIndex(md);
		getFileFor(md).createNewFile();
		assertFileExists(md);
		File file = getFileFor(md);
		byte[] fileBytes = fileContent(file);
		sut.tmpAdd(md, fileBytes);
		assertTrue(assertTmp(md));

	}

	@Test
	public void removeExistingFileTest() throws IOException, JSONException {
		ModelDescriptor md = md("1", "2");
		prepareIndex(md);
		getFileFor(md).createNewFile();

		assertFileExists(md);
		sut.removeFile(md);
		assertDelete(md);
	}

	private File getFileFor(ModelDescriptor md) {
		return new File(root.getRoot() + "\\" + md.getname() + "-" + md.getversion() + ".zip");
	}

	private void assertFileExists(ModelDescriptor md) {
		assertTrue(getFileFor(md).exists());
	}

	@Test
	public void removeNonExistingFileTest() throws IOException, JSONException {
		// IO
		/*File file = new File(fileObj);
		String newName = file.getName();
		String Version = "1";
		newName = newName.substring(0, newName.indexOf('.'));
		ModelDescriptor uds = md(newName, Version);
		byte[] fileBytes = fileContent(file);
		sut.addFile(uds, fileBytes);*/
		ModelDescriptor delFileDescriptor = md("3", "1");
		assertDeleteNonExisting(delFileDescriptor);

	}

	private void assertDeleteNonExisting(ModelDescriptor delFileDescriptor) throws JSONException, IOException {
		// TODO Auto-generated method stub
		assertFalse(sut.removeFile(delFileDescriptor));// remove function should
														// return false for non
														// existing file
		assertFalse(sut.removeIndex(delFileDescriptor));// should return false
														// for non existing
														// index
	}

	private void assertDelete(ModelDescriptor uds) throws IOException {
		File rFileHandle = getFileHandle(uds, dataDir.getAbsolutePath());
		assertFalse(readActualIndex().contains(uds));
		assertFalse(rFileHandle.exists()); // fileshould be deleted
	}

	private void assertDuplicate(long origional, long updated, ModelDescriptor md) throws IOException {
		assertIndex(md); // only one model descriptor should exist
		assertNotEquals(origional, updated); // modified times should be different
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
		assertFile(uploadedFile, fileBytes);// check file contents are not
											// changed
		assertFalse(assertTmp(uploadDesc)); // check that tmp file is deleted

	}

	private boolean assertTmp(ModelDescriptor uploadDesc) {
		// TODO Auto-generated method stub
		// to check file in tmp folder
		File tmpFile = getFileHandle(uploadDesc, tmp.getRoot().getAbsolutePath());
		return tmpFile.exists();
	}

	private void assertFile(File file, byte[] expectedContent) throws IOException {
		// TODO Auto-generated method stub
		if (file.exists()) {
			byte[] actualContent = getFileContent(file.getAbsolutePath());
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

	private File getFileHandle(ModelDescriptor md, String directoryPath) {
		File fHandle = new File(directoryPath + "\\" + md.getname() + "-" + md.getversion() + ".zip");
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
