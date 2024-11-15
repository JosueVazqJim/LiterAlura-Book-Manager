package org.vazquezj.literalura.service;

public interface IDataConverter {
	<T> T convertData(String json, Class<T> clase);
}
