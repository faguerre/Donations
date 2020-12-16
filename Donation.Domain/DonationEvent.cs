using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Donation.Domain
{
    public class DonationEvent
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public DateTime ValidityFrom { get; set; }
        public DateTime ValidityUntil { get; set; }
        public string Description { get; set; }
        public bool Active { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public virtual List<Domain.Address> Centers { get; set; }
        public virtual User CreatorUser { get; set; }

        public DonationEvent()
        {
            Centers = new List<Domain.Address>();
        }

        private bool isEmailValid()
        {

            return Regex.IsMatch(Email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$", RegexOptions.IgnoreCase);

        }
        private bool isPhoneValid()
        {

            return Regex.IsMatch(Phone, "^[0-9]+$", RegexOptions.IgnoreCase);

        }
        private bool isUserValid()
        {

            return (CreatorUser != null);
        }

        public Boolean IsValid()
        {

            return (!String.IsNullOrEmpty(Name)
                 && !String.IsNullOrEmpty(Description)
                 && !String.IsNullOrEmpty(Email)
                 && isEmailValid()
                 && isPhoneValid()
                 && isUserValid()
                 && !ValidityFrom.Equals(null)
                 && !ValidityUntil.Equals(null)
                 && (ValidityFrom.CompareTo(ValidityUntil) < 0));
        }
        public Boolean IsActive()
        {
            DateTime today = DateTime.Now;
            return this.Active &&
             (this.ValidityFrom <= today) &&
             (this.ValidityUntil >= today);

        }
    }
}