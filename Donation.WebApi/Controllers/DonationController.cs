using System;
using System.Collections.Generic;
using System.Linq;
using Donation.BusinessLogic.Interface;
using Donation.Domain;
using Microsoft.AspNetCore.Mvc;
using Donation.WebApi.Model;

namespace Donation.WebApi.Controllers
{
    
    [Route("api/donations")]
    [ApiController]
    public class DonationController : ControllerBase
    {
        private readonly IDonationLogic donationLogic;
		
		public DonationController(IDonationLogic IDonationLogic)
		{
            donationLogic = IDonationLogic;
		}

        //api/user/{id}
        [HttpGet("{id}", Name = "GetDonation")]
        public IActionResult Get(int id)
        {
           try
            {
                Result<Domain.Donation> result = this.donationLogic.Get(id);
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
       

         [HttpGet("", Name = "Dos")]
        public IActionResult GetAllDonations([FromQuery] bool active, bool confirmed)
        {
            try
            {
                string currentUserToken = HttpContext.Request.Headers["Authorization"];
                IEnumerable<Domain.Donation> donationList = new List<Domain.Donation>();
                if (active && confirmed)
                {
                    //donaciones confirmadas por el usuario
                    donationList = this.donationLogic.GetDonationsConfirmed(Guid.Parse(currentUserToken));
                    if (donationList.Count() == 0)
                    {
                        return Ok("No donations confirmed by the user");
                    }
                    else
                    {
                        return Ok(donationList);
                    }
                }
                else
                {
                    //donaciones creadas por el usuario
                    if (active)
                    {
                        donationList = this.donationLogic.GetDonationsCreatedByUser(currentUserToken, active);
                        if (donationList.Count() == 0)
                        {
                            return Ok("No donations created by this user");
                        }
                        else
                        {
                            return Ok(donationList);
                        }
                    }
                    else
                    {
                        //donations no creadas por el usuario
                        donationList = this.donationLogic.GetDonationsCreatedByUser(currentUserToken, active);
                        if (donationList.Count() == 0)
                        {
                            return Ok("No donations availables for these user");
                        }
                        else
                        {
                            return Ok(donationList);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }

        [HttpGet("all", Name = "Tres")]
        public IActionResult GetAll([FromQuery] bool active)
        {
            try
            {
               return Ok("All");                 
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }


        //api/donations
        [HttpPost]
        public IActionResult Post([FromBody] DonationModel donation)
        {
           try
            {        
                Domain.Donation _donation = donation.ToEntity();               
                Result<Domain.Donation> result = donationLogic.Add(_donation);
                Result<DonationTag> reaultTags = donationLogic.AddTag(result.ResultObject.Id, donation.Tags);
                if (result.ResultObject != null)
                {
                    return Ok("Ok");
                }
                else{
                    return BadRequest(result.Message);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }

        // api/user/{id}
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            try
            {
                Result<Domain.Donation> result = this.donationLogic.Delete(id);
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

        [HttpPut("{id}", Name = "ExtendDonation")]
        public IActionResult ExtendDonation(int id)
        {
            try
            {
                    Result<Domain.Donation> result = this.donationLogic.ExtendDonation(id);
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

        //api/donations/1
        [HttpPost("{id}", Name = "ConfirmDonation")]
        public IActionResult ConfirmDonation(int id)
        {
            // Using the HttpContext we can use the Authorization configured as token to search in database for the user.
            string currentUserToken = HttpContext.Request.Headers["Authorization"];
            try
            {
                    Result<Domain.Donation> result = this.donationLogic.ConfirmDonation(id, currentUserToken);
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

        [Route("test")]
        [HttpGet()]
        public IActionResult CheckPoint()
        {    
            try
            {
                return Ok("Test");
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }

        [HttpGet("user/{token}", Name = "token")]
        public IActionResult GetDonationForUserTags(Guid token)
        {
           try
            {
                IEnumerable<Domain.Donation> donationList = this.donationLogic.DonationsForUserTags(token);
                if (donationList.Count() == 0)
                {
                    return Ok("No donations for tags");
                }
                else
                {
                    return Ok(donationList);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }
    }
}