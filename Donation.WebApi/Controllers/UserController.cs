using System;
using System.Collections.Generic;
using System.Linq;
using Donation.BusinessLogic.Interface;
using Donation.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Donation.WebApi.Controllers
{
    
    [Route("api/users")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IUserLogic userLogic;
		
		public UserController(IUserLogic IUserLogic)
		{
            userLogic = IUserLogic;
		}

        //api/user/{id}
        [HttpGet("{id}", Name = "GetUser")]
        public IActionResult Get(int id)
        {
           try
            {
                Result<User> result = this.userLogic.Get(id);
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

        // GET api/users
        [HttpGet]
        public IActionResult GetAll()
        {
           try
            {
                IEnumerable<User> userList = this.userLogic.GetAll();
                if (userList.Count() == 0)
                {
                    return Ok("No users availables");
                }
                else
                {
                    return Ok(userList);
                }
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }
        }

       
        [HttpPost]
        public IActionResult Post([FromBody] UserModel userModel)
        {
            try
            {
                Result<User> result = this.userLogic.Add(userModel.ToEntity());
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

        
       
        // api/users/{id}
        [HttpPut("{id}", Name = "UpdateUser")]
        public IActionResult Put(int id, [FromBody] UserModel userModel)
        {
            try
            {

                Result<User> result = this.userLogic.Update(id, userModel.ToEntity());
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

        // api/user/{id}
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            try
            {
                Result<User> result = this.userLogic.Delete(id);
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

        // api/users/tags
        [HttpPost ("tags")]
        public IActionResult AddTagToUser([FromBody] UserTagModel userTagModel)
        {
            try
            {
                Result<UserTag> result = this.userLogic.AddTag(userTagModel.Token, userTagModel.Tags,
                    userTagModel.UserTags);
                if (result.Message != null)
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