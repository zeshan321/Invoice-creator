using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using InvoiceCreator3.Models;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using InvoiceCreator3.Data;
using Microsoft.EntityFrameworkCore;
using System.Reflection;

namespace InvoiceCreator3.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {
        private ApplicationDbContext _applicationDbContext;

        public HomeController(ApplicationDbContext applicationDbContext)
        {
            _applicationDbContext = applicationDbContext;
        }

        public IActionResult Index()
        {
            return View();
        }

        public async Task<IActionResult> GetTableData(BoardTableModel boardTableModel)
        {
            var userId = User.FindFirst(ClaimTypes.NameIdentifier).Value;
            var search = boardTableModel.Search?.ToUpper().Trim();

            var table = _applicationDbContext.Orders.Where(o => o.UserId == userId).OrderByDescending(o => o.ID);
            var total = await table.CountAsync();

            var data = table.Skip(boardTableModel.Offset).Take(boardTableModel.Limit);

            if (!string.IsNullOrWhiteSpace(search))
            {
                data = data.Where(o => o.Store.ToUpper().Contains(search)
                || o.Model.Contains(search)
                || o.Serial.ToUpper().Contains(search)
                || o.Description.ToUpper().Contains(search)
                || o.Status.ToUpper().Contains(search)
                || o.Price.ToString().ToUpper().Contains(search)
                || o.Date.ToShortDateString().ToUpper().Contains(search));
            }

            var test = await data.ToListAsync();

            var orderData = new OrderViewModel
            {
                Total = total,
                Orders = (await data.AnyAsync()) ? await data.ToListAsync() : new List<Order>()
            };

            return Json(orderData);
        }

        public async Task<IActionResult> GetSelectData(SelectDataModel selectDataModel)
        {
            PropertyInfo propertyInfo = typeof(Order).GetProperty(selectDataModel.Col, BindingFlags.IgnoreCase | BindingFlags.Public | BindingFlags.Instance);
            IQueryable<SelectText> modelSelectTexts = _applicationDbContext.Orders.OrderBy(o => o.ID).Select(o => new SelectText { Id = o.ID, Text = propertyInfo.GetValue(o, null).ToString() }).Distinct();

            if (!string.IsNullOrWhiteSpace(selectDataModel.Search))
            {
                modelSelectTexts = modelSelectTexts.Where(o => o.Text.Contains(selectDataModel.Search.ToLower()));
            }
            int total = await modelSelectTexts.CountAsync();
            return Json(new SelectSearchModel
            {
                SelectTexts = await modelSelectTexts
                .Skip(10 * selectDataModel.Page)
                .Take(10).ToListAsync(),
                Pagination = new Pagination { More = (total <= 10 * selectDataModel.Page) }
            });
        }


        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
