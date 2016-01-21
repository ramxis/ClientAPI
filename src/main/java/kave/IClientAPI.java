package kave;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.remote.JsonException;

public interface IClientAPI {

	List<ModelDescriptor> getIndex() throws IOException, JsonException;

	boolean upload(ModelDescriptor fileDescriptor, File file, CollisionHandling collionCase) throws IOException;

	File download(ModelDescriptor md) throws IOException;
	
	boolean saveFile(ModelDescriptor md, File file) throws IOException;

	void delete(ModelDescriptor md) throws IOException;
}