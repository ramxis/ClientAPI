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
    class ClientAPI : IClientAPI
    {
        
        private FileInfo DownDir;
        private String baseUrl;

        private IHttpUtils http;
        private Result<String> response;
        private Result<FileInfo> fileContent;

        public ClientAPI(string url, FileInfo DownDir)
        {
            
            this.DownDir = DownDir;
            this.baseUrl = url;
            //this.DownUrl = "http://127.0.0.1:8080/ClientAPI-1/static/";
            http = new HttpUtils();
            EnforceFolders();
        }
        public ClientAPI(FileInfo DownDir)
        {
            this.DownDir = DownDir;
            http = new HttpUtils();
            EnforceFolders();
        }
        private void EnforceFolders()
        {
            bool folderExists = Directory.Exists(DownDir.FullName);
            if (!folderExists)
                Directory.CreateDirectory(DownDir.FullName);
        }

        public List<ModelDescriptor> getIndex()
        {
		
		
		    fileContent = http.getIndex(baseUrl + "static/index.json");
		    FileInfo file = fileContent.Content;
		    saveFile(file, "index.json");
            List<ModelDescriptor> index = new List<ModelDescriptor>();
            using (StreamReader r = new StreamReader(DownDir + "//index.json"))
            {
                string json = r.ReadToEnd();
                index = JsonConvert.DeserializeObject<List<ModelDescriptor>>(json);
            }

		    return index;
        }
        private Result<FileInfo> downloadFile(String downloadURI, String fileName, String Version) {

		    String downloadFile = fileName;
		    downloadFile = downloadFile + "-" + Version + ".zip";
		    fileContent =  http.download(downloadURI + downloadFile);
            if (fileContent.isOk)
                return fileContent;
            else
                throw new System.ArgumentException("File not found");
        }

       
       private Result<FileInfo> downloadFile(String fileName, String Version) {
		
		    return downloadFile(baseUrl + "static/", fileName, Version);
	    }
       public Result<FileInfo> download(ModelDescriptor md) {
		    return downloadFile(md.getname(), md.getversion());
	    }
       
        public Result<String> delete(ModelDescriptor md)
        {
            return deleteFile(baseUrl + "delete/", md.getname(), md.getversion());
        }

        private Result<String> deleteFile(String deleteURI, String fileName, String Version)
        {
            ModelDescriptor modelDesc = new ModelDescriptor(fileName, Version);
            var jsonString = new JavaScriptSerializer().Serialize(modelDesc);
            response = http.delete(jsonString, deleteURI);
            if (response.isOk)
                return response;
            else
                throw new System.ArgumentException("Failed : HTTP error code : ", response.errorMessage);
            throw new NotImplementedException();
        }
        public Result<String> upload(ModelDescriptor fileDescriptor, FileInfo file, CollisionHandling handling)
        {
		    switch (handling) 
            {
		        case CollisionHandling.OVERWRITE:
			        return UploadFile(baseUrl + "upload/", fileDescriptor, file);
                case CollisionHandling.THROW_EXECPTION:
			        UploadFile(baseUrl + "upload2/", fileDescriptor, file);
			        if (response.errorMessage=="THROW_EXECPTION") 
                    {
				        IOException e = new IOException();
				        throw e;
			        }
                   return null;

		       default:
			        return UploadFile(baseUrl + "upload/", fileDescriptor, file); // default
                   
            }
        }

        private Result<string> UploadFile(string uploadURI, ModelDescriptor fileDescriptor, FileInfo file)
        {
            byte[] bFile = FileToByteArray(file.FullName);
            UploadObject UPObj = new UploadObject(fileDescriptor, bFile);
            var json2 = new JavaScriptSerializer().Serialize(UPObj);
            response = http.upload(json2, uploadURI);
            if (response.isOk)
                return response;
            else
                return response;
            throw new NotImplementedException();
        }

        private bool saveFile(FileInfo file, string fileName)
        {

            if (System.IO.Directory.Exists(DownDir.FullName))
            {
                FileInfo fileSaved = new FileInfo(DownDir.FullName + "\\" + fileName);
                System.IO.File.Copy(file.FullName, fileSaved.FullName, true);
                return true;
            }
            else
            {
                EnforceFolders();
                FileInfo fileSaved = new FileInfo(DownDir.FullName + "\\" + fileName);
                System.IO.File.Copy(file.FullName, fileSaved.FullName, true);
                return true;
            }
            //return false;
            throw new NotImplementedException();
        }
        public bool saveFile(ModelDescriptor md, FileInfo file)  
        {

		    String fileName = md.getname() + "-" + md.getversion() + ".zip";
		    return saveFile(file, fileName);
        }
        public byte[] FileToByteArray(string fileName)
        {
            return File.ReadAllBytes(fileName);
        }
    }//class end
}
