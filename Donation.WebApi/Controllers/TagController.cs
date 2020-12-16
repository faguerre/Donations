using System;
using System.Collections.Generic;
using System.Linq;
using Donation.BusinessLogic.Interface;
using Donation.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Donation.WebApi.Controllers
{
    
    [Route("api/tags")]
    [ApiController]
    public class TagController : ControllerBase
    {
        private readonly ITagLogic tagLogic;
		
		public TagController(ITagLogic ITagLogic)
		{
            tagLogic = ITagLogic;
		}

        //api/tag/{id}
        [HttpGet("{id}", Name = "GetTag")]
        public IActionResult Get(int id)
        {
           try
            {
                Result<Tag> result = this.tagLogic.Get(id);
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

        // GET api/tags
        [HttpGet]
        public IActionResult GetAll()
        {
           try
            {
                IEnumerable<Tag> tagList = this.tagLogic.GetAll();
                if (tagList.Count() == 0)
                {
                    return Ok("No tags availables");
                }
                else
                {
                    return Ok(tagList);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }
    }
}