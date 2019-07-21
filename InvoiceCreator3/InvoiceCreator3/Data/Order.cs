using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace InvoiceCreator3.Data
{
    public class Order
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public DateTime Date { get; set; }
        [Required]
        public string Store { get; set; }
        [Required]
        public string Model { get; set; }
        [Required]
        public string Serial { get; set; }
        [Required]
        public string Description { get; set; }
        [Required]
        public double Price { get; set; }
        [Required]
        public string Status { get; set; }
        public bool HasBeenPaid { get; set; }
        [Required]
        public string UserId { get; set; }
    }
}
