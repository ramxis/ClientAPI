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
