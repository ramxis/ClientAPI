/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    - Sebastian Proksch
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kave.Deserializer;
import kave.IoUtils;
import kave.ModelService;



import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class GuiceConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new JerseyServletModule() {
			private File dataDir;
			private File tmpDir;


			@Override
			protected void configureServlets() {

				//dataDir = getPath("data");//for production comment in
				dataDir = new File("F:\\Github\\Upload");//comment out
				tmpDir = getPath("tmp");

				dataDir.mkdir();
				tmpDir.mkdir();


				ObjectMapper mapper = new ObjectMapper();
				bind(JacksonJsonProvider.class).toInstance(new JacksonJsonProvider(mapper));

				Map<String, String> params = new HashMap<String, String>();
				params.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, "WEB-INF/jsp");
				filterRegex("/((?!css|js|images).)*").through(GuiceContainer.class, params);
			}
			// check this implementation
			private File getPath(String folderName) {


				String qualifier = getServletContext().getInitParameter("qualifier");
				if (qualifier == null) {
					return new File(folderName);
				} else {
					return new File(folderName + "-" + qualifier);
				}
			}


			@Provides
			public IoUtils provideUploadCleanser() {
				return new IoUtils(tmpDir,dataDir,dataDir.getAbsolutePath());
			}
			@Provides
			public ModelService provideFeedbackService(IoUtils io) throws IOException {
				//return new ModelService(dataDir, tmpDir,io, tmpUfc, dataUfc);
				return new ModelService(io, new Deserializer());
			}

		});
	}
}