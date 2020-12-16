using System;

namespace Donation.Domain
{
    public class Address
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Latitute { get; set; }
        public string Longitute { get; set; }
        public Address()
        {
        }
    }
}
