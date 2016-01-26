using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading.Tasks;
using Newtonsoft.Json;
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
            string upload = "upload/";
            string DownDirectory = @"E:\Github\download";
            string FilePath = @"C:\Users\rameez\Downloads\work-related.zip";
            test = new ClientAPI(URI+tomcat, DownDirectory);
            String filename = Path.GetFileNameWithoutExtension(FilePath);
            String version = "1";
            ModelDescriptor md = new ModelDescriptor(filename,version);
           
            test.UploadFile(upload,md,FilePath);
            test.getIndex();
            
            
        }

       
    }

    class ClientAPI
    {
        private string BaseUrl;
        private string DownDir;
        private string DownUrl;
        public ClientAPI(string url ,string DownDir)
        {
           this.BaseUrl = url;
           this.DownDir = DownDir;
           this.DownUrl = url + "static/";
           EnforceFolders();
        }

        public ClientAPI(string DownDir) 
        {
            this.DownDir = DownDir;
            EnforceFolders();
	    }
        private void EnforceFolders()
        {
            bool folderExists = Directory.Exists(DownDir);
            if (!folderExists)
                Directory.CreateDirectory(DownDir);
        }

        public bool UploadFile(String uploadURI, ModelDescriptor fileDescriptor, String FilePath)
        {
            byte[] bFile = FileToByteArray(FilePath);
            UploadObject UPObj = new UploadObject(fileDescriptor, bFile);
            String json = JsonConvert.SerializeObject(UPObj);

            uploadURI = BaseUrl + uploadURI;
           

            string result = "";
            using (var client = new WebClient())
            {
                client.Headers[HttpRequestHeader.ContentType] = "application/json";
                result = client.UploadString(uploadURI, "POST", json);
            }
            Console.WriteLine(result);
            return true;
           

        }

        public void getIndex()
        {
            

            string url = DownUrl + "index.json";

            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);

            HttpWebResponse response = (HttpWebResponse)request.GetResponse();

            Stream resStream = response.GetResponseStream();
            var fileStream = File.Create(DownDir+ "//index.json");
            resStream.CopyTo(fileStream);
        }

        public byte[] FileToByteArray(string fileName)
        {
            return File.ReadAllBytes(fileName);
        }

        

    }
}
