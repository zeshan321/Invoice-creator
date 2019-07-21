using InvoiceCreator3.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Models
{
    public class SelectDataModel
    {
        public string Term { get; set; }
        public string Q { get; set; }
        public string _type { get; set; }
        public int Page { get; set; }
        public string Col { get; set; }
        public string Search { get; set; }
    }
}
