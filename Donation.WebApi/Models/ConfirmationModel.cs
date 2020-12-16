using System;
using Donation.Domain;
using System.ComponentModel.DataAnnotations;


namespace Donation.WebApi
{
    public class ConfirmationModel
    {
        [Required(ErrorMessage = "DonationId is required")]
        public int DonationId {get; set;}
        [Required(ErrorMessage = "ConfirmedUserId is required")]
        public int ConfirmedUserId {get; set;}
        public ConfirmationModel() {}
    }
}
