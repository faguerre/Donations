using System;

namespace Donation.Domain
{

    public class DonationTag
    {
        public int DonationId { get; set; }
        public int TagId { get; set; }
        public virtual Donation Donation { get; set; }
        public virtual Tag Tag { get; set; }
    }
}