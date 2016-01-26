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
