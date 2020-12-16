using System;
using System.Collections.Generic;
using System.Linq;
using Donation.BusinessLogic.Interface;
using Donation.WebApi.Model;
using Donation.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Donation.WebApi.Controllers
{
    [Route("api/sessions")]
    [ApiController]
    public class SessionController : ControllerBase
    {
        private ISessionLogic sessionLogic;
        public SessionController(ISessionLogic sessionLogic)
        {
            this.sessionLogic = sessionLogic;
        }

        [HttpGet("{token}")]
        public IActionResult GetUser(Guid token)
        {
            try
            {
                Result<User> result = this.sessionLogic.GetUserFromToken(token);
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
                Console.WriteLine("error message " + e.Message);
                return BadRequest(e.Message);
            }
        }
        [HttpPost]
        public IActionResult Login([FromBody] UserLoginModel userLoginModel)
        {
            try
            {
                Result<Session> result = sessionLogic.Login(userLoginModel.Email, userLoginModel.Password);
                if (result.ResultObject != null)
                {
                    Guid token = result.ResultObject.Token;
                    var tokenOut = new TokenModel();
                    tokenOut.token = token.ToString();
                    return Ok(tokenOut);
                }
                else
                {
                    return BadRequest(result.Message);
                }
            }
            catch (ArgumentException e) { return BadRequest(e.Message); }

        }
        [HttpPost]
        [Route("external")]
        public IActionResult LoginExternal([FromBody] UserModelExternal userLoginModel)
        {
            try
            {
                Result<Session> result = sessionLogic.LoginExternal(userLoginModel.ToEntity());
                if (result.ResultObject != null)
                {
                    Guid token = result.ResultObject.Token;
                    var tokenOut = new TokenModel();
                    tokenOut.token = token.ToString();
                    return Ok(tokenOut);
                }
                else
                {
                    return BadRequest(result.Message);
                }
            }
            catch (ArgumentException e) { return BadRequest(e.Message); }

        }

        [HttpDelete("{token}")]
        public IActionResult Logout(Guid token)
        {
            try
            {
                Result<Session> result = this.sessionLogic.Logout(token);
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