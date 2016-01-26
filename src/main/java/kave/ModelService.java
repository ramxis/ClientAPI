package kave;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONException;

import com.google.inject.Inject;

@JsonIgnoreProperties(ignoreUnknown = true)
@Path("/")
public class ModelService {

	private final IoUtils Io;
	private final IDeserializer deserializationModel;

	@Inject
	public ModelService(IoUtils io, IDeserializer receivedObject) throws IOException {
		this.Io = io;
		this.deserializationModel = receivedObject;
	}

	@POST
	@Path("/upload/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upload(String jsonString) throws JSONException, IOException {
		UploadObject UO = deserializationModel.DeserializeUploadDesc(jsonString);
		Io.addFile(UO.getModelDesc(), UO.getBytes());
		return Response.status(200).entity("File uploaded to : GITHub \n").build();

	}

	@DELETE
	@Path("/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(String jsonString) throws IOException, JSONException {
		ModelDescriptor delDescriptor = deserializationModel.DeserializeModelDesc(jsonString);
		if (Io.removeFile(delDescriptor))
			return Response.status(200).entity("File has been deleted").build();
		else
			return Response.status(201).entity("File/Index not found!").build();

	}
}