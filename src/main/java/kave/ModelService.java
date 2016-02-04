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
		return Response.status(200).entity("Success!").build();

	}
	
	@POST
	@Path("/upload2/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadThrowException(String jsonString) throws JSONException, IOException {
		UploadObject UO = deserializationModel.DeserializeUploadDesc(jsonString);
		if(!(Io.detectCollision(UO.getModelDesc()))){
			Io.addFile(UO.getModelDesc(), UO.getBytes());
			return Response.status(200).entity("Success!").build();
		}
		else
		{
			return Response.status(200).entity("THROW_EXECPTION").build();
		}
		

	}

	@DELETE
	@Path("/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(String jsonString) throws IOException, JSONException {
		ModelDescriptor delDescriptor = deserializationModel.DeserializeModelDesc(jsonString);
		if (Io.removeFile(delDescriptor))
			return Response.status(200).entity("deleteSuccessful!").build();
		else
			return Response.status(201).entity("File/Model not found!").build();

	}
}