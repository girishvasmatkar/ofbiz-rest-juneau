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
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.juneau.http.annotation.Path;
import org.apache.juneau.rest.BasicRest;
import org.apache.juneau.rest.annotation.MethodSwagger;
import org.apache.juneau.rest.annotation.Rest;
import org.apache.juneau.rest.annotation.RestMethod;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;


@Rest(path = "/entities", title = "Entities Resource")
public class EntityResource extends BasicRest {
	@RestMethod(name = GET, path = "/", summary = "Find Entity by Name", description = "Returns list of entities", swagger = @MethodSwagger(tags = "listEntities"))
	public TreeSet<String> listEntities() throws IOException, GenericEntityException {
		HttpServletRequest httpRequest = getRequest();
		ServletContext servletContext = httpRequest.getServletContext(); // TODO Find better way getting ServletContext, possibly by injection
		Delegator delegator = (Delegator) servletContext.getAttribute("delegator");
		ModelReader reader = delegator.getModelReader();
		TreeSet<String> entities = new TreeSet<String>(reader.getEntityNames());
		return entities;
	}

	@RestMethod(name = GET, path = "/{name}", summary = "Find Entity by Name", description = "Returns a single Entity", swagger = @MethodSwagger(tags = "getEntity"))
	public List<Map<String, Object>> getEntity(@Path("name") String name) {
		System.out.println("Entity Name " + name);
		HttpServletRequest httpRequest = getRequest();
		ServletContext servletContext = httpRequest.getServletContext(); // TODO Find better way getting ServletContext, possibly by injection
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Delegator delegator = (Delegator) servletContext.getAttribute("delegator");
		ModelEntity entity = delegator.getModelEntity(name);
		List<String> fieldNames = entity.getAllFieldNames();
		fieldNames.forEach((fieldName) -> {
			ModelField field = entity.getField(fieldName);
			String fType = field.getType();
			boolean isPk = field.getIsPk();
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("name", fieldName);
			map.put("type", fType);
			map.put("is_pk", isPk);
			response.add(map);
		});
		return response;
	}

}