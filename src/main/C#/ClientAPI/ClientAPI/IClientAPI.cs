using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClientAPI
{
    interface IClientAPI
    {
        List<ModelDescriptor> getIndex();

        Result<String> upload(ModelDescriptor fileDescriptor, FileInfo file, CollisionHandling collionCase);

        Result<FileInfo> download(ModelDescriptor md);
	
	    bool saveFile(ModelDescriptor md, FileInfo file);

        Result<String> delete(ModelDescriptor md);

    }
}
