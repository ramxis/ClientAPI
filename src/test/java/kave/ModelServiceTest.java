package kave;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class ModelServiceTest {

	private ModelService sut;
	private IDeserializer deserializationModel;
	private IoUtils ioUtils;

	@Before
	public void setup() throws IOException {
		ioUtils = mock(IoUtils.class);
		deserializationModel = mock(IDeserializer.class);

		sut = new ModelService(ioUtils, deserializationModel);
	}

	/*@Test
	public void makeSureIndexIsNotEmpty() throws JSONException, IOException {

		ModelDescriptor expectedMD = mock(ModelDescriptor.class);
		byte[] expectedArr = new byte[] { 1, 2, 3 };
		
		when(deserializationModel.DeserializeUploadDesc("xyz")).thenReturn(new UploadObject(expectedMD, expectedArr));

		//sut.upload("xyz");
		
		verify(ioUtils).addFile(expectedMD, expectedArr);

	}*/
}