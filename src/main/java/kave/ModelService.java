package kave;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONException;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;



@JsonIgnoreProperties(ignoreUnknown = true)
@Path("/")
public class ModelService {
	public static final String NO_SINGLE_UPLOAD = "Only a single file can be selected!";
	public static final String UPLOAD_FAILED = "The Uplaod Failed!";
	private IoUtils Io; // io being final might cause problems
	private UploadObject UO;
	private final File dataFolder;
	private final File tmpFolder;
	DeserializeModel receivedObject = new DeserializeModel();
	
	
	// checking the files if its zip and contains valid feed back
	// need to know what is a valid feedback file might need to delete or change
	// it!
	@Inject
	public ModelService(File dataFolder, File tmpFolder, IoUtils io)
			throws IOException {
		this.dataFolder = dataFolder;
		// this.dataFolder = new File("E:\\Github\\Upload\\");
		this.tmpFolder = tmpFolder;
		this.Io = io;
		enforceFolders();
	}

	// creates tmp and data folders
	private void enforceFolders() throws IOException {
		if (!dataFolder.exists()) {
			// File CustomFolder = new File("E:\\Github\\Upload\\");
			FileUtils.forceMkdir(dataFolder);
		}
		if (!tmpFolder.exists()) {
			FileUtils.forceMkdir(tmpFolder);
		}
	}

	@GET
	@Produces(MediaType.TEXT_HTML + "; charset=utf-8")
	public Viewable index() {
		return new Viewable("/index.jsp");
	}

	
	@POST
	@Path("/upload/")
	@Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	public Response receiveJSON(String jsonString) throws JSONException, IOException {
		UO = receivedObject.DeserializeUploadDesc(jsonString);
		Io.addFile(UO.getModelDesc(), UO.getBytes());
		return Response.status(200).entity("File uploaded to : GITHub \n").build();
		
	}


	

	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyQuery(@QueryParam("fname") String fileName) {
		return download(fileName);
	}

	@GET
	@Path("/download/{filename}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadFilebyPath(@PathParam("filename") String fileName) {
		return download(fileName);
	}

	private Response download(String fileName) {
		System.out.println("Downlaod was called");
		// download url = http://127.0.0.1:8080/0.zip
		File file = new File(dataFolder.getPath() + "\\" + fileName);
		System.out.println(dataFolder.getPath());
		if (fileName == null || fileName.isEmpty()) {
			ResponseBuilder response = Response.status(Status.BAD_REQUEST);
			return response.build();
		}
		if (file.exists()) {
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + file.getName());
			return response.build();
		} else {
			ResponseBuilder response = Response.status(404);
			response.header("FILE NOT FOUND: ", file.getName());

			return response.build();
		}

	}

	
	@DELETE
	//@POST
	@Path("/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	public Response remove(String jsonString) throws IOException, JSONException {
		ModelDescriptor delDescriptor = receivedObject.DeserializeModelDesc(jsonString);
		if (Io.removeFile(delDescriptor))
			return Response.status(200).entity("File has been deleted").build();
		else
			return Response.status(201).entity("File/Index not found!").build();
		
	}

	
	/*
	 * private File writeToTempFile(byte[] bytes) { // TODO Auto-generated
	 * method stub return null; }
	 */
}
