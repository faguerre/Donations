using System;
using System.Collections.Generic;
using System.Linq;
using Donation.BusinessLogic.Interface;
using Donation.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Donation.WebApi.Controllers
{
    
    [Route("api/events")]
    [ApiController]
    public class DonationEventController : ControllerBase
    {
        private readonly IDonationEventLogic donationEventLogic;
		
		public DonationEventController(IDonationEventLogic idonationEventLogic)
		{
            donationEventLogic = idonationEventLogic;
		}


        [HttpGet("{id}", Name = "GetDonationEvent")]
        public IActionResult Get(int id)
        {
           try
            {
                Result<DonationEvent> result = this.donationEventLogic.Get(id);
                if (result.ResultObject != null)
                {
                    return Ok(result.ResultObject);
                }
                else
                {
                    return BadRequest(result.Message);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
           try
            {
                IEnumerable<DonationEvent> donationEventList = this.donationEventLogic.GetAll();
                if (donationEventList.Count() == 0)
                {
                    return Ok("No events actives availables");
                }
                else
                {
                    return Ok(donationEventList);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }


        [HttpPost]
        public IActionResult Post([FromBody] DonationEventModel donationEvent)
        {

           try
            {        
                DonationEvent _donationEvent = donationEvent.ToEntity();               
                Result<DonationEvent> result = donationEventLogic.Add(_donationEvent);
                if (result.ResultObject != null)
                {
                    DonationEventModel newEvent = new DonationEventModel();
                    newEvent.FromEntity(result.ResultObject);
                    return Ok(newEvent);
                }
                else{
                    return BadRequest(result.Message);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return BadRequest(e.Message);
            }
        }


        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            try
            {
                Result<DonationEvent> result = this.donationEventLogic.Delete(id);
                if (result.ResultObject != null)
                {
                    return Ok(result.Message);
                }
                else
                {
                    return BadRequest(result.Message);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }
    }
}