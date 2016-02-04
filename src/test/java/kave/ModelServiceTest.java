package kave;
/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 *    - Muhammad Rameez
 */
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.stubbing.OngoingStubbing;

import com.google.gson.Gson;

public class ModelServiceTest {

	private ModelService sut;
	private IDeserializer deserializationModel;
	private ModelDescriptor deserializeModel;
	private IoUtils ioUtils;
	
	
	@Before
	public void setup() throws IOException {
		ioUtils = mock(IoUtils.class);
		deserializationModel = mock(IDeserializer.class);
		sut = new ModelService(ioUtils, deserializationModel);
		
	}

	@Test
	public void makeSureIndexIsNotEmpty() throws JSONException, IOException {

		ModelDescriptor expectedMD = mock(ModelDescriptor.class);
		byte[] expectedArr = new byte[] { 1, 2, 3 };
		
		when(deserializationModel.DeserializeUploadDesc(anyString())).thenReturn(new UploadObject(expectedMD, expectedArr));

		sut.upload("xyz");
		
		verify(ioUtils).addFile(expectedMD, expectedArr);

	}
	
	@Test
	public void makeSureRemoveisCalled() throws JSONException, IOException {

		ModelDescriptor expectedMD = mock(ModelDescriptor.class);
		boolean expected = true;
		
		when(deserializationModel.DeserializeModelDesc(anyString())).thenReturn(new ModelDescriptor("1","2"));

		sut.delete("xyz");
		
		verify(ioUtils).removeFile(new ModelDescriptor("1", "2"));

	}
	@Test
	public void makeSureAddisCalled() throws JSONException, IOException {

		ModelDescriptor expectedMD = mock(ModelDescriptor.class);
		byte[] expectedArr = new byte[] { 1, 2, 3 };
		
		when(deserializationModel.DeserializeUploadDesc(anyString())).thenReturn(new UploadObject(expectedMD, expectedArr));

		sut.uploadThrowException("xyz");
		
		verify(ioUtils).addFile(expectedMD, expectedArr);

	}
	
	
	
	private ModelDescriptor md(String name, String version) {
		return new ModelDescriptor("n" + name, "v" + version);
	}
}