using System;

namespace Donation.Domain
{
    public class File
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Size { get; set; }
        public byte[] DataFiles { get; set; }
    }
}
