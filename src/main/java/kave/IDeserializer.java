package kave;

public interface IDeserializer {
	

		UploadObject DeserializeUploadDesc(String uploadobject);

		ModelDescriptor DeserializeModelDesc(String modelDesc);

	

}
