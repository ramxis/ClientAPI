package kave;

import java.io.File;

public interface IHttpUtils<T> {

	Result<String> upload(String jsonString, String url);
	Result<T> download(String url);
	Result<String> delete(String JsonString, String url);

}
