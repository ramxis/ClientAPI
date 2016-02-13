using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClientAPI
{
    interface IHttpUtils
    {
       
        Result<String> upload(String jsonString, String url);
        Result<FileInfo> download(String url);
        Result<String> delete(String JsonString, String url);
        Result<FileInfo> getIndex(String url);
    }
}
