using InvoiceCreator3.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Models
{
    public class SelectSearchModel
    {
        [JsonProperty("results")]
        public List<SelectText> SelectTexts { get; set; }

        public Pagination Pagination { get; set; }
    }
}
