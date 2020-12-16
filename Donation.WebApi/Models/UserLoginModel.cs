using System.ComponentModel.DataAnnotations;

namespace Donation.WebApi.Model
{
    public class UserLoginModel
    {
        [Required(ErrorMessage = "Email is required")]
        public string Email{  get; set;}
        [Required(ErrorMessage = "Password is required")]
        public string Password{  get; set;}
        public UserLoginModel() {}
    }
}