using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Web.Script.Serialization;
using System.Net;

namespace ClientAPI
{
    class Program
    {
        private static ClientAPI test;
        static void Main(string[] args)
        {
            
            string URI = "http://127.0.0.1:8080/";
            string tomcat = "ClientAPI-1/";
            FileInfo DownDirectory = new FileInfo(@"F:\Github\download");
            string FilePath = @"C:\Users\fullt\Downloads\work-related.zip";
            test = new ClientAPI(URI+tomcat, DownDirectory);
            String filename = Path.GetFileNameWithoutExtension(FilePath);
            String version = "1";
            ModelDescriptor md = new ModelDescriptor(filename,version);
            FileInfo myFile = new FileInfo(FilePath);
            Result<String> result = test.upload(md, myFile, CollisionHandling.OVERWRITE);
            //make sure a file is in place to delete; otherwise an exception will occur
            filename = "Upload";
            version = "beta";
            md = new ModelDescriptor(filename, version);
            result = test.delete(md);
            List<ModelDescriptor> index = test.getIndex();
		    foreach (ModelDescriptor i in index)
            {
			
			    Result<FileInfo> output = test.download(i);
			    test.saveFile(i, output.Content);
		    }
             
           
            //this wll throw an exception as expected
            //result = test.upload(md, myFile, CollisionHandling.THROW_EXECPTION);
            
            
            
        }

       
    }

    
}
