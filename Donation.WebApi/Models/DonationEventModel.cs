using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Donation.Domain;

namespace Donation.WebApi
{
    public class DonationEventModel
    {
        public int Id { get; set; }
        [Required(ErrorMessage = "Name related to the event is required")]
        public string Name { get; set; }
        [Required(ErrorMessage = "Date From is required")]
        public string ValidityFrom { get; set; }
        public string ValidityUntil { get; set; }
        [Required(ErrorMessage = "Description of the event is required")]

        public string Description { get; set; }

        [Required(ErrorMessage = "Email is required")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Phone is required")]
        public string Phone { get; set; }
        [Required(ErrorMessage = "Creator User is required")]

        public UserModel CreatorUser { get; set; }

        public virtual List<Address> Centers { get; set; }
        public DonationEventModel() { }

        public DonationEvent ToEntity()
        {
            DonationEvent donationEvent = new DonationEvent();
            donationEvent.Name = Name;
            donationEvent.ValidityFrom = DateTime.ParseExact(ValidityFrom, "dd/mm/yyyy",null);
            donationEvent.ValidityUntil =DateTime.ParseExact(ValidityUntil, "dd/mm/yyyy",null);
            donationEvent.Description = Description;
            donationEvent.Active = true;
            donationEvent.Email = Email;
            donationEvent.Phone = Phone;
            donationEvent.Centers = Centers;
            donationEvent.CreatorUser = CreatorUser.ToEntity();

            return donationEvent;
        }
        public void FromEntity(DonationEvent _donationEvent)
        {
            this.Id = _donationEvent.Id;
            this.Name = _donationEvent.Name;
            this.ValidityFrom = Convert.ToString(_donationEvent.ValidityFrom);
            this.ValidityUntil = Convert.ToString(_donationEvent.ValidityUntil);
            this.Description = _donationEvent.Description;
            this.Email = _donationEvent.Email;
            this.Phone = _donationEvent.Phone;
            this.Centers = _donationEvent.Centers;
            this.CreatorUser = new UserModel();
            this.CreatorUser.FromEntity(_donationEvent.CreatorUser);
        }
    }
}