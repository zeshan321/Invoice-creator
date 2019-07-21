using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Models
{
    public class BoardTableModel
    {
        public string Search { get; set; }
        public string Order { get; set; }
        public int Offset { get; set; }
        public int Limit { get; set; }
    }
}
