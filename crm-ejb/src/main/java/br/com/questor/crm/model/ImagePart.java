package br.com.questor.crm.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.Part;
public class ImagePart implements Part {
	
	private String contentType;
	private String header;
	private Collection<String> headerNames;
	private Collection<String> headers;
	private InputStream inputStream;
	private String name;
	private long size;

	@Override
	public void delete() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return this.contentType;
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return this.header;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return this.headerNames;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return this.headers;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return this.inputStream;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public void write(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setHeaders(Collection<String> headers) {
		this.headers = headers;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setHeaderNames(Collection<String> headerNames) {
		this.headerNames = headerNames;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
