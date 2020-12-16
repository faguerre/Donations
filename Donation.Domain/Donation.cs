using System;
using System.Collections.Generic;

namespace Donation.Domain
{
    public class Donation
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public virtual User CreatorUser { get; set; }
        public virtual User ConfirmedUser { get; set; }
        public virtual Address Address { get; set; }
        public string Description { get; set; }
        public virtual State State { get; set; }
        public virtual List<File> Files { get; set; }
        public virtual List<DonationTag> DonationTag { get; set; }
        public Donation()
        {
            State = new State();
            Files = new List<File>();
            Address = new Address();
        }
    }
}
