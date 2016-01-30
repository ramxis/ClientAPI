package kave;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.remote.JsonException;

public interface IClientAPI {

	List<ModelDescriptor> getIndex() throws IOException, JsonException;

	Result<String> upload(ModelDescriptor fileDescriptor, File file, CollisionHandling collionCase) throws IOException;

	Result<File> download(ModelDescriptor md) throws IOException;
	
	boolean saveFile(ModelDescriptor md, File file) throws IOException;

	Result<String> delete(ModelDescriptor md) throws IOException;
}