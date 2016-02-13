/**
 * Copyright 2016 - Muhammad Rameez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 *    
 */
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClientAPI
{
    class UploadObject
    {
        public ModelDescriptor modelDesc;
        public byte[] bytes;

	    public UploadObject(ModelDescriptor modelDesc, byte[] filebyte)  {
		// TODO Auto-generated constructor stub
		// check if the filedescriptor exists already
		    this.modelDesc = modelDesc;
		    this.bytes = filebyte;

	    }

	    public ModelDescriptor getModelDesc() {
		    // TODO Auto-generated method stub
		    return this.modelDesc;
	    }

	    public byte[] getBytes() {
		    // TODO Auto-generated method stub
		    return this.bytes;
	    }
    
    }
}
