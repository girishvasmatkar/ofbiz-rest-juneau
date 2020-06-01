package org.apache.ofbiz.juneau.resource;

import org.apache.juneau.http.HttpMethodName;
import org.apache.juneau.rest.BasicRestServlet;
import org.apache.juneau.rest.annotation.Rest;
import org.apache.juneau.rest.annotation.RestMethod;

@Rest(path = "/helloWorld", title = "Hello World", description = "An example of the simplest-possible resource")
public class HelloWorldJuneau extends BasicRestServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5528772255104904452L;

	@RestMethod(name = HttpMethodName.GET, path = "/*", summary = "Responds with \"Hello world!\"")
	public String sayHello() {
		return "Hello world!";
	}
}
