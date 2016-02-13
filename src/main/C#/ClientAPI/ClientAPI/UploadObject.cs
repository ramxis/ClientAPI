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
