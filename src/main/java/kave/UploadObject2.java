package kave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadObject2 {
	private ModelDescriptor2 modelDesc;
	private byte[] bytes;

	public UploadObject2(ModelDescriptor2 modelDesc, byte[] filebyte) throws IOException {
		// TODO Auto-generated constructor stub
		// check if the filedescriptor exists already
		this.modelDesc = modelDesc;
		this.bytes = filebyte;

	}

	public ModelDescriptor2 getModelDesc() {
		// TODO Auto-generated method stub
		return this.modelDesc;
	}

	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return this.bytes;
	}

}