package kave;

public class Result<T> {
	private T Content;
	private boolean isOk;
	private String errorMessage;

	public T getContent() {
		return Content;
	}

	public void setContent(T content) {
		Content = content;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
