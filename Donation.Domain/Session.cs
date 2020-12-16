using System;

namespace Donation.Domain
{
    public class Session
    {
        public int Id { get; set; }
        public virtual User User { get; set; }
        public Guid Token { get; set; }
        public DateTime CreateDate { get; set; }
    }
}