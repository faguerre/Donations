using System;
using System.Collections.Generic;

namespace Donation.Domain
{
    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public int Phone { get; set; }
        public Boolean IsExternal { get; set; }
        public virtual List<Domain.UserTag> userTag { get; set; }
        public User() { }


    }
}
