using System;
using Donation.Domain;
using System.ComponentModel.DataAnnotations;


namespace Donation.WebApi
{
    public class UserModelExternal
    {
        [Required(ErrorMessage = "Name is required")]
        public string Name {get; set;}
        [Required(ErrorMessage = "Email is required and should never be empty")]
        public string Email {get; set;}
        
        public UserModelExternal() {}

        public User ToEntity()
        {
            return new User()
            {
                Name = this.Name,
                Email = this.Email,
                IsExternal=true
            }; 
        }
    }
}
