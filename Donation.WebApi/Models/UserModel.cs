using System;
using Donation.Domain;
using System.ComponentModel.DataAnnotations;


namespace Donation.WebApi
{
    public class UserModel
    {
        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }
        [Required(ErrorMessage = "Email is required and should never be empty")]
        public string Email { get; set; }
        [Required(ErrorMessage = "Password is required")]
        public string Password { get; set; }
        [Required(ErrorMessage = "Phone is required")]
        public bool IsExternal { get; set; }
        public int Phone { get; set; }
        public UserModel() { }

        public User ToEntity()
        {
            return new User()
            {
                Name = this.Name,
                Email = this.Email,
                Phone = this.Phone,
                Password = this.Password,
                IsExternal = false
            };
        }
        public void FromEntity(User user)
        {
            Name = user.Name;
            Email = user.Email;
            Phone = user.Phone;
            Password = user.Password;
            IsExternal = user.IsExternal;
        }
    }
}
