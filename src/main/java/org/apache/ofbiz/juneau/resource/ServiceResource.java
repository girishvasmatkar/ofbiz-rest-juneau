// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the 'License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************

package org.apache.ofbiz.juneau.resource;

import static org.apache.juneau.http.HttpMethodName.GET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.juneau.rest.BasicRest;
import org.apache.juneau.rest.annotation.MethodSwagger;
import org.apache.juneau.rest.annotation.Rest;
import org.apache.juneau.rest.annotation.RestMethod;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;

@Rest(path = "/services", title = "Services Resource")
public class ServiceResource extends BasicRest {

	@RestMethod(name = GET, path = "/", summary = "Lists All Services", description = "Returns all OFBiz services", swagger = @MethodSwagger(tags = "listServices"))
	public List<Map<String, String>> listServices() throws IOException, GenericEntityException {
		HttpServletRequest httpRequest = getRequest();
		ServletContext servletContext = httpRequest.getServletContext(); // TODO Find better way getting ServletContext, possibly by injection
		LocalDispatcher dispatcher = (LocalDispatcher) servletContext.getAttribute("dispatcher");
		DispatchContext dContext = dispatcher.getDispatchContext();
		Set<String> serviceNames = dContext.getAllServiceNames();
		List<Map<String, String>> serviceList = new ArrayList<>();
		serviceNames.forEach((serviceName) -> {
			ModelService service = null;
			try {
				service = dContext.getModelService(serviceName);
			} catch (GenericServiceException e) {
				e.printStackTrace();
			}
			if (service != null && service.export) {
				Map<String, String> serviceDetails = new LinkedHashMap<String, String>();
				serviceDetails.put("serviceName", serviceName);
				serviceDetails.put("engineName", service.engineName);
				serviceDetails.put("defaultEntityName", service.defaultEntityName);
				serviceDetails.put("invoke", service.invoke);
				serviceList.add(serviceDetails);
			}
		});
		return serviceList;

	}

}
