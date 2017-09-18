package com.example.uge01006.converter.Extractor;

/**
 * Interface for passing code that will be executed after the JS has finished
 */

public interface JsCallback {
	public abstract void onResult(String value);
	public abstract void onError(String errorMessage);
}
