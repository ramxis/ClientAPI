/**
 * Copyright 2016 - Muhammad Rameez
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
 *    
 */
import static kave.CollisionHandling.OVERWRITE;
import static kave.CollisionHandling.THROW_EXECPTION;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kave.ClientAPI;
import kave.IClientAPI;
import kave.ModelDescriptor;
import kave.Result;

public class cientTest {
	private static final String UploadURl = null;

	public static void main(String[] args) throws IOException {

		String BaseUrl = "http://127.0.0.1:8080/";
		String BaseUrltomcat = "http://127.0.0.1:8080/";
		BaseUrl = BaseUrltomcat;

		String TestUploadObj = "C:\\Users\\rameez\\Downloads\\Upload.zip";
		String TestUploadObj2 = "C:\\Users\\rameez\\Downloads\\work-related.zip";
		String TestUploadObj3 = "C:\\Users\\rameez\\Downloads\\application packet.zip";
		String TestUploadObj4 = "C:\\Users\\rameez\\Downloads\\messages.mo";
		String fileName;
		String Version = "1.0";

		File downDir = new File("E:\\Github\\download\\");

		File someFile = new File(TestUploadObj); // get from somewhere
		fileName = someFile.getName().substring(0, someFile.getName().indexOf('.'));
		ModelDescriptor umd = new ModelDescriptor(fileName, "1.0");

		
		
		
		
		
		
		IClientAPI test = new ClientAPI(BaseUrl, downDir);
		test.upload(umd, someFile, OVERWRITE);
		test.upload(umd, someFile, OVERWRITE);
		test.delete(umd);
		someFile = new File(TestUploadObj2); // get from somewhere
		fileName = someFile.getName().substring(0, someFile.getName().indexOf('.'));
		umd = new ModelDescriptor(fileName, "1.0");
		test.upload(umd, someFile, THROW_EXECPTION);
		// test.delete(umd);

		List<ModelDescriptor> index = test.getIndex();
		for (ModelDescriptor md : index) {
			Result<File> output = test.download(md);
			test.saveFile(md, output.getContent());
		}

		test.delete(new ModelDescriptor());

	}

}
