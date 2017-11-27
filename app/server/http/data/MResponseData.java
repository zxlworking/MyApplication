package com.dsv.appstore.http.data;

import java.lang.reflect.ParameterizedType;
import java.util.List;



@SuppressWarnings("serial")
public class MResponseData<T> implements java.io.Serializable{
	private String  status;
	private List<T> list;
	private T object;
	private String message;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MResponseData{" +
				"status='" + status + '\'' +
				", list=" + list +
				", object=" + object +
				", message='" + message + '\'' +
				'}';
	}
}
