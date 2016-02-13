using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClientAPI
{
    class Result<T>
    {
        public T Content { get; set; }
        public bool isOk { get; set; }
        public string errorMessage { get; set; }
    }
}
