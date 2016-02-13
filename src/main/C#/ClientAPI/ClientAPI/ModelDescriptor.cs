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
    class ModelDescriptor
    {
        //[JsonProperty]private String name;
        public String name;
        public String version;

        public ModelDescriptor(String name, String version)
        {
            this.name = name;
            this.version = version;
        }

        public ModelDescriptor()
        {
            // TODO Auto-generated constructor stub
        }

        public String getname()
        {
            return this.name;
        }

        public String getversion()
        {
            return this.version;
        }
        public void setversion(String newVersion)
        {
            this.version = newVersion;
        }
        public void setname(String newName)
        {
            this.name = newName;
        }

    }
}
