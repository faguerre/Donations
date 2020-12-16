using System;

namespace Donation.Domain
{

    public class UserTag
    {
        public int UserId { get; set; }

        public int TagId { get; set; }

        public virtual User User { get; set; }

        public virtual Tag Tag { get; set; }
    }
}