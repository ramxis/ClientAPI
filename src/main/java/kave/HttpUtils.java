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
			res.setOk(false);
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
			res.setErrorMessage(response.getEntity(String.class));
			res.setOk(false);
			
		}

		String statusMessage = response.getEntity(String.class);
		if (statusMessage.equals("deleteSuccessful!")) {
			res.setOk(true);
			res.setErrorMessage(statusMessage);

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
