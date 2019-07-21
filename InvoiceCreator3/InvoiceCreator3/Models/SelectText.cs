using InvoiceCreator3.Data;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Models
{
    public class SelectText : IEquatable<SelectText>
    {
        public int Id { get; set; }

        public string Text { get; set; }

        public bool Equals(SelectText other)
        {
            return Text == other.Text;
        }

        public override int GetHashCode()
        {
            int hashText = Text == null ? 0 : Text.GetHashCode();
            return hashText;
        }
    }
}
