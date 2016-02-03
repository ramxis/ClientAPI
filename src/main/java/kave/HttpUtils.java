package kave;

import java.io.File;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class HttpUtils implements IHttpUtils {

	

	@Override
	public Result<String> upload(String jsonString, String url) {
		Result<String> res = new Result<String>();

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonString);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String statusMessage = response.getEntity(String.class);
		if (statusMessage.equals("Success!")) {
			res.setOk(true);
			res.setErrorMessage(statusMessage);

		} else {
			res.setErrorMessage(statusMessage);
			res.setOk(false);
		}
		return res;

	}

	@Override
	public Result<File> download(String url) {
		// TODO Auto-generated method stub
		Result<File> res = new Result<File>();
		ClientResponse response;
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		response = webResource.type("application/x-www-form-urlencode").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		res.setContent(response.getEntity(File.class));
		res.setOk(true);
		return res;
	}

	@Override
	public Result<String> delete(String jsonString, String url) {
		// TODO Auto-generated method stub
		Result<String> res = new Result<String>();

		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json").delete(ClientResponse.class, jsonString);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String statusMessage = response.getEntity(String.class);
		if (statusMessage.equals("deleteSuccessful!")) {
			res.setOk(true);
			res.setErrorMessage(statusMessage);

		} else {
			res.setErrorMessage(statusMessage);
			res.setOk(false);
		}
		return res;
	}

	@Override
	public Result<File> getIndex(String url) {
		// TODO Auto-generated method stub
		Result<File> res = new Result<File>();
		ClientResponse response;
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		response = webResource.type("application/x-www-form-urlencode").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		res.setContent(response.getEntity(File.class));
		res.setOk(true);
		return res;
		
	}

}
