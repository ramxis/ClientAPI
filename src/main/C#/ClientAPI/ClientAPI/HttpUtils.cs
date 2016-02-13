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
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace ClientAPI
{
    class HttpUtils : IHttpUtils
    {
        public Result<String> upload(String jsonString, String uploadURI)
        {
            Result<String> res = new Result<String>();
            String response;
            using (var client = new WebClient())
            {
                client.Headers[HttpRequestHeader.ContentType] = "application/json";
                response = client.UploadString(uploadURI, "POST", jsonString);
            }

            if (response == "Success!")
            {
                res.isOk = true;
                res.errorMessage = response;

            }
            else
            {
                res.isOk = false;
                res.errorMessage = response;            
            }
            return res;
        }

        public Result<FileInfo> download(String url)
        {
            Result<FileInfo> res = new Result<FileInfo>();
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            Stream resStream = response.GetResponseStream();
            var fileName = Path.GetTempFileName().Replace(".tmp",".zip");
            int httpcode = (int)response.StatusCode;
            if (httpcode != 200)
            {
                res.errorMessage = response.StatusDescription;
                res.isOk = false;
            }
            else
            {
                using (FileStream fileStream = File.OpenWrite(fileName))
                {

                    resStream.CopyTo(fileStream);
                }
               FileInfo file = new FileInfo(fileName);
               res.Content = file;
               res.isOk = true;
            }
            
            return res;
        }
        public Result<String> delete(String JsonString, String url)
        {

           HttpWebResponse response;
           String statusMessage;
            Result<String> res = new Result<String>();
            WebRequest request = WebRequest.Create(url);
            request.Method = "DELETE";
            request.ContentType="application/json";
            
            //request.Headers.Add("Content-type", "application/json");
            using (var streamWriter = new StreamWriter(request.GetRequestStream()))
            {
                

                streamWriter.Write(JsonString);
                streamWriter.Flush();
            }
            
            response = (HttpWebResponse)request.GetResponse();
            using (var streamReader = new StreamReader(response.GetResponseStream()))
            {
                var result = streamReader.ReadToEnd();
                statusMessage = result;
            }
            int httpcode = (int)response.StatusCode;
            if (httpcode!=200)
            {
               res.errorMessage = response.StatusDescription;
               res.isOk = false;
            }
            //String statusMessage = response.StatusDescription;
             
		    if (statusMessage=="deleteSuccessful!") 
            {
			    res.isOk=true;
			    res.errorMessage=statusMessage;

		    } 
		return res;
        }
        public Result<FileInfo> getIndex(String url)
        {
            Result<FileInfo> res = new Result<FileInfo>();
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            Stream resStream = response.GetResponseStream();
            var fileName = Path.GetTempFileName().Replace(".tmp", ".json");
            int httpcode = (int)response.StatusCode;
            if (httpcode != 200)
            {
                res.errorMessage = response.StatusDescription;
                res.isOk = false;
            }
            else
            {
                using (FileStream fileStream = File.OpenWrite(fileName))
                {

                    resStream.CopyTo(fileStream);
                }
                FileInfo file = new FileInfo(fileName);
                res.Content = file;
                res.isOk = true;
            }

            return res;
        }
    }
}
