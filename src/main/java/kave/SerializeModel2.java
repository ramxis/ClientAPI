package kave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializeModel2 {
	public  SerializeModel2() {
		// TODO Auto-generated constructor stub
	}
	// all json related functions
		public  String SerializeUploadObj(UploadObject2 objDescriptor) {
			// Configure GSON
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			Gson gson = gsonBuilder.create();
			String jsonString = gson.toJson(objDescriptor);
			return jsonString;

		}
		public  String SerializeModelDesc(ModelDescriptor2 FileDescriptor) {
			// Configure GSON
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			Gson gson = gsonBuilder.create();
			String jsonString = gson.toJson(FileDescriptor);
			return jsonString;

		}

		

}
