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
package kave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Deserializer implements IDeserializer {

	@Override
	public UploadObject DeserializeUploadDesc(String uploadobject) {
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		UploadObject descriptor = gson.fromJson(uploadobject, UploadObject.class);
		return descriptor;
	}

	@Override
	public ModelDescriptor DeserializeModelDesc(String modelDesc) {
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		ModelDescriptor descriptor = gson.fromJson(modelDesc, ModelDescriptor.class);
		return descriptor;

	}
}
