using InvoiceCreator3.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Models
{
    public class OrderViewModel
    {
        public int Total { get; set; }

        [JsonProperty("rows")]
        public List<Order> Orders { get; set; }
    }
}
