using System;
using Donation.Domain;

namespace Donation.WebApi

{
    public class AddressModel
    {
        public string Name { get; set; }
        public string Latitute { get; set; }
        public string Longitute { get; set; }
        public AddressModel() {}

        internal Address ToEntity()
        {
            return new Address() {
                Name = this.Name,
                Latitute = this.Latitute,
                Longitute = this.Longitute
            };
        }
    }
}